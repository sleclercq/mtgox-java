package to.sparks.mtgox.net;

import to.sparks.mtgox.model.Depth;
import to.sparks.mtgox.model.Ticker;

/**
 *
 * @author SparksG
 */
public interface MtGoxListener {

    void depthEvent(Depth depth);

    void tickerEvent(Ticker ticker);
}
