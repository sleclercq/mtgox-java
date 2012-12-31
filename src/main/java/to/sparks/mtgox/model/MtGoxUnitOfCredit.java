package to.sparks.mtgox.model;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A class to encapsulate all the weird mtgox money conversions. See...
 * https://en.bitcoin.it/wiki/MtGox/API
 *
 * @author SparksG
 */
public class MtGoxUnitOfCredit {

    private BigDecimal amount;
    protected CurrencyInfo currencyInfo;

    /*
     * A convenience method for creating Bitcoin units. Not recommended though
     * because you shouldn't really be storing monetary values as doubles
     * anyway. Use BigDecimal instead.
     */
    public MtGoxUnitOfCredit(double float_value, CurrencyInfo currencyInfo) {
        this(BigDecimal.valueOf(float_value).setScale(currencyInfo.getDecimals()), currencyInfo);
    }

    /*
     * This is an attempt to be directly compatible with the MtGox API price_int
     * parameter, which has variable scales depending on currency code. See
     * https://en.bitcoin.it/wiki/MtGox/API#Number_Formats
     */
    public MtGoxUnitOfCredit(long int_value, CurrencyInfo currencyInfo) {
        this(new BigDecimal(BigInteger.valueOf(int_value), currencyInfo.getDecimals()), currencyInfo);
    }

    protected MtGoxUnitOfCredit(BigDecimal amount, CurrencyInfo currency) {
        this.amount = amount;
        this.currencyInfo = currency;
    }

    public BigDecimal getCredits() {
        return amount;
    }

    public CurrencyInfo getCurrencyInfo() {
        return currencyInfo;
    }
}
