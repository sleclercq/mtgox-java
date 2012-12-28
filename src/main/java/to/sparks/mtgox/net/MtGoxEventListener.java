package to.sparks.mtgox.net;

import to.sparks.mtgox.model.Depth;
import to.sparks.mtgox.model.Ticker;
import to.sparks.mtgox.model.Trade;

/**
 * An implementing class will be called on these methods when the corresponding packet arrives from the WebSocket.
 * 
 * @author SparksG
 */
public interface MtGoxEventListener {

    void depthEvent(Depth depth);

    void tickerEvent(Ticker ticker);

    void tradeEvent(Trade trade);
}
