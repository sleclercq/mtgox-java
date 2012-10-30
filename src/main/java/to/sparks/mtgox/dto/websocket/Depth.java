package to.sparks.dto.websocket;

import org.codehaus.jackson.annotate.JsonProperty;
import to.sparks.dto.Offer;

/**
 *
 * @author SparksG
 */
public class Depth extends Offer {

    private String currency;
    private String item;
    private long total_volume_int;
    private String type_str;
    private int type;

    public Depth(@JsonProperty("currency") String currency,
            @JsonProperty("item") String item,
            @JsonProperty("total_volume_int") long total_volume_int,
            @JsonProperty("type_str") String type_str,
            @JsonProperty("type") int type,
            @JsonProperty("price") double price,
            @JsonProperty("volume") double amount,
            @JsonProperty("price_int") long price_int,
            @JsonProperty("volume_int") long amount_int,
            @JsonProperty("now") long stamp) {
        super(price, amount, price_int, amount_int, stamp);
        this.currency = currency;
        this.item = item;
        this.total_volume_int = total_volume_int;
        this.type_str = type_str;
        this.type = type;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return the item
     */
    public String getItem() {
        return item;
    }

    /**
     * @return the total_volume_int
     */
    public long getTotal_volume_int() {
        return total_volume_int;
    }

    /**
     * @return the type_str
     */
    public String getType_str() {
        return type_str;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }
}
