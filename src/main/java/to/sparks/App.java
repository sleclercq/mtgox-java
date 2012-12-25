package to.sparks;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import to.sparks.mtgox.MTGOXAPI;
import to.sparks.mtgox.dto.Ticker;

/**
 * Pass your MtGox API key and secret as the command line arguments.
 *
 */
public class App {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchAlgorithmException {


        // Pass your mtgox api key and secret as the command line args.
        MTGOXAPI mtgox = new MTGOXAPI(Logger.getGlobal(), args[0], args[1]);

        // Example of parsing mtgox public JSON sources, such as the ticker price
        Ticker ticker = mtgox.getTicker();
        logger.log(Level.INFO, "Last price: {0}", ticker.getLast());

        // Example of performing a private API function.
        double orderPrice = 1.00D; // $1.00
        double orderVolume = 1.00D; // 1 bitcoin
        mtgox.placeOrder(MTGOXAPI.OrderType.bid, orderPrice, orderVolume);

    }
}
