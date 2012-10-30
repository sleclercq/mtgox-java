package to.sparks.dto;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author SparksG
 */
public class FullDepth {

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
     * @param aAsks the asks to set
     */
    public void setAsks(Offer[] aAsks) {
        asks = aAsks;
    }

    /**
     * @return the bids
     */
    public Offer[] getBids() {
        return bids;
    }

    /**
     * @param aBids the bids to set
     */
    public void setBids(Offer[] aBids) {
        bids = aBids;
    }
}
