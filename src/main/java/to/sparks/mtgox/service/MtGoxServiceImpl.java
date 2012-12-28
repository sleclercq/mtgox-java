package to.sparks.mtgox.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import to.sparks.mtgox.MtGoxAPI;
import to.sparks.mtgox.model.*;

/**
 * All MtGox API interactions (both HTTP/REST and Websocket) are handled by this
 * API class.
 *
 * @author SparksG
 */
class MtGoxServiceImpl implements MtGoxAPI {

    private static Logger logger;
    private MtGoxWebSocketClient wsApi;
    private Currency currency;
    MtGoxHTTPClient httpAPI;

    public MtGoxServiceImpl(final Logger logger, MtGoxHTTPClient httpAPI, MtGoxWebSocketClient mtGoxWebSocketApi, Currency currency) {
        this.logger = logger;
        this.currency = currency;
        this.httpAPI = httpAPI;
        this.wsApi = mtGoxWebSocketApi;
    }

    @Override
    public List<Depth> getAllDepthSince(long timestamp) {
        return wsApi != null ? wsApi.getAllDepthSince(timestamp) : null;
    }

    @Override
    public List<Trade> getAllTradesSince(long timestamp) {
        return wsApi != null ? wsApi.getAllTradesSince(timestamp) : null;
    }

    @Override
    public List<Depth> getDepthHistory() {
        return wsApi != null ? wsApi.getDepthHistory() : null;
    }

    @Override
    public List<Ticker> getTickerHistory() {
        return wsApi != null ? wsApi.getTickerHistory() : null;
    }

    @Override
    public List<Trade> getTradeHistory() {
        return wsApi != null ? wsApi.getTradeHistory() : null;
    }

    @Override
    public FullDepth getFullDepth() throws IOException, Exception {
        return httpAPI.getFullDepth(currency);
    }

    @Override
    public String placeOrder(OrderType orderType, MtGoxCurrency price, MtGoxBitcoin volume) throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {

        HashMap<String, String> params = new HashMap<>();
        if (orderType == OrderType.Bid) {
            params.put("type", "bid");
        } else {
            params.put("type", "ask");
        }

        if (price != null) {
//            params.put("price_int", String.valueOf(convertPricetoInt(currency.getCurrencyCode(), price)));
            params.put("price_int", String.valueOf(price.getAmount().longValueExact()));
        }
        params.put("amount_int", String.valueOf(volume.getAmount().longValueExact()));

        return httpAPI.placeOrder(currency, params);
    }

    @Override
    public String placeMarketOrder(OrderType orderType, MtGoxBitcoin volume) throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        return placeOrder(orderType, null, volume);
    }

    @Override
    public OrderResult getOrderResult(OrderType orderType, String orderRef) throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        HashMap<String, String> params = new HashMap<>();
        if (orderType == OrderType.Bid) {
            params.put("type", "bid");
        } else {
            params.put("type", "ask");
        }
        params.put("order", orderRef);

        return httpAPI.getPrivateOrderResult(params);
    }

    @Override
    public Order[] getOpenOrders() throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        return httpAPI.getOpenOrders();
    }

    @Override
    public Ticker getTicker() throws IOException, Exception {
        return httpAPI.getTicker(currency);
    }

    @Override
    public Currency getBaseCurrency() {
        return currency;
    }
}