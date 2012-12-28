package to.sparks.mtgox.net;

import to.sparks.mtgox.model.Depth;
import to.sparks.mtgox.model.Ticker;
import to.sparks.mtgox.model.Trade;

/**
 *
 * @author SparksG
 */
public interface MtGoxEventListener {

    void depthEvent(Depth depth);

    void tickerEvent(Ticker ticker);

    void tradeEvent(Trade trade);
}
