package to.sparks;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import to.sparks.mtgox.MtGoxAPI;
import to.sparks.mtgox.model.*;

/*
 * Specify your MtGox API key & secret as JVM system properties, e.g.,
 * <code>java -Dapi.key=YOUR_KEY -Dapi.secret=YOUR_SECRET
 * to.sparks.MtGoxExample</code>
 */
public class MtGoxExample {

    static final Logger logger = Logger.getLogger(MtGoxExample.class.getName());

    public static void main(String[] args) throws Exception {

        // Obtain a $USD instance of the API
        ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/Beans.xml");
        MtGoxAPI mtgoxUSD = (MtGoxAPI) context.getBean("mtgoxUSD");

        // Get the private account info
        Info info = mtgoxUSD.getInfo();
        logger.log(Level.INFO, "Logged into account: {0}", info.getLogin());
        
        // Example of getting the current ticker price
        Ticker ticker = mtgoxUSD.getTicker();
        logger.log(Level.INFO, "Last price: {0}", ticker.getLast().getDisplay());


        // Purchase 0.01000000 bitcoins for USD$0.00001
        // long price_int = 1l;
        // long amount_int = 1000000L;

        // Purchase 10 bitcoins for USD$0.0001
        // long price_int = 10L;
        // long amount_int = 1000000000L;

        // Purchase 0.1 bitcoins for USD$0.01
        // long price_int = 1000L;
        // long amount_int = 10000000L;

        // Purchase 1.0 bitcoins for USD$1.00
        long price_int = 100000L;
        long amount_int = 100000000L;

        MtGoxFiatUnit fiatUnit = MtGoxFiatUnit.createCurrencyInstance(price_int, mtgoxUSD.getBaseCurrency());
        MtGoxBitcoinUnit bitcoinUnit = MtGoxBitcoinUnit.createBitcoinInstance(amount_int);
        String orderRef = mtgoxUSD.placeOrder(MtGoxAPI.OrderType.Bid, fiatUnit, bitcoinUnit);
        logger.log(Level.INFO, "orderRef: {0}", new Object[]{orderRef});

        for (Order order : mtgoxUSD.getOpenOrders()) {
            logger.log(Level.INFO, "Open order: {0} status: {1} price: {2}{3} amount: {4}", new Object[]{order.getOid(), order.getStatus(), order.getCurrency().getCurrencyCode(), order.getPrice().getDisplay(), order.getAmount().getDisplay()});
        }

        // TODO:  Other examples...  The API is very readable, just give it a try! :)

    }

}
