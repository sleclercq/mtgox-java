package to.sparks;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import to.sparks.mtgox.MtGoxAPI;
import to.sparks.mtgox.dto.Ticker;

/*
 * Specify your API key & secret as JVM system properties, e.g,
 * java -Dapi.key=YOUR_KEY -Dapi.secret=YOUR_SECRET to.sparks.App
 */
public class App {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/Beans.xml");
        MtGoxAPI mtgoxUSD = (MtGoxAPI) context.getBean("mtgoxUSD");

        // Example of getting the current ticker price
        Ticker ticker = mtgoxUSD.getTicker();
        logger.log(Level.INFO, "Last price: {0}", ticker.getLast().getValue());

        /*
        // Example of performing a private API function.
        double orderPrice = 1.00D; // $1.00
        double orderVolume = 1.00D; // 1 bitcoin
        mtgoxUSD.placeOrder(MtGoxApiImpl.OrderType.Bid, orderPrice, orderVolume);        
        */

    }
}
