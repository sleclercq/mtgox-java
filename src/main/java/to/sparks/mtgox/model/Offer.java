package to.sparks.mtgox.model;

import java.util.Currency;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class Offer extends DtoBase {

    private Currency currency;
    private long price_int;
    private MtGoxBitcoinUnit amount;
    private long stamp;

    public Offer(long price_int,
            MtGoxBitcoinUnit amount,
            long stamp, Currency currency) {
        this.price_int = price_int;
        this.amount = amount;
        this.stamp = stamp;
        this.currency = currency;
    }

//    public Offer(MtGoxCurrency price,
//            MtGoxBitcoin amount,
//            long stamp) {
//        this(price.getCredits().longValueExact(), amount, stamp, price.getCurrency());
//    }
    
    public Offer(@JsonProperty("price") double price,
            @JsonProperty("amount") double amount,
            @JsonProperty("price_int") long price_int,
            @JsonProperty("amount_int") long amount_int,
            @JsonProperty("stamp") long stamp) {
        this(price_int, MtGoxBitcoinUnit.createBitcoinInstance(amount_int), stamp, null);
    }

    /**
     * @return the currency
     */
    public Currency getCurrency() {
        return currency;
    }

    void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * @return the price_int
     */
    long getPrice_int() {
        return price_int;
    }

    public MtGoxFiatUnit getPrice() {
        MtGoxFiatUnit price = null;
        if (currency != null) {
            price = MtGoxFiatUnit.createCurrencyInstance(price_int, currency);
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
