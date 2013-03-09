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
package to.sparks.mtgox.service;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * TODO: This should be a Spring service API.
 *
 * @author SparksG
 */
class UrlFactory {

    public enum RestCommand {

        PrivateOrderAdd,
        PrivateOrderResult,
        PrivateOrders,
        PrivateInfo,
        FullDepth,
        Ticker,
        CurrencyInfo,
        PrivateOrderCancel,
        SendBitcoins,
        Lag
    }
    private static String MTGOX_HTTP_API_URL = "https://mtgox.com/api/";
    private static String MTGOX_HTTP_API_CACHE_URL = "https://data.mtgox.com/api/";
    private static String MTGOX_HTTP_API_VERSION_0 = "0/";
    private static String MTGOX_HTTP_API_VERSION_1 = "1/";
    private static final HashMap<Currency, String> currencyMap;
    private static final HashMap<RestCommand, String> apiV0RestMap;
    private static final HashMap<RestCommand, String> apiV1RestMap;
    private static final List<RestCommand> readOnlyCommands;
    private static final String[] currencyList = {"USD", "AUD", "CAD", "CHF", "CNY", "DKK", "EUR", "GBP", "HKD", "JPY", "NZD", "PLN", "RUB", "SEK", "SGD", "THB"};

    static {
        currencyMap = new HashMap<>();
        for (String currency : currencyList) {
            currencyMap.put(Currency.getInstance(currency), "BTC" + currency + "/");
        }

        // These commands are ok to get from the cache api
        readOnlyCommands = new ArrayList<>();
        readOnlyCommands.add(RestCommand.Ticker);

        apiV1RestMap = new HashMap<>();
        apiV1RestMap.put(RestCommand.PrivateOrderAdd, "private/order/add");
        apiV1RestMap.put(RestCommand.PrivateOrderResult, "private/order/result");
        apiV1RestMap.put(RestCommand.PrivateOrders, "private/orders");
        apiV1RestMap.put(RestCommand.PrivateInfo, "private/info");
        apiV1RestMap.put(RestCommand.FullDepth, "fulldepth");
        apiV1RestMap.put(RestCommand.Ticker, "ticker");
        apiV1RestMap.put(RestCommand.CurrencyInfo, "currency");
        apiV1RestMap.put(RestCommand.SendBitcoins, "bitcoin/send_simple");
        apiV1RestMap.put(RestCommand.Lag, "order/lag");

        apiV0RestMap = new HashMap<>();
        apiV0RestMap.put(RestCommand.PrivateOrderCancel, "cancelOrder.php");

    }

    /**
     * Only use this for 'generic' commands that don't depend on currency.
     */
    public static String getUrlForRestCommand(RestCommand restCommand) throws Exception {
        return getUrlForRestCommand("", restCommand);
    }

    public static String getUrlForRestCommand(Currency currency, RestCommand restCommand) throws Exception {
        return getUrlForRestCommand(currency.getCurrencyCode(), restCommand);
    }

    public static String getUrlForRestCommand(String currencyCode, RestCommand restCommand) throws Exception {
        StringBuilder url = new StringBuilder();

        if (StringUtils.isEmpty(currencyCode)
                || currencyCode.equalsIgnoreCase("BTC")
                || currencyMap.containsKey(Currency.getInstance(currencyCode))) {
            if (apiV1RestMap.containsKey(restCommand)) {
                if (readOnlyCommands.contains(restCommand)) {
                    url.append(MTGOX_HTTP_API_CACHE_URL);
                } else {
                    url.append(MTGOX_HTTP_API_URL);
                }
                url.append(MTGOX_HTTP_API_VERSION_1);
                Currency currency = null;
                try {
                    currency = Currency.getInstance(currencyCode);
                } catch (IllegalArgumentException ex) {
                    // Sigh.
                }
                if (StringUtils.isEmpty(currencyCode)
                        || currencyCode.equalsIgnoreCase("BTC")
                        || !currencyMap.containsKey(currency)) {
                    url.append("generic/");
                } else {
                    url.append(currencyMap.get(Currency.getInstance(currencyCode)));
                }
                url.append(apiV1RestMap.get(restCommand));
            } else if (apiV0RestMap.containsKey(restCommand)) {
                if (readOnlyCommands.contains(restCommand)) {
                    url.append(MTGOX_HTTP_API_CACHE_URL);
                } else {
                    url.append(MTGOX_HTTP_API_URL);
                }
                url.append(MTGOX_HTTP_API_VERSION_0);
                url.append(apiV0RestMap.get(restCommand));
            } else {
                throw new Exception("Unknown command: " + restCommand.toString());
            }
        } else {
            throw new Exception("Unknown currency: " + currencyCode);
        }
        return url.toString();
    }
}
