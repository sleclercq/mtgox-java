package to.sparks.mtgox.model;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Create an instance of a real-world currency, e.g., USD that holds a value
 * represented by a scalar integer.
 *
 * @author SparksG
 */
public class MtGoxFiatUnit extends MtGoxUnitOfCredit {

    /*
     * This is the recommended way to create a representation of money.
     */
    public MtGoxFiatUnit(BigDecimal value, CurrencyInfo currencyInfo) {
        super(value, currencyInfo);
    }

    /*
     * A convenience method for creating Bitcoin units. Not recommended though
     * because you shouldn't really be storing monetary values as doubles
     * anyway. Use BigDecimal instead.
     */
    public MtGoxFiatUnit(double float_value, CurrencyInfo currencyInfo) {
        super(BigDecimal.valueOf(float_value), currencyInfo);
    }

    /*
     * This is an attempt to be directly compatible with the MtGox API price_int
     * parameter, which has variable scales depending on currency code. See
     * https://en.bitcoin.it/wiki/MtGox/API#Number_Formats
     */
    public MtGoxFiatUnit(long int_value, CurrencyInfo currencyInfo) {
        super(new BigDecimal(BigInteger.valueOf(int_value), currencyInfo.getDecimals()), currencyInfo);
    }

    @Override
    public CurrencyInfo getCurrencyInfo() {
        return currencyInfo;
    }
}
