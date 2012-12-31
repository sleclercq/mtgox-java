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
    MtGoxHTTPClient httpAPI;
    CurrencyInfo currencyInfo;

    public MtGoxServiceImpl(final Logger logger, MtGoxHTTPClient httpAPI, MtGoxWebSocketClient mtGoxWebSocketApi, Currency currency) throws IOException, Exception {
        this.logger = logger;
        this.httpAPI = httpAPI;
        this.wsApi = mtGoxWebSocketApi;
        this.currencyInfo = httpAPI.getCurrencyInfo(currency);
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
        FullDepth fullDepth = httpAPI.getFullDepth(currencyInfo.getCurrency());
        currencyKludge(fullDepth);
        return fullDepth;
    }

    @Override
    public String placeOrder(OrderType orderType, MtGoxUnitOfCredit price, MtGoxUnitOfCredit volume) throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {

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

        return httpAPI.placeOrder(currencyInfo.getCurrency(), params);
    }

    @Override
    public String placeMarketOrder(OrderType orderType, MtGoxUnitOfCredit volume) throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
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

        OrderResult orderResult = httpAPI.getPrivateOrderResult(params);
        currencyKludge(orderResult);

        return orderResult;
    }

    @Override
    public Order[] getOpenOrders() throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        Order[] orders = httpAPI.getOpenOrders();
        for (Order order : orders) {
            currencyKludge(order);
        }
        return orders;
    }

    @Override
    public Ticker getTicker() throws IOException, Exception {
        Ticker ticker = httpAPI.getTicker(currencyInfo.getCurrency());
        currencyKludge(ticker);
        return ticker;
    }

    @Override
    public Currency getBaseCurrency() {
        return currencyInfo.getCurrency();
    }

    @Override
    public AccountInfo getAccountInfo() throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        AccountInfo info = httpAPI.getPrivateInfo();
        currencyKludge(info);
        return info;
    }

    @Override
    public CurrencyInfo getCurrencyInfo(Currency currency) throws IOException, Exception {
        return httpAPI.getCurrencyInfo(currency);
    }

    @Override
    public CurrencyInfo getCurrencyInfo(String currencyCode) throws Exception {
        return httpAPI.getCurrencyInfo(currencyCode);
    }

    private void currencyKludge(DtoBase o) {
        if (o != null && o instanceof CurrencyKludge) {
            ((CurrencyKludge) o).setCurrencyInfo(currencyInfo);
        }
    }
}