package to.sparks.mtgox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.client.java.BaseWebSocket;
import org.jwebsocket.kit.RawPacket;
import org.jwebsocket.kit.WebSocketException;
import to.sparks.mtgox.dto.*;
import to.sparks.mtgox.util.JSONSource;

public class MtGoxRealTime implements WebSocketClientListener {

    private static final Logger logger = Logger.getLogger(MtGoxRealTime.class.getName());
    private List<Depth> depthHistory = new CopyOnWriteArrayList<>();

    public MtGoxRealTime() throws WebSocketException {
        final BaseWebSocket websocket = new BaseWebSocket();
        websocket.addListener(this);
        websocket.open("ws://websocket.mtgox.com/mtgox");

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                System.out.println("Closing connection...");
                try {
                    websocket.close();
                    //System.exit(0);
                } catch (WebSocketException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /*
     * Return ALL the depth items younger than the timestamp
     */
    public List<Depth> getAllDepthSince(long timestamp) {
        List<Depth> result = new ArrayList<>();
        int length = depthHistory.size();
        for (int i = 0; i < length; i++) {
            Depth depth = depthHistory.get(i);
            if (depth.getStamp() > timestamp) {
                result = depthHistory.subList(i, length - 1);
                break;
            }
        }
        return result;
    }

    @Override
    public void processOpened(WebSocketClientEvent aEvent) {
        // The websocket has been opened
    }

    @Override
    public void processPacket(WebSocketClientEvent aEvent, WebSocketPacket aPacket) {
        if (aEvent != null) {
            if (aPacket != null && aPacket.getFrameType() == RawPacket.FRAMETYPE_UTF8) {
                try {
                    // System.out.println(aPacket.getUTF8());

                    JsonFactory factory = new JsonFactory();
                    ObjectMapper mapper = new ObjectMapper();

                    JsonParser jp = factory.createJsonParser(aPacket.getUTF8());
                    DynaBean op = mapper.readValue(jp, DynaBean.class);

                    if (op.get("op") != null && op.get("op").equals("private")) {
                        String messageType = op.get("private").toString();
                        if (messageType.equalsIgnoreCase("ticker")) {
                            OpPrivateTicker opPrivateTicker = mapper.readValue(factory.createJsonParser(aPacket.getUTF8()), OpPrivateTicker.class);
                            Ticker ticker = opPrivateTicker.getTicker();
                            //  logger.log(Level.INFO, "Ticker: currency: {0}", new Object[]{ticker.getAvg().getCurrency()});
                        } else if (messageType.equalsIgnoreCase("depth")) {
                            OpPrivateDepth opPrivateDepth = mapper.readValue(factory.createJsonParser(aPacket.getUTF8()), OpPrivateDepth.class);
                            Depth depth = opPrivateDepth.getDepth();
                            depthHistory.add(depth);
                            //    logger.log(Level.INFO, "Depth currency: {0}", new Object[]{depth.getCurrency()});
                        } else {
                            logger.log(Level.INFO, "Unknown private operation: {0}", new Object[]{aPacket.getUTF8()});
                        }

                        // logger.log(Level.INFO, "messageType: {0}, payload: {1}", new Object[]{messageType, dataPayload});
                    } else {
                        logger.log(Level.INFO, "Unknown operation: {0}, payload: {1}", new Object[]{op.get("op")});
                        // TODO:  Process the following types
                        // subscribe
                        // unsubscribe
                        // remark
                        // result
                    }
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public void processClosed(WebSocketClientEvent aEvent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) throws WebSocketException, IOException, InterruptedException {

        MtGoxRealTime mtGoxSocket = new MtGoxRealTime();

        JSONSource<Result<FullDepth>> fullDepthJSON = new JSONSource<>();

        FullDepth fullDepthUSD = fullDepthJSON.getResultFromStream(new URL("https://mtgox.com/api/1/BTCUSD/fulldepth").openStream(), FullDepth.class).getReturn();

        List<Offer> asksUSD = new ArrayList<>(Arrays.asList(fullDepthUSD.getAsks()));
        List<Offer> bidsUSD = new ArrayList<>(Arrays.asList(fullDepthUSD.getBids()));

        //       System.out.println("Start Depth size asks: " + asksUSD.size() + " bids: " + bidsUSD.size());
        long mostRecentAskTimestamp = getMostRecentTimestamp(asksUSD);
        long mostRecentBidTimestamp = getMostRecentTimestamp(bidsUSD);

        long mostRecentTimestamp = mostRecentAskTimestamp < mostRecentBidTimestamp ? mostRecentBidTimestamp : mostRecentAskTimestamp;

        while (true) {
            Thread.sleep(1000);  // TODO: We only really need to do processing when updates acutally arrive, rather than every X seconds.
            List<Depth> updates = mtGoxSocket.getAllDepthSince(mostRecentTimestamp);
//            System.out.println(updates.size() + " updates found.");
            for (Depth update : updates) {
                if (update.getStamp() < mostRecentTimestamp) {
                    System.out.println("Warning:  Younger timestamp.");
                } else {
                    mostRecentTimestamp = update.getStamp();
                    if (update.getCurrency().equalsIgnoreCase("USD")) {
                        if (update.getType_str().equalsIgnoreCase("ASK")) {
                            updateDepth(update, asksUSD);
                        } else {
                            updateDepth(update, bidsUSD);
                        }
                    } else {
                        // Some other currency
                    }
                }
            }
            logger.log(Level.INFO, "Asks: {0}  Bids: {1}", new Object[]{asksUSD.size(), bidsUSD.size()});
        }

    }

    private static void updateDepth(Depth update, List<Offer> depth) {
        boolean found = false;

        List<Offer> emptyOffers = new ArrayList<>();
        for (Offer offer : depth) {

            if (offer.getPrice_int() == update.getPrice_int()) {
                double dAmount = ((double) update.getTotal_volume_int()) / 100000000.0D; // TODO:  This value is currency dependent.  JPY is different from USD for example.
                logger.log(Level.INFO, "Update at price {0}   Old volume: {1}  New volume: {2}", new Object[]{offer.getPrice(), offer.getAmount(), dAmount});
                offer.setAmount_int(update.getTotal_volume_int());
                offer.setAmount(dAmount);
                offer.setStamp(update.getStamp());
                found = true;
            }
            if (offer.getAmount_int() == 0) {
                emptyOffers.add(offer);
            }
            if (found) {
                break;
            }
        }

        if (!found && update.getAmount_int() > 0) {
            logger.log(Level.INFO, "New offer at price {0}   volume: {1}  (total {2})", new Object[]{update.getPrice(), update.getAmount(), update.getTotal_volume_int()});

            // There is nothing at this price point, add it to the collection.
            Offer offer = new Offer(update.getPrice(), update.getAmount(), update.getPrice_int(), update.getAmount_int(), update.getStamp());
            depth.add(offer);
        }

        // Clean the array.  Remove any offers of zero coins.
        for (Offer offer : emptyOffers) {
            depth.remove(offer);
        }

    }

    private static long getMostRecentTimestamp(List<Offer> offers) {
        long mostRecentTimestamp = 0;
        for (Offer offer : offers) {
            if (offer.getStamp() > mostRecentTimestamp) {
                mostRecentTimestamp = offer.getStamp();
            }
        }
        return mostRecentTimestamp;
    }
}
