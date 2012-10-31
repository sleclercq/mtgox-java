package to.sparks.mtgox.dto;

import org.codehaus.jackson.annotate.JsonProperty;
import to.sparks.mtgox.dto.Ticker;

/**
 *
 * @author SparksG
 */
public class OpPrivateTicker extends OpPrivate<Ticker> {

    private Ticker ticker;

    public OpPrivateTicker(@JsonProperty("op") String op,
            @JsonProperty("channel") String channel,
            @JsonProperty("private") String messageType,
            @JsonProperty("ticker") Ticker ticker,
            @JsonProperty("origin") String origin) {
        super(op, channel, messageType, origin);
        this.ticker = ticker;
    }

    /**
     * @return the ticker
     */
    public Ticker getTicker() {
        return ticker;
    }
}
