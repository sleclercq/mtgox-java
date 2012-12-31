package to.sparks.mtgox.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class Offer extends DtoBase implements CurrencyKludge {

    private CurrencyInfo currencyInfo;
    private long price_int;
    private MtGoxBitcoinUnit amount;
    private long stamp;

    public Offer(long price_int,
            MtGoxBitcoinUnit amount,
            long stamp, CurrencyInfo currencyInfo) {
        this.price_int = price_int;
        this.amount = amount;
        this.stamp = stamp;
        this.currencyInfo = currencyInfo;
    }

    public Offer(@JsonProperty("price") double price,
            @JsonProperty("amount") double amount,
            @JsonProperty("price_int") long price_int,
            @JsonProperty("amount_int") long amount_int,
            @JsonProperty("stamp") long stamp) {
        this(price_int, new MtGoxBitcoinUnit(amount_int), stamp, null);
    }

    /**
     * @return the currency
     */
    public CurrencyInfo getCurrencyInfo() {
        return currencyInfo;
    }

    @Override
    public void setCurrencyInfo(CurrencyInfo currencyInfo) {
        this.currencyInfo = currencyInfo;
    }

    public MtGoxFiatUnit getPrice() {
        MtGoxFiatUnit price = null;
        if (currencyInfo != null) {
            price = new MtGoxFiatUnit(price_int, currencyInfo);
        } else {
            throw new RuntimeException("Error: getPrice called before currency was initialised.");
        }
        return price;
    }

    /**
     * @return the amount_int
     */
    public MtGoxBitcoinUnit getAmount() {
        return amount;
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
    public void setAmount(MtGoxBitcoinUnit amount) {
        this.amount = amount;
    }

    /**
     * @param stamp the stamp to set
     */
    public void setStamp(long stamp) {
        this.stamp = stamp;
    }
}
