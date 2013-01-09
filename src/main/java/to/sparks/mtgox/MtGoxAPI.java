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
package to.sparks.mtgox;

import java.util.Currency;
import java.util.List;
import to.sparks.mtgox.model.*;

/**
 * A java api for the MtGox bitcoin exchange built using Spring and Maven. See
 * https://en.bitcoin.it/wiki/MtGox/API for protocol details.
 *
 * @author SparksG
 */
public interface MtGoxAPI {

    public enum OrderType {

        Bid, Ask
    }

    /**
     *
     * @return The java currency used by this api connection.
     */
    Currency getBaseCurrency();

    /**
     * Get the market full depth from the MtGox exchange
     *
     * @return An object containing the full market depth information
     * @throws Exception
     */
    FullDepth getFullDepth() throws Exception;

    /**
     * A private function that requires credentials. Get all open orders.
     *
     * @return An array of open orders
     * @throws Exception
     */
    Order[] getOpenOrders() throws Exception;

    /**
     * Return the result of an order request
     *
     * @param orderType Bid or Ask
     * @param orderRef The reference string that was returned when the order was
     * pleaced
     * @return
     * @throws Exception
     */
    OrderResult getOrderResult(OrderType orderType, String orderRef) throws Exception;

    /**
     * Get all depth events since a particular time (or whenever this api object
     * was created)
     *
     * @param timestamp The java timestamp to return events since.
     * @return
     */
    List<Depth> getAllDepthSince(long timestamp);

    /**
     * Get all trade events since a particular time (or whenever this api object
     * was created)
     *
     * @param timestamp The java timestamp to return trades since.
     * @return
     */
    List<Trade> getAllTradesSince(long timestamp);

    /**
     * Get all depth events since this api object was created
     */
    List<Depth> getDepthHistory();

    /**
     * Get all ticker events since this api object was created
     */
    List<Ticker> getTickerHistory();

    /**
     * Get all trade events since this api object was created
     */
    List<Trade> getTradeHistory();

    /**
     * Request the current ticker from MtGox
     *
     * @return
     * @throws Exception
     */
    Ticker getTicker() throws Exception;

    /**
     * Place a bitcoin order at market price.
     *
     * @param orderType Bid or Ask
     * @param volume The volume of bitcoins
     * @return Order reference number
     * @throws Exception
     */
    String placeMarketOrder(OrderType orderType, MtGoxUnitOfCredit volume) throws Exception;

    /**
     * Place a bitcoin order at a particular price
     *
     * @param orderType Bid or Ask
     * @param price The price in the base currency of this api instance
     * @param volume The volume of bitcoins
     * @return Order reference number
     * @throws Exception
     */
    String placeOrder(OrderType orderType, MtGoxUnitOfCredit price, MtGoxUnitOfCredit volume) throws Exception;

    /**
     * Get private information about the currently logged-in account
     *
     * @return
     * @throws Exception
     */
    AccountInfo getAccountInfo() throws Exception;

    /**
     * Get MtGox special information about a currency.
     *
     * @param currency The java currency
     * @return
     * @throws Exception
     */
    CurrencyInfo getCurrencyInfo(Currency currency) throws Exception;

    /**
     * Get MtGox special information about a currency.
     *
     * @param currencyCode The currency code
     * @return
     * @throws Exception
     */
    CurrencyInfo getCurrencyInfo(String currencyCode) throws Exception;

    /**
     * Cancel an existing order. Currently this is only available on the HTTP
     * Version 0 API, so the JSON result should be discarded.
     *
     * @param orderType Bid or Ask
     * @param orderRef The reference string that was returned when the order was
     * placed
     * @return The JSON response. Avoid using this response because this api
     * call uses the old Version 0 MtGox API.
     * @throws Exception
     */
    OrderCancelResult cancelOrder(OrderType orderType, String orderRef) throws Exception;
}
