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
package to.sparks.mtgox.examples;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import to.sparks.mtgox.MtGoxAPI;
import to.sparks.mtgox.model.*;

/**
 * A trading bot that will maintain bid orders slightly below the last price.
 *
 * It should maintain a staggered set of bid orders below the last price that
 * are cancelled and reordered if they move too far from their calculated price
 * or volume.
 * The bids are for varying amounts of bitcoins, according to a configured array
 * of percentages.
 * The total fiat currency balance of the account is used in placing the orders.
 *
 * @author SparksG
 */
public class TradingBot {

    /* The logger */
    static final Logger logger = Logger.getLogger(TradingBot.class.getName());

    /* The percentage of total coins ordered in each staggered order */
    static final double[] percentagesOrderPriceSpread = new double[]{0.10D, 0.15D, 0.50D, 0.15D, 0.10D};

    /* The percentage below last price that each order should be staggered */
    static final double[] percentagesBelowLastPrice = new double[]{0.005D, 0.0075D, 0.0125D, 0.0150D, 0.02D};

    /* The percentage of price that an order is allowed to deviate before re-ordering
     * at the newly calculated prices */
    static final double percentAllowedPriceDeviation = 0.005D;

    /*
     * Note:  THIS CODE IS STILL INCOMPLETE - NOT READY FOR USE YET
     */
    public static void main(String[] args) throws Exception {

        // Obtain an instance of the API
        ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/mtgox/examples/Beans.xml");
        MtGoxAPI mtgoxAPI = (MtGoxAPI) context.getBean("mtgoxUSD");

//        Ticker ticker = mtgoxUSD.getTicker();
//        logger.log(Level.INFO, "Last price: {0}", ticker.getLast().getDisplay());

        // Get the private account info
        AccountInfo info = mtgoxAPI.getAccountInfo();
        logger.log(Level.INFO, "Logged into account: {0}", info.getLogin());

        CurrencyInfo baseCurrency = mtgoxAPI.getCurrencyInfo(mtgoxAPI.getBaseCurrency());
        String currencyCode = baseCurrency.getCurrency().getCurrencyCode();
        logger.log(Level.INFO, "Configured base currency: {0}", currencyCode);
        Wallet fiatWallet = info.getWallets().get(currencyCode);

        Ticker ticker = mtgoxAPI.getTicker();
        logger.log(Level.INFO, "Last price: {0}", ticker.getLast().toPlainString());

        BigDecimal numBTCtoOrder = fiatWallet.getBalance().divide(ticker.getAvg());
        logger.log(Level.INFO, "Trying to buy a total of {0} bitcoins.", numBTCtoOrder.toPlainString());

//        Wallet btcWallet = info.getWallets().get("BTC");

        Order[] openOrders = mtgoxAPI.getOpenOrders();

        if (isOrdersValid(openOrders)) {
            logger.info("The current orders remain valid.");
            for (Order order : openOrders) {
                logger.log(Level.INFO, "Open order: {0} status: {1} price: {2}{3} amount: {4}", new Object[]{order.getOid(), order.getStatus(), order.getCurrency().getCurrencyCode(), order.getPrice().getDisplay(), order.getAmount().getDisplay()});
            }
        } else {
            logger.info("There are invalid bid or ask orders, or none exist.");
            cancelOrders(mtgoxAPI, openOrders);
        }

        // Shutdown the api when you are finished
        mtgoxAPI.shutdown();
    }

    private static boolean isOrdersValid(Order[] orders) {
        boolean bRet = false;
        if (ArrayUtils.isNotEmpty(orders)
                && orders.length == percentagesOrderPriceSpread.length
                && orders.length == percentagesBelowLastPrice.length) {
            for (int i = 0; i < orders.length; i++) {
                Order order = orders[i];
                Double price = getPriceAtOrderIndex(i);

            }
        }
        return bRet;
    }

    private static void cancelOrders(MtGoxAPI mtGoxAPI, Order[] orders) throws Exception {
        if (ArrayUtils.isNotEmpty(orders)) {
            for (Order order : orders) {
                logger.log(Level.INFO, "Cancelling order: {0}", order.getOid());
                mtGoxAPI.cancelOrder(order);
            }
        } else {
            logger.info("There are no orders to cancel.");
        }
    }

    /**
     * Return the calculated price for the order at the given index in the
     * orders arrays.
     *
     * @param index The index of the order price/volume in the orders arrays.
     * @return The calulated price of the order at the given index
     */
    private static Double getPriceAtOrderIndex(int index) {
        return 0.0D;
    }

    /**
     * Have the orders deviated from the calculated prices?
     * The list must be sorted by descending price or this comparison will not
     * work.
     */
    private static boolean isWithinAllowedDeviation(BigDecimal ordersPriceAllowedDeviation, Order[] actualOrders, MtGoxFiatCurrency[] optimumPrices) {
        boolean bRet = true;
        for (int i = 0; i < actualOrders.length; i++) {
            BigDecimal actualPrice = actualOrders[i].getPrice().getPriceValue().getNumUnits();
            BigDecimal optimumPrice = optimumPrices[i].getNumUnits();
            BigDecimal diff;
            if (actualPrice.compareTo(optimumPrice) < 0) {
                diff = optimumPrice.subtract(actualPrice);
            } else {
                diff = actualPrice.subtract(optimumPrice);
            }
            if (diff.compareTo(ordersPriceAllowedDeviation) > 0) {
                bRet = false;
                break;
            }
        }
        return bRet;
    }
}
