/*
 * The MtGox-Java API is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The MtGox-Java API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the Lesser GNU General Public License
 * along with the MtGox-Java API .  If not, see <http://www.gnu.org/licenses/>.
 */
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

        // Obtain information about the base currency of the API instance
        CurrencyInfo currencyInfo = mtgoxUSD.getCurrencyInfo(mtgoxUSD.getBaseCurrency());
        logger.log(Level.INFO, "Base currency: {0}", currencyInfo.getCurrency().getCurrencyCode());

        // Example of getting the current ticker price
        Ticker ticker = mtgoxUSD.getTicker();
        logger.log(Level.INFO, "Last price: {0}", ticker.getLast().getDisplay());

        try {
            // Get the private account info
            AccountInfo info = mtgoxUSD.getAccountInfo();
            logger.log(Level.INFO, "Logged into account: {0}", info.getLogin());

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

            MtGoxUnitOfCredit fiatUnit = new MtGoxUnitOfCredit(price_int, currencyInfo);
            MtGoxUnitOfCredit bitcoinUnit = new MtGoxUnitOfCredit(amount_int);
            String orderRef = mtgoxUSD.placeOrder(MtGoxAPI.OrderType.Bid, fiatUnit, bitcoinUnit);
            logger.log(Level.INFO, "orderRef: {0}", new Object[]{orderRef});

            for (Order order : mtgoxUSD.getOpenOrders()) {
                logger.log(Level.INFO, "Open order: {0} status: {1} price: {2}{3} amount: {4}", new Object[]{order.getOid(), order.getStatus(), order.getCurrency().getCurrencyCode(), order.getPrice().getDisplay(), order.getAmount().getDisplay()});
            }

            mtgoxUSD.cancelOrder(MtGoxAPI.OrderType.Bid, orderRef);

            for (Order order : mtgoxUSD.getOpenOrders()) {
                logger.log(Level.INFO, "Open order: {0} status: {1} price: {2}{3} amount: {4}", new Object[]{order.getOid(), order.getStatus(), order.getCurrency().getCurrencyCode(), order.getPrice().getDisplay(), order.getAmount().getDisplay()});
            }
        } catch (IllegalArgumentException ex) {
            logger.log(Level.SEVERE, "Private functions require your private mtgox api keys. Run java with these command line arguments:  -Dapi.key=YOUR_KEY -Dapi.secret=YOUR_SECRET ", ex);
        }

        // TODO:  Other examples...  The API is very readable, just give it a try! :)

    }
}
