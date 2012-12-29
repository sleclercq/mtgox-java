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
public class MtGoxFiatUnit extends MtGoxUnitOfCredit {

    private static final Map<Currency, Integer> scaleMap;

    static {
        Map<Currency, Integer> aMap = new HashMap<>();
        aMap.put(Currency.getInstance("AUD"), 5);
        aMap.put(Currency.getInstance("USD"), 8);
        aMap.put(Currency.getInstance("JPY"), 3);
        scaleMap = Collections.unmodifiableMap(aMap);
    }

    private MtGoxFiatUnit(long int_value, Currency currency, int scale) {
        super(new BigDecimal(BigInteger.valueOf(int_value), scale), currency);
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    public static MtGoxFiatUnit createCurrencyInstance(long int_value, Currency currency) {
        int scale = 5;
        if (scaleMap.containsKey(currency)) {
            scale = scaleMap.get(currency);
        }
        return new MtGoxFiatUnit(int_value, currency, scale);
    }
}
