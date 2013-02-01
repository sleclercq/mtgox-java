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
package to.sparks.mtgox.net;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientListener;
import org.jwebsocket.api.WebSocketPacket;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import to.sparks.mtgox.StreamEvent;

/**
 *
 * @author SparksG
 */
public class SocketListener implements WebSocketClientListener, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher = null;
    private Logger logger;

    public SocketListener(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void processOpened(WebSocketClientEvent aEvent) {
        // The websocket has been opened
    }

    @Override
    public void processPacket(WebSocketClientEvent aEvent, WebSocketPacket aPacket) {
        String sEvent = aEvent != null ? aEvent.toString() : "null";
        String sPacket = aPacket != null ? aPacket.getUTF8() : "null";
        logger.log(Level.INFO, "Event: {0}  Packet: {1}", new Object[]{sEvent, sPacket});
        MtGoxPacket packet = new MtGoxPacket(aEvent, aPacket);
        StreamEvent event = new StreamEvent(this, StreamEvent.EventType.Packet, packet);
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void processClosed(WebSocketClientEvent aEvent) {
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
