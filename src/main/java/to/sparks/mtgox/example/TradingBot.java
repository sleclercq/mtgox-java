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
package to.sparks.mtgox.example;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import to.sparks.mtgox.MtGoxHTTPClient;
import to.sparks.mtgox.event.StreamEvent;
import to.sparks.mtgox.event.TradeEvent;
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
public class TradingBot implements ApplicationListener<StreamEvent> {

    /* The logger */
    static final Logger logger = Logger.getLogger(TradingBot.class.getName());

    /* The percentage of total coins ordered in each staggered order */
    static final double[] percentagesOrderPriceSpread = new double[]{0.12D, 0.18D, 0.50D, 0.15D, 0.05D};

    /* The percentage below last price that each order should be staggered */
    static final double[] percentagesBelowLastPrice = new double[]{0.004D, 0.006D, 0.01D, 0.0140D, 0.018D};

    /* The percentage of price that an order is allowed to deviate before re-ordering
     * at the newly calculated prices */
    static final BigDecimal percentAllowedPriceDeviation = BigDecimal.valueOf(0.003D);
    private MtGoxHTTPClient mtgoxAPI;
    private AccountInfo info;
    private CurrencyInfo baseCurrency;

    public TradingBot(MtGoxHTTPClient mtgoxAPI) throws Exception {
        this.mtgoxAPI = mtgoxAPI;

        // Get the private account info
        info = mtgoxAPI.getAccountInfo();
        logger.log(Level.INFO, "Logged into account: {0}", info.getLogin());

        baseCurrency = mtgoxAPI.getCurrencyInfo(mtgoxAPI.getBaseCurrency());
        String currencyCode = baseCurrency.getCurrency().getCurrencyCode();
        logger.log(Level.INFO, "Configured base currency: {0}", currencyCode);

    }

    public static void main(String[] args) throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/mtgox/example/TradingBot.xml");
        TradingBot me = context.getBean("tradingBot", TradingBot.class);
    }

    @Override
    public void onApplicationEvent(StreamEvent event) {

        if (event instanceof TradeEvent) {
            try {
                Trade trade = (Trade) event.getPayload();
                if (trade.getPrice().getCurrencyInfo().equals(baseCurrency)) {
                    logger.log(Level.INFO, "Trade: {0} {1} volume: {2}", new Object[]{trade.getPrice_currency(), trade.getPrice().toPlainString(), trade.getAmount().toPlainString()});

                    if (trade.getAmount().compareTo(new MtGoxBitcoin(0.2D)) > 0) {

                        //        Wallet btcWallet = info.getWallets().get("BTC");

                        Order[] openOrders = mtgoxAPI.getOpenOrders();

                        Ticker ticker = mtgoxAPI.getTicker();
                        logger.log(Level.INFO, "Buy price: {0}", ticker.getBuy().getDisplay());

                        MtGoxFiatCurrency[] optimumPrices = new MtGoxFiatCurrency[percentagesBelowLastPrice.length];
                        for (int i = 0; i < percentagesBelowLastPrice.length; i++) {
                            optimumPrices[i] = getPriceAtOrderIndex(ticker.getBuy().getPriceValue(), i);
                        }

                        if (isOrdersValid(optimumPrices, openOrders)) {
                            logger.info("The current orders remain valid.");
//                        for (Order order : openOrders) {
//                            logger.log(Level.INFO, "Open order: {0} status: {1} price: {2}{3} amount: {4}", new Object[]{order.getOid(), order.getStatus(), order.getCurrency().getCurrencyCode(), order.getPrice().getDisplay(), order.getAmount().getDisplay()});
//                        }
                        } else {
                            logger.info("There are invalid bid or ask orders, or none exist.");
                            cancelOrders(mtgoxAPI, openOrders);

                            Wallet fiatWallet = info.getWallets().get(baseCurrency.getCurrency().getCurrencyCode());
                            MtGoxBitcoin numBTCtoOrder = new MtGoxBitcoin(fiatWallet.getBalance().divide(ticker.getBuy().getPriceValue()));
                            logger.log(Level.INFO, "Trying to buy a total of {0} bitcoins.", numBTCtoOrder.toPlainString());
                            for (int i = 0; i < optimumPrices.length; i++) {
                                MtGoxBitcoin vol = new MtGoxBitcoin(numBTCtoOrder.multiply(BigDecimal.valueOf(percentagesOrderPriceSpread[i])));
                                logger.log(Level.INFO, "Placing order at price: {0}{1} amount: {2}", new Object[]{optimumPrices[i].getCurrencyInfo().getCurrency().getCurrencyCode(), optimumPrices[i].getNumUnits(), vol.toPlainString()});
                                String ref = mtgoxAPI.placeOrder(MtGoxHTTPClient.OrderType.Bid, optimumPrices[i], vol);
                                logger.log(Level.INFO, "Order ref: {0}", ref);
                            }
                        }
                    } else {
                        logger.info("Volume too small for trigger.");
                    }
                }
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    private static boolean isOrdersValid(MtGoxFiatCurrency[] optimumPrices, Order[] orders) {
        boolean bRet = false;
        if (ArrayUtils.isNotEmpty(orders)
                && orders.length == percentagesOrderPriceSpread.length
                && orders.length == percentagesBelowLastPrice.length) {
            return isWithinAllowedDeviation(percentAllowedPriceDeviation, orders, optimumPrices);
        }
        return bRet;
    }

    private static void cancelOrders(MtGoxHTTPClient mtGoxAPI, Order[] orders) throws Exception {
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
    private static MtGoxFiatCurrency getPriceAtOrderIndex(MtGoxFiatCurrency lastPrice, int index) {
        BigDecimal price = lastPrice.subtract(lastPrice.multiply(BigDecimal.valueOf(percentagesBelowLastPrice[index])));
        return new MtGoxFiatCurrency(price, lastPrice.getCurrencyInfo());
    }

    /**
     * Have the orders deviated from the calculated prices?
     * The list must be sorted by descending price or this comparison will not
     * work.
     */
    private static boolean isWithinAllowedDeviation(BigDecimal percentAllowedPriceDeviation, Order[] actualOrders, MtGoxFiatCurrency[] optimumPrices) {
        boolean bRet = true;
        // Sort the actual orders by price descending, so that they can be compared to the optimum prices array which is also in that order.
        List<Order> sortedActualOrders = Arrays.asList(actualOrders);
        Collections.sort(sortedActualOrders, new ReverseComparator(new Comparator<Order>() {

            @Override
            public int compare(Order o1, Order o2) {
                return o1.getPrice().getPriceValue().compareTo(o2.getPrice().getPriceValue().getNumUnits());
            }
        }));

        for (int i = 0; i < sortedActualOrders.size(); i++) {
            BigDecimal actualPrice = sortedActualOrders.get(i).getPrice().getPriceValue().getNumUnits();
            BigDecimal optimumPrice = optimumPrices[i].getNumUnits();
            BigDecimal diff;
            if (actualPrice.compareTo(optimumPrice) < 0) {
                diff = optimumPrice.subtract(actualPrice);
            } else {
                diff = actualPrice.subtract(optimumPrice);
            }
            if (diff.compareTo(optimumPrice.multiply(percentAllowedPriceDeviation)) > 0) {
                bRet = false;
                break;
            }
        }
        return bRet;
    }
}
