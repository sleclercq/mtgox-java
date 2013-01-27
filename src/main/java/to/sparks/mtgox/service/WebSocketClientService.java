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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jwebsocket.client.java.BaseWebSocket;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import to.sparks.mtgox.WebSocketClient;
import to.sparks.mtgox.model.Depth;
import to.sparks.mtgox.model.Ticker;
import to.sparks.mtgox.model.Trade;
import to.sparks.mtgox.net.EventListener;
import to.sparks.mtgox.net.SocketListener;

/**
 *
 * @author SparksG
 */
class WebSocketClientService implements EventListener, Runnable, WebSocketClient, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher = null;
    private Logger logger;
    final BaseWebSocket websocket = new BaseWebSocket();
    private ThreadPoolTaskExecutor taskExecutor;

    public WebSocketClientService(Logger logger, ThreadPoolTaskExecutor taskExecutor) {
        this.logger = logger;
        this.taskExecutor = taskExecutor;
    }

    public void init() {
        taskExecutor.execute(this);
    }

    public void destroy() {
        try {
            websocket.close();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void shutdown() {
        taskExecutor.shutdown();
    }

    @Override
    public void tradeEvent(Trade trade) {
        if (applicationEventPublisher != null) {
            StreamEvent<Trade> event = new StreamEvent<>(this, StreamEvent.EventType.Trade, trade);
            applicationEventPublisher.publishEvent(event);
        }
    }

    @Override
    public void tickerEvent(Ticker ticker) {
        if (applicationEventPublisher != null) {
            StreamEvent<Ticker> event = new StreamEvent<>(this, StreamEvent.EventType.Ticker, ticker);
            applicationEventPublisher.publishEvent(event);
        }
    }

    public void depthEvent(Depth depth) {
        if (applicationEventPublisher != null) {
            StreamEvent<Depth> event = new StreamEvent<>(this, StreamEvent.EventType.Depth, depth);
            applicationEventPublisher.publishEvent(event);
        }
    }

    @Override
    public void run() {
        try {

            websocket.addListener(new SocketListener(logger, this));
            websocket.open("ws://websocket.mtgox.com/mtgox");
            logger.info("WS API started.");

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
