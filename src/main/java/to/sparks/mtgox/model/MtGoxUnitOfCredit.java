package to.sparks.mtgox.model;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import to.sparks.mtgox.util.JSONSource;

/**
 * A class to encapsulate all the weird mtgox money conversions. See...
 * https://en.bitcoin.it/wiki/MtGox/API
 *
 * @author SparksG
 */
public class MtGoxUnitOfCredit extends DtoBase {

    private static final String BITCOIN_INFO_JSON = "{\"result\":\"success\",\"return\":{\"currency\":\"BTC\",\"name\":\"Bitcoin\",\"symbol\":\"BTC\",\"decimals\":\"8\",\"display_decimals\":\"2\",\"symbol_position\":\"after\",\"virtual\":\"Y\",\"ticker_channel\":\"13edff67-cfa0-4d99-aa76-52bd15d6a058\",\"depth_channel\":\"7d3d7ae3-7da7-48cf-9c82-51d7ab3fe60f\"}}";
    private BigDecimal amount;
    protected CurrencyInfo currencyInfo;
    public static CurrencyInfo BitcoinCurrencyInfo;

    static {
        try {
            BitcoinCurrencyInfo = new JSONSource<Result<CurrencyInfo>>().getResultFromString(BITCOIN_INFO_JSON, CurrencyInfo.class).getReturn();
        } catch (IOException ex) {
            Logger.getLogger(MtGoxUnitOfCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Create units of any currency A convenience method for creating Bitcoin
     * units. Not recommended though because you shouldn't really be storing
     * monetary values as doubles anyway. Use BigDecimal instead.
     */
    public MtGoxUnitOfCredit(double float_value, CurrencyInfo currencyInfo) {
        this(BigDecimal.valueOf(float_value).setScale(currencyInfo.getDecimals()), currencyInfo);
    }

    /*
     * Create units of any currency This is an attempt to be directly compatible
     * with the MtGox API price_int parameter, which has variable scales
     * depending on currency code. See
     * https://en.bitcoin.it/wiki/MtGox/API#Number_Formats
     */
    public MtGoxUnitOfCredit(long int_value, CurrencyInfo currencyInfo) {
        this(new BigDecimal(BigInteger.valueOf(int_value), currencyInfo.getDecimals()), currencyInfo);
    }

    /*
     * Create units of any currency
     */
    protected MtGoxUnitOfCredit(BigDecimal amount, CurrencyInfo currency) {
        this.amount = amount;
        this.currencyInfo = currency;
    }

    /*
     * Create units of bitcoin currency
     */
    protected MtGoxUnitOfCredit(BigDecimal amount) {
        this.amount = amount;
        this.currencyInfo = BitcoinCurrencyInfo;
    }

    /*
     * A convenience method for creating Bitcoin units of currency. Not
     * recommended though because you shouldn't really be storing monetary
     * values as doubles anyway. Use BigDecimal instead.
     */
    public MtGoxUnitOfCredit(double float_value) {
        this(BigDecimal.valueOf(float_value).setScale(BitcoinCurrencyInfo.getDecimals()));
    }

    /*
     * Create bitcoin units of currency This is an attempt to be directly
     * compatible with the MtGox API price_int parameter, which has variable
     * scales depending on currency code. See
     * https://en.bitcoin.it/wiki/MtGox/API#Number_Formats
     */
    public MtGoxUnitOfCredit(long int_value) {
        this(new BigDecimal(BigInteger.valueOf(int_value), BitcoinCurrencyInfo.getDecimals()));
    }

    public BigDecimal getCredits() {
        return amount;
    }

    public CurrencyInfo getCurrencyInfo() {
        return currencyInfo;
    }
}
