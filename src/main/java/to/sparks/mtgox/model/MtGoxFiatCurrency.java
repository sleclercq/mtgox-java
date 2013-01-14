package to.sparks.mtgox.model;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A real-world currency (such as USD) as used by MtGox
 *
 * @author SparksG
 */
public class MtGoxFiatCurrency extends MtGoxUnitOfCredit {

    /*
     * Create units of any currency
     */
    public MtGoxFiatCurrency(BigDecimal amount, CurrencyInfo currency) {
        this.amount = amount;
        this.currencyInfo = currency;
    }

    /*
     * Create units of any currency This is an attempt to be directly compatible
     * with the MtGox API price_int parameter, which has variable scales
     * depending on currency code. See
     * https://en.bitcoin.it/wiki/MtGox/API#Number_Formats
     */
    public MtGoxFiatCurrency(long int_value, CurrencyInfo currencyInfo) {
        this(new BigDecimal(BigInteger.valueOf(int_value), currencyInfo.getDecimals()), currencyInfo);
    }

    /*
     * Create units of any currency A convenience method for creating Bitcoin
     * units. Not recommended though because you shouldn't really be storing
     * monetary values as doubles anyway. Use BigDecimal instead.
     */
    public MtGoxFiatCurrency(double float_value, CurrencyInfo currencyInfo) {
        this(BigDecimal.valueOf(float_value).setScale(currencyInfo.getDecimals()), currencyInfo);
    }
}
