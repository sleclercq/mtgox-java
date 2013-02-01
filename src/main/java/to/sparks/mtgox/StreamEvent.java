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

import org.springframework.context.ApplicationEvent;

/**
 *
 * @author SparksG
 */
public class StreamEvent extends ApplicationEvent {

    private static final long serialVersionUID = 3487524279263502L;

    public enum EventType {

        Trade,
        Ticker,
        Depth,
        Packet
    }
    private EventType eventType;
    private Object payload;

    public StreamEvent(Object source, EventType eventType, Object payload) {
        super(source);
        this.eventType = eventType;
        this.payload = payload;

    }

    public Object getPayload() {
        return payload;
    }

    public EventType getEventType() {
        return eventType;
    }
}
