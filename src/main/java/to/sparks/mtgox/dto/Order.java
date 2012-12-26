package to.sparks.mtgox.dto;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class Order extends DtoBase {

    private String oid;
    private String currency;
    private String item;
    private String type;
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
        this.type = type;
        this.amount = amount;
        this.effective_amount = effective_amount;
        this.price = price;
        this.status = status;
        this.date = date;
        this.priority = priority;
        this.actions = actions;
        this.invalid_amount = invalid_amount;

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
     * @return the type
     */
    public String getType() {
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
}
