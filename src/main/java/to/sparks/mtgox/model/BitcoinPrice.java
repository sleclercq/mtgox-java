package to.sparks.mtgox.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class BitcoinPrice extends MtGoxPrice {

    public BitcoinPrice(@JsonProperty("value") double value,
            @JsonProperty("value_int") long value_int,
            @JsonProperty("display") String display,
            @JsonProperty("display_short") String display_short,
            @JsonProperty("currency") String currency) {
        super(value, value_int, display, display_short, currency);
    }

    /**
     * This function must be called *after* you have setCurrencyInfo on this
     * object.
     *
     * @return the value_int
     */
    public MtGoxBitcoin getPriceValue() {
        return new MtGoxBitcoin(this.getPriceValueInt());
    }
}
