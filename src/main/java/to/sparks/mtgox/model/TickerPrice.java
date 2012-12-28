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
public class TickerPrice extends DtoBase {

    private long value_int;
    private String display;
    private String display_short;
    private String currency;

    public TickerPrice(@JsonProperty("value") double value,
            @JsonProperty("value_int") long value_int,
            @JsonProperty("display") String display,
            @JsonProperty("display_short") String display_short,
            @JsonProperty("currency") String currency) {
        this.value_int = value_int;
        this.display = display;
        this.display_short = display_short;
        this.currency = currency;
    }

    /**
     * @return the value_int
     */
    public long getValue_int() {
        return value_int;
    }

    /**
     * @param value_int the value_int to set
     */
    public void setValue_int(long value_int) {
        this.value_int = value_int;
    }

    /**
     * @return the display
     */
    public String getDisplay() {
        return display;
    }

    /**
     * @param display the display to set
     */
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     * @return the display_short
     */
    public String getDisplay_short() {
        return display_short;
    }

    /**
     * @param display_short the display_short to set
     */
    public void setDisplay_short(String display_short) {
        this.display_short = display_short;
    }

    /**
     * @return the currency
     */
    public Currency getCurrency() {
        return Currency.getInstance(currency);
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(Currency currency) {
        this.currency = currency.getCurrencyCode();
    }
}
