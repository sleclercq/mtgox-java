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
public class MtGoxServiceImpl implements MtGoxAPI {

    // TODO:  This value is currency dependent.  JPY is different from USD for example.
    public static double USD_INT_MULTIPLIER = 100000000.0D;
    public static double AUD_INT_MULTIPLIER = 100000.0D;
    public static double BTC_VOL_INT_MULTIPLIER = 100000000.0D;
    private static Logger logger;
    private static MtGoxWebSocketClient wsApi;
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

    private static int convertVolumeBTCtoInt(double d) {
        double total = d * BTC_VOL_INT_MULTIPLIER;
        return (int) total;
    }

    private static int convertPricetoInt(String currencyCode, double d) {
        double multiplier;
        // TODO:  Verify what the correct multiplier is for each currency
        switch (currencyCode.toLowerCase()) {
            case "aud":
                multiplier = AUD_INT_MULTIPLIER;
                break;
            default:
                multiplier = USD_INT_MULTIPLIER;
                break;
        }
        double total = d * multiplier;
        return (int) total;
    }

    @Override
    public String placeOrder(OrderType orderType, Double price, double volume) throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        HashMap<String, String> params = new HashMap<>();
        if (orderType == OrderType.Bid) {
            params.put("type", "bid");
        } else {
            params.put("type", "ask");
        }
        if (price != null) {
            params.put("price_int", String.valueOf(convertPricetoInt(currency.getCurrencyCode(), price)));
        }
        params.put("amount_int", String.valueOf(convertVolumeBTCtoInt(volume)));

        return httpAPI.placeOrder(currency, params);
    }

    @Override
    public String placeMarketOrder(OrderType orderType, double volume) throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
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
}