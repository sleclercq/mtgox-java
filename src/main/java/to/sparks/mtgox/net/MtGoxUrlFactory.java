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
package to.sparks.mtgox.net;

import java.util.Currency;
import java.util.HashMap;

/**
 *
 * @author SparksG
 */
public class MtGoxUrlFactory {

    public enum RestCommand {

        PrivateOrderAdd,
        PrivateOrderResult,
        PrivateOrders,
        PrivateInfo,
        FullDepth,
        Ticker,
        CurrencyInfo
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
        restMap.put(RestCommand.PrivateInfo, "private/info");
        restMap.put(RestCommand.FullDepth, "fulldepth");
        restMap.put(RestCommand.Ticker, "ticker");
        restMap.put(RestCommand.CurrencyInfo, "currency");

    }

    public static String getUrlForRestCommand(Currency currency, RestCommand restCommand) throws Exception {
        StringBuilder url = new StringBuilder();
        if (currency == null || currencyMap.containsKey(currency)) {
            if (restMap.containsKey(restCommand)) {
                url.append(MTGOX_HTTP_API_URL);
                url.append(MTGOX_HTTP_API_VERSION);

                if (currency == null || restCommand == RestCommand.CurrencyInfo) {
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
