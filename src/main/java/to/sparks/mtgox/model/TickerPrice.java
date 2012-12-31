package to.sparks.mtgox.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class TickerPrice extends DtoBase implements CurrencyKludge {

    private String display;
    private String display_short;
    private CurrencyInfo currencyInfo;
    private long value_int;
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

    /*
     * This is a bit of a kludge that ensures the offers know what currency they
     * are in.
     */
    @Override
    public void setCurrencyInfo(CurrencyInfo currencyInfo) {
        this.currencyInfo = currencyInfo;
    }

    /**
     * This function must be called *after* you have setCurrencyInfo on this
     * object.
     *
     * @return the value_int
     */
    public MtGoxUnitOfCredit getPriceValue() {
        if (currency.equalsIgnoreCase("BTC")) {
            return new MtGoxBitcoinUnit(value_int);
        } else {
            return new MtGoxFiatUnit(value_int, currencyInfo);
        }

    }

    /**
     * @return the display
     */
    public String getDisplay() {
        return display;
    }

    /**
     * @return the display_short
     */
    public String getDisplay_short() {
        return display_short;
    }
}
