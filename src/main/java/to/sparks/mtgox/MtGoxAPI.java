package to.sparks.mtgox;

import java.util.List;
import to.sparks.mtgox.dto.*;

/**
 * A java api for the MtGox bitcoin exchange
 *
 * @author SparksG
 */
public interface MtGoxAPI {

    public enum OrderType {

        Bid, Ask
    }

    FullDepth getFullDepth() throws Exception;

    Order[] getOpenOrders() throws Exception;

    OrderResult getOrderResult(OrderType orderType, String orderRef) throws Exception;

    List<Offer> getRealtimeAsks();

    List<Offer> getRealtimeBids();

    Ticker getTicker() throws Exception;

    String placeMarketOrder(OrderType orderType, double volume) throws Exception;

    String placeOrder(OrderType orderType, Double price, double volume) throws Exception;
}
