package to.sparks.mtgox.util;

import to.sparks.mtgox.dto.Depth;
import to.sparks.mtgox.dto.Ticker;

/**
 *
 * @author SparksG
 */
public interface MtGoxListener {

    void depthEvent(Depth depth);

    void tickerEvent(Ticker ticker);
}
