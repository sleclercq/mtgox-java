package to.sparks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

/*
 * Specify your MtGox API key & secret as JVM system properties, e.g.,
 * <code>java -Dapi.key=YOUR_KEY -Dapi.secret=YOUR_SECRET
 * to.sparks.MtGoxExample</code>
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
        logger.log(Level.INFO, "Last price: {0}", ticker.getLast().getDisplay());

        // Example of code that would watch market depth
        logger.info("Downloading fulldepth...");
        FullDepth fullDepth = mtgoxUSD.getFullDepth();

        asks = new ArrayList<>(Arrays.asList(fullDepth.getAsks()));
        bids = new ArrayList<>(Arrays.asList(fullDepth.getBids()));

        long mostRecentAskTimestamp = getMostRecentTimestamp(asks);
        long mostRecentBidTimestamp = getMostRecentTimestamp(bids);
        mostRecentTimestamp = mostRecentAskTimestamp < mostRecentBidTimestamp ? mostRecentBidTimestamp : mostRecentAskTimestamp;

        List<Depth> updates = mtgoxUSD.getAllDepthSince(mostRecentTimestamp);
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

                updateDepth(update, offers);
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

    private static void updateDepth(Depth update, List<Offer> depth) {

        List<Offer> emptyOffers = new ArrayList<>();
        for (Offer offer : depth) {
            if (offer.getAmount_int() == 0) {
                emptyOffers.add(offer);
            }
            if (offer.getPrice_int() == update.getPrice_int()) {
                offer.setAmount_int(update.getTotal_volume_int());
                offer.setStamp(update.getStamp());
                break;
            }
        }

        if (update.getAmount_int() > 0) {
            // There is nothing at this price point, add it to the collection.
            Offer offer = new Offer(update.getPrice_int(), update.getAmount_int(), update.getStamp());
            depth.add(offer);
        }

        // Clean the array.  Remove any offers of zero coins.
        for (Offer offer : emptyOffers) {
            depth.remove(offer);
        }

    }
}
