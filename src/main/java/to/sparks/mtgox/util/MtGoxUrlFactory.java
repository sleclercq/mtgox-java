package to.sparks.mtgox.util;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author SparksG
 */
public class MtGoxUrlFactory {

    public enum RestCommand {

        PrivateOrderAdd,
        PrivateOrderResult,
        PrivateOrders,
        FullDepth,
        Ticker
    }
    private static String MTGOX_HTTP_API_URL = "https://mtgox.com/api/";
    private static String MTGOX_HTTP_API_VERSION = "1/";
    private static final HashMap<Currency, String> currencyMap;
    private static final HashMap<RestCommand, String> restMap;
    private static final String[] currencyList = {"USD", "AUD", "CAD", "CHF", "CNY", "DKK", "EUR", "GBP", "HKD", "JPY", "NZD", "PLN", "RUB", "SEK", "SGD", "THB"};

    static {
        currencyMap = new HashMap<>();
        for (String currency : currencyList) {
            currencyMap.put(Currency.getInstance(currency), "BTC" + currency + "/");
        }

        restMap = new HashMap<>();
        restMap.put(RestCommand.PrivateOrderAdd, "private/order/add");
        restMap.put(RestCommand.PrivateOrderResult, "private/order/result");
        restMap.put(RestCommand.PrivateOrders, "private/orders");
        restMap.put(RestCommand.FullDepth, "fulldepth");
        restMap.put(RestCommand.Ticker, "ticker");

    }

    public static String getUrlForRestCommand(Currency currency, RestCommand restCommand) throws Exception {
        StringBuilder url = new StringBuilder();
        if (currency == null || currencyMap.containsKey(currency)) {
            if (restMap.containsKey(restCommand)) {
                url.append(MTGOX_HTTP_API_URL);
                url.append(MTGOX_HTTP_API_VERSION);

                if (currency == null) {
                    url.append("generic/");
                } else {
                    url.append(currencyMap.get(currency));
                }

                url.append(restMap.get(restCommand));
            } else {
                throw new Exception("Unknown command: " + restCommand.toString());
            }
        } else {
            throw new Exception("Unknown currency: " + currency.getCurrencyCode());
        }
        return url.toString();
    }
}
