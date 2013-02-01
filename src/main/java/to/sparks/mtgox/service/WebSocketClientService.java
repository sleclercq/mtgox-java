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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.client.java.BaseWebSocket;
import org.jwebsocket.kit.RawPacket;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import to.sparks.mtgox.MtGoxWebsocketClient;
import to.sparks.mtgox.StreamEvent;
import to.sparks.mtgox.model.*;
import to.sparks.mtgox.net.MtGoxPacket;
import to.sparks.mtgox.net.SocketListener;

/**
 *
 * @author SparksG
 */
class WebsocketClientService implements Runnable, MtGoxWebsocketClient, ApplicationEventPublisherAware, ApplicationListener<StreamEvent> {

    private ApplicationEventPublisher applicationEventPublisher = null;
    private Logger logger;
    final BaseWebSocket websocket = new BaseWebSocket();
    private ThreadPoolTaskExecutor taskExecutor;
    private Map<String, CurrencyInfo> currencyCache;
    private HTTPClientV1Service httpAPIV1;
    private SocketListener socketListener;

    public WebsocketClientService(Logger logger, ThreadPoolTaskExecutor taskExecutor, HTTPClientV1Service httpAPIV1, SocketListener socketListener) {
        this.logger = logger;
        this.taskExecutor = taskExecutor;
        this.httpAPIV1 = httpAPIV1;
        currencyCache = new HashMap<>();
        currencyCache.put("BTC", CurrencyInfo.BitcoinCurrencyInfo);
        this.socketListener = socketListener;
    }

    public void init() {
        taskExecutor.execute(this);
    }

    public void destroy() {
        try {
            if (websocket != null) {
                websocket.close();
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void shutdown() {
        taskExecutor.shutdown();
    }

    public void packetEvent(MtGoxPacket packet) {
        WebSocketClientEvent aEvent = packet.getaEvent();
        WebSocketPacket aPacket = packet.getaPacket();

        if (aEvent != null) {
            if (aPacket != null && aPacket.getFrameType() == RawPacket.FRAMETYPE_UTF8) {  // RawPacket.FRAMETYPE_UTF8  or  WebSocketFrameType.TEXT
                try {
                    // logger.fine(aPacket.getUTF8());

                    JsonFactory factory = new JsonFactory();
                    ObjectMapper mapper = new ObjectMapper();

                    JsonParser jp = factory.createJsonParser(aPacket.getUTF8());
                    DynaBean op = mapper.readValue(jp, DynaBean.class);

                    if (op.get("op") != null && op.get("op").equals("private")) {
                        String messageType = op.get("private").toString();
                        if (messageType.equalsIgnoreCase("ticker")) {
                            OpPrivateTicker opPrivateTicker = mapper.readValue(factory.createJsonParser(aPacket.getUTF8()), OpPrivateTicker.class);
                            Ticker ticker = opPrivateTicker.getTicker();
                            tickerEvent(ticker);
                            logger.log(Level.FINE, "Ticker: last: {0}", new Object[]{ticker.getLast().toPlainString()});
                        } else if (messageType.equalsIgnoreCase("depth")) {
                            OpPrivateDepth opPrivateDepth = mapper.readValue(factory.createJsonParser(aPacket.getUTF8()), OpPrivateDepth.class);
                            Depth depth = opPrivateDepth.getDepth();
                            depthEvent(depth);
                            logger.log(Level.FINE, "Depth total volume: {0}", new Object[]{depth.getTotalVolume().toPlainString()});
                        } else if (messageType.equalsIgnoreCase("trade")) {
                            OpPrivateTrade opPrivateTrade = mapper.readValue(factory.createJsonParser(aPacket.getUTF8()), OpPrivateTrade.class);
                            Trade trade = opPrivateTrade.getTrade();
                            tradeEvent(trade);
                            logger.log(Level.FINE, "Trade currency: {0}", new Object[]{trade.getPrice_currency()});
                        } else {
                            logger.log(Level.WARNING, "Unknown private operation: {0}", new Object[]{aPacket.getUTF8()});
                        }

                        // logger.log(Level.INFO, "messageType: {0}, payload: {1}", new Object[]{messageType, dataPayload});
                    } else {
                        logger.log(Level.WARNING, "Unknown operation: {0}, payload: {1}", new Object[]{op.get("op")});
                        // TODO:  Process the following types
                        // subscribe
                        // unsubscribe
                        // remark
                        // result
                    }
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public void tradeEvent(Trade trade) {
        if (applicationEventPublisher != null) {
            CurrencyInfo ci = null;
            if (!currencyCache.containsKey(trade.getPrice_currency())) {
                try {
                    ci = httpAPIV1.getCurrencyInfo(trade.getPrice_currency());
                    currencyCache.put(trade.getPrice_currency(), ci);
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            ci = currencyCache.get(trade.getPrice_currency());
            if (ci != null) {
                trade.setCurrencyInfo(ci);
            }
            StreamEvent event = new StreamEvent(this, StreamEvent.EventType.Trade, trade);
            applicationEventPublisher.publishEvent(event);
        }
    }

    public void tickerEvent(Ticker ticker) {
        if (applicationEventPublisher != null) {
            StreamEvent event = new StreamEvent(this, StreamEvent.EventType.Ticker, ticker);
            applicationEventPublisher.publishEvent(event);
        }
    }

    public void depthEvent(Depth depth) {
        if (applicationEventPublisher != null) {
            StreamEvent event = new StreamEvent(this, StreamEvent.EventType.Depth, depth);
            applicationEventPublisher.publishEvent(event);
        }
    }

    @Override
    public void run() {
        try {

            websocket.addListener(socketListener);
            websocket.open("ws://websocket.mtgox.com/mtgox");
            logger.info("WebSocket API Client started.");

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onApplicationEvent(StreamEvent event) {
        switch (event.getEventType()) {
            case Packet:
                MtGoxPacket packet = (MtGoxPacket) event.getPayload();
                packetEvent(packet);
                break;
        }
    }
}
