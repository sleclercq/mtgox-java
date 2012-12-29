package to.sparks.mtgox.model;

import java.util.Currency;
import java.util.Locale;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class Trade extends DtoBase implements IEventTime {

    private long amount_int; // "amount_int":"1000000",
    private long price_int; //    "price_int":"1336001",
    private long date; // "date":1356641315,
    private String item; // "item":"BTC",
    private String type; // "type":"trade"
    private String primary; // "primary":"Y",
    private String properties; // "properties":"limit",
    private String price_currency; // "price_currency":"USD",
    private String tid; // "tid":"1356641315101735",
    private String trade_type; // "trade_type":"ask",

    public Trade(@JsonProperty("tid") String tid,
            @JsonProperty("primary") String primary,
            @JsonProperty("price_currency") String price_currency,
            @JsonProperty("type") String type,
            @JsonProperty("properties") String properties,
            @JsonProperty("item") String item,
            @JsonProperty("amount") double amount,
            @JsonProperty("price") double price,
            @JsonProperty("trade_type") String trade_type,
            @JsonProperty("date") long date,
            @JsonProperty("amount_int") long amount_int,
            @JsonProperty("price_int") long price_int) {
        this.tid = tid;
        this.primary = primary;
        this.price_currency = price_currency;
        this.type = type;
        this.properties = properties;
        this.item = item;
        this.trade_type = trade_type;
        this.date = date;
        this.amount_int = amount_int;
        this.price_int = price_int;

    }

    /**
     * @return the trade_id
     */
    public String getTradeId() {
        return tid;
    }

    /**
     * @return the primary
     */
    public String getPrimary() {
        return primary;
    }

    /**
     * @return the currency
     */
    public Currency getPriceCurrency() {
        return Currency.getInstance(price_currency);
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the properties
     */
    public String getProperties() {
        return properties;
    }

    /**
     * @return the item
     */
    public String getItem() {
        return item;
    }

    /**
     * @return the trade type
     */
    public String getTrade_type() {
        return trade_type;
    }

    /**
     * @return the date
     */
    public long getDate() {
        return date;
    }

    public long getAmount_int() {
        return amount_int;
    }

    public long getPrice_int() {
        return price_int;
    }

    @Override
    public long getEventTime() {
        return getDate();
    }
}
