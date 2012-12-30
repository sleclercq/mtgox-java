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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.kit.RawPacket;
import to.sparks.mtgox.model.*;

/**
 *
 * @author SparksG
 */
public class MtGoxSocketListener implements WebSocketClientListener {

    private MtGoxEventListener eventListener;
    private Logger logger;

    public MtGoxSocketListener(Logger logger, MtGoxEventListener eventListener) {
        this.eventListener = eventListener;
        this.logger = logger;
    }

    @Override
    public void processOpened(WebSocketClientEvent aEvent) {
        // The websocket has been opened
    }

    @Override
    public void processPacket(WebSocketClientEvent aEvent, WebSocketPacket aPacket) {
        if (aEvent != null) {
            if (aPacket != null && aPacket.getFrameType() == RawPacket.FRAMETYPE_UTF8) {
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
                            eventListener.tickerEvent(ticker);
                            logger.log(Level.FINE, "Ticker: last: {0}", new Object[]{ticker.getLast().getDisplay()});
                        } else if (messageType.equalsIgnoreCase("depth")) {
                            OpPrivateDepth opPrivateDepth = mapper.readValue(factory.createJsonParser(aPacket.getUTF8()), OpPrivateDepth.class);
                            Depth depth = opPrivateDepth.getDepth();
                            eventListener.depthEvent(depth);
                            logger.log(Level.FINE, "Depth total volume: {0}", new Object[]{depth.getTotalVolume().getCredits()});
                        } else if (messageType.equalsIgnoreCase("trade")) {
                            OpPrivateTrade opPrivateTrade = mapper.readValue(factory.createJsonParser(aPacket.getUTF8()), OpPrivateTrade.class);
                            Trade trade = opPrivateTrade.getTrade();
                            eventListener.tradeEvent(trade);
                            logger.log(Level.FINE, "Trade price: {0}", new Object[]{trade.getPrice().getCredits()});
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

    @Override
    public void processClosed(WebSocketClientEvent aEvent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
