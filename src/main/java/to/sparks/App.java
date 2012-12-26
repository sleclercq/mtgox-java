package to.sparks;

import java.util.Currency;
import java.util.logging.Level;
import java.util.logging.Logger;
import to.sparks.mtgox.MtGoxAPI;
import to.sparks.mtgox.dto.Ticker;
import to.sparks.mtgox.impl.MtGoxApiImpl;

/**
 * Pass your MtGox API key and secret as the command line arguments.
 *
 */
public class App {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws Exception {

        // Create a $USD instance of the API
        MtGoxAPI mtgoxUSD = new MtGoxApiImpl(Logger.getGlobal(), Currency.getInstance("USD"), args[0], args[1]);

        // Example of parsing mtgox public JSON sources, such as the ticker price
        Ticker ticker = mtgoxUSD.getTicker();
        logger.log(Level.INFO, "Last price: {0}", ticker.getLast());

        // Example of performing a private API function.
        double orderPrice = 1.00D; // $1.00
        double orderVolume = 1.00D; // 1 bitcoin
        mtgoxUSD.placeOrder(MtGoxApiImpl.OrderType.Bid, orderPrice, orderVolume);

    }
}
