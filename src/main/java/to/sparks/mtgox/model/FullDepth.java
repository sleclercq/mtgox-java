package to.sparks.mtgox.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author SparksG
 */
@JsonAutoDetect
public class FullDepth extends DtoBase {

    private Offer[] asks;
    private Offer[] bids;

    @JsonCreator
    public FullDepth(@JsonProperty("asks") Offer[] asks,
            @JsonProperty("bids") Offer[] bids) {
        this.asks = asks;
        this.bids = bids;
    }

    /**
     * @return the asks
     */
    public Offer[] getAsks() {
        return asks;
    }

    /**
     * @return the bids
     */
    public Offer[] getBids() {
        return bids;
    }
}
