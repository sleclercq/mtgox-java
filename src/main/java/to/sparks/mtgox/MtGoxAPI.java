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
 * A java api for the MtGox bitcoin exchange built using Spring and Maven
 *
 * @author SparksG
 */
public interface MtGoxAPI {

    public enum OrderType {

        Bid, Ask
    }
    
    Currency getBaseCurrency();

    FullDepth getFullDepth() throws Exception;

    Order[] getOpenOrders() throws Exception;

    OrderResult getOrderResult(OrderType orderType, String orderRef) throws Exception;

    List<Depth> getAllDepthSince(long timestamp);

    List<Trade> getAllTradesSince(long timestamp);

    List<Depth> getDepthHistory();

    List<Ticker> getTickerHistory();

    List<Trade> getTradeHistory();

    Ticker getTicker() throws Exception;

    String placeMarketOrder(OrderType orderType, MtGoxBitcoinUnit volume) throws Exception;

    String placeOrder(OrderType orderType, MtGoxFiatUnit price, MtGoxBitcoinUnit volume) throws Exception;
    
    Info getInfo() throws Exception;
}
