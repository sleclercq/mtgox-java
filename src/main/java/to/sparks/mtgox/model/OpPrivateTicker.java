package to.sparks.mtgox.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import to.sparks.mtgox.model.Ticker;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
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
