package to.sparks.mtgox.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

/**
 * Create an instance of a real-world currency, e.g., USD that holds a value
 * represented by a scalar integer.
 *
 * @author SparksG
 */
public class MtGoxFiatUnit extends MtGoxUnitOfCredit {

    private static final Map<Currency, Integer> scaleMap;

    static {
        Map<Currency, Integer> aMap = new HashMap<>();
        aMap.put(Currency.getInstance("AUD"), 5);
        aMap.put(Currency.getInstance("USD"), 5);
        aMap.put(Currency.getInstance("JPY"), 3);
        // TODO: Add other currency scale values from MtGox?
        scaleMap = Collections.unmodifiableMap(aMap);
    }

    private MtGoxFiatUnit(BigDecimal value, Currency currency) {
        super(value, currency);
    }

    private MtGoxFiatUnit(double float_value, Currency currency) {
        super(BigDecimal.valueOf(float_value), currency);
    }

    private MtGoxFiatUnit(long int_value, Currency currency, int scale) {
        super(new BigDecimal(BigInteger.valueOf(int_value), scale), currency);
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    /*
     * This is an attempt to be directly compatible with the MtGox API price_int
     * parameter, which has variable scales depending on currency code. See
     * https://en.bitcoin.it/wiki/MtGox/API#Number_Formats
     */
    public static MtGoxFiatUnit createFiatInstance(long int_value, Currency currency) {
        int scale = 8; // Default is 8?
        if (scaleMap.containsKey(currency)) {
            scale = scaleMap.get(currency);
        }
        return new MtGoxFiatUnit(int_value, currency, scale);
    }

    /*
     * This is the recommended way to create a representation of money.
     *
     * <code>
     *
     * MtGoxFiatUnit.createFiatInstance(price_int,mtgoxAPI.getBaseCurrency());
     *
     * </code>
     */
    public static MtGoxFiatUnit createFiatInstance(BigDecimal amount, Currency currency) {
        return new MtGoxFiatUnit(amount, currency);
    }

    /*
     * A convenience method for creating Bitcoin units. Not recommended though
     * because you shouldn't really be storing monetary values as doubles
     * anyway. Use BigDecimal instead.
     */
    public static MtGoxFiatUnit createFiatInstance(double float_value, Currency currency) {
        return new MtGoxFiatUnit(float_value, currency);
    }
}
