package to.sparks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import to.sparks.mtgox.MtGoxAPI;
import to.sparks.mtgox.model.Depth;
import to.sparks.mtgox.model.FullDepth;
import to.sparks.mtgox.model.Offer;
import to.sparks.mtgox.model.Ticker;
import to.sparks.mtgox.service.MtGoxHTTPClient;
import to.sparks.mtgox.service.MtGoxServiceImpl;
import to.sparks.mtgox.service.MtGoxWebSocketClient;

/*
 * Specify your MtGox API key & secret as JVM system properties, e.g.,
 * <code>java -Dapi.key=YOUR_KEY -Dapi.secret=YOUR_SECRET to.sparks.MtGoxExample</code>
 */
public class MtGoxExample {

    static final Logger logger = Logger.getLogger(MtGoxExample.class.getName());

    public static void main(String[] args) throws Exception {
        List<Offer> asks;
        List<Offer> bids;
        long mostRecentTimestamp;

        // Obtain a $USD instance of the API
        ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/Beans.xml");
        MtGoxAPI mtgoxUSD = (MtGoxAPI) context.getBean("mtgoxUSD");

        // Example of getting the current ticker price
        Ticker ticker = mtgoxUSD.getTicker();
        logger.log(Level.INFO, "Last price: {0}", ticker.getLast().getValue());

        // Example of code that would watch market depth
        logger.info("Downloading fulldepth...");
        MtGoxHTTPClient httpAPI = (MtGoxHTTPClient) context.getBean("httpApi");
        Currency currency = (Currency) context.getBean("usdCurrency");
        FullDepth fullDepth = httpAPI.getFullDepth(currency);
        asks = new ArrayList<>(Arrays.asList(fullDepth.getAsks()));
        bids = new ArrayList<>(Arrays.asList(fullDepth.getBids()));

        long mostRecentAskTimestamp = getMostRecentTimestamp(asks);
        long mostRecentBidTimestamp = getMostRecentTimestamp(bids);
        mostRecentTimestamp = mostRecentAskTimestamp < mostRecentBidTimestamp ? mostRecentBidTimestamp : mostRecentAskTimestamp;

        MtGoxWebSocketClient wsAPI = (MtGoxWebSocketClient) context.getBean("wsApi");
        List<Depth> updates = wsAPI.getAllDepthSince(mostRecentTimestamp);
        for (Depth update : updates) {
            if (update.getStamp() < mostRecentTimestamp) {
                logger.log(Level.WARNING, "Warning:  Out of order timestamp found. {0} < {1}", new Object[]{update.getStamp(), mostRecentTimestamp});
            } else {
                List<Offer> offers;
                if (update.getType_str().equalsIgnoreCase("ASK")) {
                    offers = asks;
                } else {
                    offers = bids;
                }

                mostRecentTimestamp = update.getStamp();

                double multiplier;
                // TODO: Verify multiplier values for all currencies
                switch (update.getCurrency().toLowerCase()) {
                    case "aud":
                        multiplier = MtGoxServiceImpl.AUD_INT_MULTIPLIER;
                        break;
                    default:
                        multiplier = MtGoxServiceImpl.USD_INT_MULTIPLIER;
                        break;
                }
                updateDepth(update, offers, multiplier);
            }
        }
        logger.log(Level.INFO, "Asks: {0}  Bids: {1}", new Object[]{asks != null ? asks.size() : "null", bids != null ? bids.size() : "null"});

        // TODO:  Other examples...  The API is very readable, just give it a try! :)

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

    private static void updateDepth(Depth update, List<Offer> depth, double covertToIntFactor) {

        List<Offer> emptyOffers = new ArrayList<>();
        for (Offer offer : depth) {
            if (offer.getAmount_int() == 0) {
                emptyOffers.add(offer);
            }
            if (offer.getPrice_int() == update.getPrice_int()) {
                double dAmount = ((double) update.getTotal_volume_int()) / covertToIntFactor;
                offer.setAmount_int(update.getTotal_volume_int());
                offer.setAmount(dAmount);
                offer.setStamp(update.getStamp());
                break;
            }
        }

        if (update.getAmount_int() > 0) {
            // There is nothing at this price point, add it to the collection.
            Offer offer = new Offer(update.getPrice(), update.getAmount(), update.getPrice_int(), update.getAmount_int(), update.getStamp());
            depth.add(offer);
        }

        // Clean the array.  Remove any offers of zero coins.
        for (Offer offer : emptyOffers) {
            depth.remove(offer);
        }

    }
}
