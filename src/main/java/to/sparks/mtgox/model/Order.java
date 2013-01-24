package to.sparks.mtgox.model;

import java.util.Currency;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import to.sparks.mtgox.MtGoxAPI;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class Order extends DtoBase implements CurrencyKludge {

    private String oid;
    private String currency;
    private String item;
    private MtGoxAPI.OrderType type;
    private TickerPrice amount;
    private TickerPrice effective_amount;
    private TickerPrice price;
    private String status;
    private String date;
    private String priority;
    private DynaBean[] actions;
    private TickerPrice invalid_amount;

    public Order(@JsonProperty("oid") String oid,
            @JsonProperty("currency") String currency,
            @JsonProperty("item") String item,
            @JsonProperty("type") String type,
            @JsonProperty("amount") TickerPrice amount,
            @JsonProperty("effective_amount") TickerPrice effective_amount,
            @JsonProperty("price") TickerPrice price,
            @JsonProperty("status") String status,
            @JsonProperty("date") String date,
            @JsonProperty("priority") String priority,
            @JsonProperty("actions") DynaBean[] actions,
            @JsonProperty("invalid_amount") TickerPrice invalid_amount) {
        this.oid = oid;
        this.currency = currency;
        this.item = item;
        this.type = type != null && type.equalsIgnoreCase("ask") ? MtGoxAPI.OrderType.Ask : MtGoxAPI.OrderType.Bid;
        this.amount = amount;
        this.effective_amount = effective_amount;
        this.price = price;
        this.status = status;
        this.date = date;
        this.priority = priority;
        this.actions = actions;
        this.invalid_amount = invalid_amount;

        if (this.amount != null) {
            this.amount.setCurrencyInfo(CurrencyInfo.BitcoinCurrencyInfo);
        }
        if (this.effective_amount != null) {
            this.effective_amount.setCurrencyInfo(CurrencyInfo.BitcoinCurrencyInfo);
        }
        if (this.invalid_amount != null) {
            this.invalid_amount.setCurrencyInfo(CurrencyInfo.BitcoinCurrencyInfo);
        }
    }

    /**
     * @return the oid
     */
    public String getOid() {
        return oid;
    }

    /**
     * @return the currency
     */
    public Currency getCurrency() {
        return Currency.getInstance(currency);
    }

    /**
     * @return the item
     */
    public String getItem() {
        return item;
    }

    /**
     * @return the type
     */
    public MtGoxAPI.OrderType getType() {
        return type;
    }

    /**
     * @return the amount
     */
    public TickerPrice getAmount() {
        return amount;
    }

    /**
     * @return the valid_amount
     */
    public TickerPrice getValidAmount() {
        return effective_amount;
    }

    /**
     * @return the price
     */
    public TickerPrice getPrice() {
        return price;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @return the priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * @return the actions
     */
    public DynaBean[] getActions() {
        return actions;
    }

    /**
     * @return the invalid_amount
     */
    public TickerPrice getInvalidAmount() {
        return invalid_amount;
    }

    @Override
    public void setCurrencyInfo(CurrencyInfo currencyInfo) {
        price.setCurrencyInfo(currencyInfo);
    }
}
