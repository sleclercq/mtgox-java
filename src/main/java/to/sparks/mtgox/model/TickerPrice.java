package to.sparks.mtgox.model;

import java.util.Currency;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class TickerPrice extends DtoBase {

    private MtGoxUnitOfCredit priceValue;
    private String display;
    private String display_short;
    private Currency currency = null;

    public TickerPrice(@JsonProperty("value") double value,
            @JsonProperty("value_int") long value_int,
            @JsonProperty("display") String display,
            @JsonProperty("display_short") String display_short,
            @JsonProperty("currency") String currency) {
        this.display = display;
        this.display_short = display_short;
        if(currency.equalsIgnoreCase("BTC"))
        {
            this.priceValue = MtGoxBitcoin.createBitcoinInstance(value_int);
        }else
        {
        this.currency = Currency.getInstance(currency);
        this.priceValue = MtGoxCurrency.createCurrencyInstance(value_int, this.currency);
        }
    }

    /**
     * @return the value_int
     */
    public MtGoxUnitOfCredit getPriceValue() {
        return priceValue;
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
