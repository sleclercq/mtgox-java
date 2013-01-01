package to.sparks.mtgox.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class Offer extends DtoBase implements CurrencyKludge {

    protected CurrencyInfo currencyInfo;
    private long price_int;
    private long stamp;
    private long amount_int;

    public Offer(@JsonProperty("price") double price,
            @JsonProperty("amount") double amount,
            @JsonProperty("price_int") long price_int,
            @JsonProperty("amount_int") long amount_int,
            @JsonProperty("stamp") long stamp) {
        this.price_int = price_int;
        this.amount_int = amount_int;
        this.stamp = stamp;
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

    public MtGoxUnitOfCredit getPrice() {
        MtGoxUnitOfCredit price = null;
        if (currencyInfo != null) {
            price = new MtGoxUnitOfCredit(price_int, currencyInfo);
        } else {
            throw new RuntimeException("Error: getPrice called before currency was initialised.");
        }
        return price;
    }

    /**
     * @return the amount_int
     */
    public MtGoxUnitOfCredit getAmount() {
        MtGoxUnitOfCredit amount = new MtGoxUnitOfCredit(amount_int, MtGoxUnitOfCredit.BitcoinCurrencyInfo);
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
    public void setAmount(MtGoxUnitOfCredit amount) {
        this.amount_int = amount.getCredits().longValueExact();
    }

    /**
     * @param stamp the stamp to set
     */
    public void setStamp(long stamp) {
        this.stamp = stamp;
    }
}
