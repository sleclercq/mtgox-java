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

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import to.sparks.mtgox.HTTPClientV0;
import to.sparks.mtgox.HTTPClientV1;
import to.sparks.mtgox.MtGoxAPI;
import to.sparks.mtgox.WebSocketClient;
import to.sparks.mtgox.model.*;

/**
 * All MtGox API interactions (both HTTP/REST and Websocket) are handled by this
 * API class.
 *
 * @author SparksG
 */
class APIService implements MtGoxAPI {

    private static Logger logger;
    private WebSocketClient wsApi;
    HTTPClientV0 httpAPIV0;
    HTTPClientV1 httpAPIV1;
    CurrencyInfo currencyInfo;

    public APIService(final Logger logger, HTTPClientV0 httpAPIV0, HTTPClientV1 httpAPIV1, WebSocketClient mtGoxWebSocketApi, Currency currency) throws IOException, Exception {
        this.logger = logger;
        this.httpAPIV0 = httpAPIV0;
        this.httpAPIV1 = httpAPIV1;
        this.wsApi = mtGoxWebSocketApi;
        this.currencyInfo = httpAPIV1.getCurrencyInfo(currency);
    }

    @Override
    public void shutdown() {
        wsApi.shutdown();
    }

    @Override
    public List<Depth> getAllDepthSince(long timestamp) {
        List<Depth> depths = wsApi.getAllDepthSince(timestamp);
        for (Depth depth : depths) {
            currencyKludge(depth);
        }
        return depths;
    }

    @Override
    public List<Trade> getAllTradesSince(long timestamp) {
        List<Trade> trades = wsApi.getAllTradesSince(timestamp);
        for (Trade trade : trades) {
            currencyKludge(trade);
        }
        return trades;
    }

    @Override
    public List<Depth> getDepthHistory() {
        List<Depth> depthHistory = wsApi.getDepthHistory();
        for (Depth depth : depthHistory) {
            currencyKludge(depth);
        }
        return depthHistory;
    }

    @Override
    public List<Ticker> getTickerHistory() {
        List<Ticker> tickerHistory = wsApi.getTickerHistory();
        for (Ticker ticker : tickerHistory) {
            currencyKludge(ticker);
        }
        return tickerHistory;


    }

    @Override
    public List<Trade> getTradeHistory() {
        List<Trade> tradeHistory = wsApi.getTradeHistory();
        for (Trade trade : tradeHistory) {
            currencyKludge(trade);
        }
        return tradeHistory;
    }

    @Override
    public FullDepth getFullDepth() throws IOException, Exception {
        FullDepth fullDepth = httpAPIV1.getFullDepth(currencyInfo.getCurrency());
        currencyKludge(fullDepth);
        return fullDepth;
    }

    @Override
    public String placeOrder(OrderType orderType, MtGoxFiatCurrency price, MtGoxBitcoin volume) throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {

        HashMap<String, String> params = new HashMap<>();
        if (orderType == OrderType.Bid) {
            params.put("type", "bid");
        } else {
            params.put("type", "ask");
        }

        if (price != null) {
            params.put("price_int", String.valueOf(price.getCredits().unscaledValue().longValue()));
        }
        params.put("amount_int", String.valueOf(volume.getCredits().unscaledValue().longValue()));

        return httpAPIV1.placeOrder(currencyInfo.getCurrency(), params);
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

        OrderResult orderResult = httpAPIV1.getPrivateOrderResult(params);
        currencyKludge(orderResult);

        return orderResult;
    }

    @Override
    public Order[] getOpenOrders() throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        Order[] orders = httpAPIV1.getOpenOrders();
        for (Order order : orders) {
            currencyKludge(order);
        }
        return orders;
    }

    @Override
    public Ticker getTicker() throws IOException, Exception {
        Ticker ticker = httpAPIV1.getTicker(currencyInfo.getCurrency());
        currencyKludge(ticker);
        return ticker;
    }

    @Override
    public Currency getBaseCurrency() {
        return currencyInfo.getCurrency();
    }

    @Override
    public AccountInfo getAccountInfo() throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        AccountInfo info = httpAPIV1.getPrivateInfo();
        currencyKludge(info);
        return info;
    }

    @Override
    public CurrencyInfo getCurrencyInfo(Currency currency) throws IOException, Exception {
        return httpAPIV1.getCurrencyInfo(currency);
    }

    @Override
    public CurrencyInfo getCurrencyInfo(String currencyCode) throws Exception {
        return httpAPIV1.getCurrencyInfo(currencyCode);
    }

    private void currencyKludge(DtoBase o) {
        if (o != null && o instanceof CurrencyKludge) {
            ((CurrencyKludge) o).setCurrencyInfo(currencyInfo);
        }
    }

    @Override
    public OrderCancelResult cancelOrder(OrderType orderType, String orderRef) throws Exception {
        HashMap<String, String> params = new HashMap<>();
        if (orderType == MtGoxAPI.OrderType.Bid) {
            params.put("type", "2");
        } else {
            params.put("type", "1");
        }
        params.put("oid", orderRef);
        return httpAPIV0.cancelOrder(params);
    }

    @Override
    public OrderCancelResult cancelOrder(Order order) throws Exception {
        return cancelOrder(order.getType(), order.getOid());
    }
}