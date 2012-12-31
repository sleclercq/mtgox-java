package to.sparks.mtgox.model;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A representation of a certain number of units of bitcoins, stored in a
 * decimal scale as used by the MtGox API.
 * <code>
 *      // Create a representation of 1.0 bitcoins.
 *      long amount_int = 100000000L;
 *      MtGoxBitcoinUnit bitcoinUnit = MtGoxBitcoinUnit.createBitcoinInstance(amount_int);
 * </code>
 *
 * @author SparksG
 */
public class MtGoxBitcoinUnit extends MtGoxUnitOfCredit {

    public static final int BITCOIN_INT_SCALE = 8;

    /*
     * A convenience method for creating Bitcoin units. Not recommended though
     * because you shouldn't really be storing monetary values as doubles
     * anyway. Use BigDecimal instead.
     */
    private MtGoxBitcoinUnit(double float_value) {
        super(BigDecimal.valueOf(float_value), null);
    }

    /*
     * Use this constructor with the int_value returned by the MtGox API.
     */
    private MtGoxBitcoinUnit(long int_value) {
        super(new BigDecimal(BigInteger.valueOf(int_value), BITCOIN_INT_SCALE), null);
    }

    private MtGoxBitcoinUnit(BigDecimal amount) {
        super(amount, null);
    }

    @Override
    public CurrencyInfo getCurrencyInfo() {
        return currencyInfo;
    }

    public static MtGoxBitcoinUnit createBitcoinInstance(long int_value) {
        return new MtGoxBitcoinUnit(int_value);
    }

    public static MtGoxBitcoinUnit createBitcoinInstance(BigDecimal amount) {
        return new MtGoxBitcoinUnit(amount);
    }

    public static MtGoxBitcoinUnit createBitcoinInstance(double float_value) {
        return new MtGoxBitcoinUnit(float_value);
    }
}
