package to.sparks.mtgox.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class Offer extends DtoBase {

    private double price;
    private double amount;
    private long price_int;
    private long amount_int;
    private long stamp;

    public Offer(@JsonProperty("price") double price,
            @JsonProperty("amount") double amount,
            @JsonProperty("price_int") long price_int,
            @JsonProperty("amount_int") long amount_int,
            @JsonProperty("stamp") long stamp) {
        this.price = price;
        this.amount = amount;
        this.price_int = price_int;
        this.amount_int = amount_int;
        this.stamp = stamp;
    }

    /**
     * @return the price
     */
    @Deprecated
    public double getPrice() {
        return price;
    }

    /**
     * @return the amount
     */
    @Deprecated
    public double getAmount() {
        return amount;
    }

    /**
     * @return the price_int
     */
    public long getPrice_int() {
        return price_int;
    }

    /**
     * @return the amount_int
     */
    public long getAmount_int() {
        return amount_int;
    }

    /**
     * @return the stamp
     */
    public long getStamp() {
        return stamp;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @param amount_int the amount_int to set
     */
    public void setAmount_int(long amount_int) {
        this.amount_int = amount_int;
    }

    /**
     * @param stamp the stamp to set
     */
    public void setStamp(long stamp) {
        this.stamp = stamp;
    }
}
