package to.sparks.mtgox.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author SparksG
 */
public class MtGoxCurrency extends MtGoxUnitOfCredit {

    private static final Map<Currency, Integer> scaleMap;

    static {
        Map<Currency, Integer> aMap = new HashMap<>();
        aMap.put(Currency.getInstance("AUD"), 5);
        aMap.put(Currency.getInstance("USD"), 8);
        aMap.put(Currency.getInstance("JPY"), 3);
        scaleMap = Collections.unmodifiableMap(aMap);
    }

    private MtGoxCurrency(long int_value, Currency currency, int scale) {
        super(new BigDecimal(BigInteger.valueOf(int_value), scale), currency);
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    public static MtGoxCurrency createCurrencyInstance(long int_value, Currency currency) {
        int scale = 5;
        if (scaleMap.containsKey(currency)) {
            scale = scaleMap.get(currency);
        }
        return new MtGoxCurrency(int_value, currency, scale);
    }
}
