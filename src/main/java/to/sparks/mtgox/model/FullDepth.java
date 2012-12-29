package to.sparks.mtgox.model;

import java.util.Currency;
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

    /*
     * This is a bit of a kludge that ensures the offers know what currency they
     * are in.
     */
    public void setCurrency(Currency currency) {
        for (Offer ask : asks) {
            ask.setCurrency(currency);
        }
        for (Offer bid : bids) {
            bid.setCurrency(currency);
        }
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
