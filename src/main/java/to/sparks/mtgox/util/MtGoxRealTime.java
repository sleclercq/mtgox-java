package to.sparks.mtgox.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jwebsocket.client.java.BaseWebSocket;
import org.jwebsocket.kit.WebSocketException;
import to.sparks.mtgox.MTGOXAPI;
import to.sparks.mtgox.dto.Depth;
import to.sparks.mtgox.dto.FullDepth;
import to.sparks.mtgox.dto.Offer;
import to.sparks.mtgox.dto.Ticker;

/**
 * This class maintains a realtime state constantly updated by a websocket
 * connection to the MtGox exchange.
 *
 * @author SparksG
 */
public class MtGoxRealTime implements MtGoxListener {

    private static final Logger logger = Logger.getLogger(MtGoxRealTime.class.getName());
    private List<Depth> depthHistory = new CopyOnWriteArrayList<>();
    private List<Ticker> tickerHistory = new CopyOnWriteArrayList<>();
    private List<Offer> asks;
    private List<Offer> bids;
    private long mostRecentAskTimestamp;
    private long mostRecentBidTimestamp;
    private long mostRecentTimestamp;
    final BaseWebSocket websocket = new BaseWebSocket();

    public MtGoxRealTime(FullDepth fullDepth) throws WebSocketException, IOException {

        websocket.addListener(new MtGoxSocket(this, logger));
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

        asks = new ArrayList<>(Arrays.asList(fullDepth.getAsks()));
        bids = new ArrayList<>(Arrays.asList(fullDepth.getBids()));

        mostRecentAskTimestamp = getMostRecentTimestamp(asks);
        mostRecentBidTimestamp = getMostRecentTimestamp(bids);
        mostRecentTimestamp = mostRecentAskTimestamp < mostRecentBidTimestamp ? mostRecentBidTimestamp : mostRecentAskTimestamp;
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
    public void tickerEvent(Ticker ticker) {
        tickerHistory.add(ticker);
    }

    @Override
    public void depthEvent(Depth depth) {
        depthHistory.add(depth);

        List<Depth> updates = getAllDepthSince(mostRecentTimestamp);
//            System.out.println(updates.size() + " updates found.");
        for (Depth update : updates) {
            if (update.getStamp() < mostRecentTimestamp) {
                logger.log(Level.WARNING, "Warning:  Out of order timestamp found. {0} < {1}", new Object[]{update.getStamp(), mostRecentTimestamp});
            } else {
                mostRecentTimestamp = update.getStamp();
                if (update.getCurrency().equalsIgnoreCase("USD")) {
                    if (update.getType_str().equalsIgnoreCase("ASK")) {
                        updateDepth(update, asks, MTGOXAPI.USD_INT_MULTIPLIER);
                    } else {
                        updateDepth(update, bids, MTGOXAPI.USD_INT_MULTIPLIER);
                    }
                } else {
                    // Some other currency
                }
            }
        }
        logger.log(Level.INFO, "Asks: {0}  Bids: {1}", new Object[]{asks.size(), bids.size()});
    }

    private static void updateDepth(Depth update, List<Offer> depth, double covertToIntFactor) {

        List<Offer> emptyOffers = new ArrayList<>();
        for (Offer offer : depth) {
            if (offer.getAmount_int() == 0) {
                emptyOffers.add(offer);
            }
            if (offer.getPrice_int() == update.getPrice_int()) {
                double dAmount = ((double) update.getTotal_volume_int()) / covertToIntFactor;
                logger.log(Level.INFO, "Update at price {0}   Old volume: {1}  New volume: {2}", new Object[]{offer.getPrice(), offer.getAmount(), dAmount});
                offer.setAmount_int(update.getTotal_volume_int());
                offer.setAmount(dAmount);
                offer.setStamp(update.getStamp());
                break;
            }
        }

        if (update.getAmount_int() > 0) {
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

    public List<Depth> getDepthHistory() {
        return depthHistory;
    }

    public List<Ticker> getTickerHistory() {
        return tickerHistory;
    }

    public List<Offer> getAsks() {
        return asks;
    }

    public List<Offer> getBids() {
        return bids;
    }
}
