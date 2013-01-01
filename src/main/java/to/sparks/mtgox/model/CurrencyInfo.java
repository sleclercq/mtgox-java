package to.sparks.mtgox.model;

import java.util.Currency;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Contains information about an MtGox currency
 *
 * @author SparksG
 */
public class CurrencyInfo extends DtoBase {

    private String currency_code;
    private String name;
    private String symbol;
    private int decimals;
    private int display_decimals;
    private String symbol_position;
    private boolean virtual;
    private String ticker_channel;
    private String depth_channel;

    public CurrencyInfo(@JsonProperty("currency") String currency_code,
            @JsonProperty("name") String name,
            @JsonProperty("symbol") String symbol,
            @JsonProperty("decimals") int decimals,
            @JsonProperty("display_decimals") int display_decimals,
            @JsonProperty("symbol_position") String symbol_position,
            @JsonProperty("virtual") String virtual,
            @JsonProperty("ticker_channel") String ticker_channel,
            @JsonProperty("depth_channel") String depth_channel) {
        this.currency_code = currency_code;
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
        this.display_decimals = display_decimals;
        this.symbol_position = symbol_position;
        this.virtual = virtual.trim().equalsIgnoreCase("Y");
        this.ticker_channel = ticker_channel;
        this.depth_channel = depth_channel;
    }

    /**
     * @return the currency
     */
    public Currency getCurrency() {
        if (isVirtual()) {
            throw new UnsupportedOperationException("Virtual MtGox currencies cannot be expressed as a Java currency.");
        }
        return Currency.getInstance(currency_code);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @return the decimals
     */
    public int getDecimals() {
        return decimals;
    }

    /**
     * @return the display_decimals
     */
    public int getDisplay_decimals() {
        return display_decimals;
    }

    /**
     * @return the symbol_position
     */
    public String getSymbol_position() {
        return symbol_position;
    }

    /**
     * @return the virtual
     */
    public boolean isVirtual() {
        return virtual;
    }

    /**
     * @return the ticker_channel
     */
    public String getTicker_channel() {
        return ticker_channel;
    }

    /**
     * @return the depth_channel
     */
    public String getDepth_channel() {
        return depth_channel;
    }
}
