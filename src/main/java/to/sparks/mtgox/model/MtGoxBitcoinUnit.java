package to.sparks.mtgox.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

/**
 *
 * @author SparksG
 */
public class MtGoxBitcoinUnit extends MtGoxUnitOfCredit {

    public static final int BITCOIN_INT_SCALE = 8;

    private MtGoxBitcoinUnit(long int_value) {
        super(new BigDecimal(BigInteger.valueOf(int_value), BITCOIN_INT_SCALE), null);
    }

    private MtGoxBitcoinUnit(BigDecimal amount) {
        super(amount, null);
    }

    @Override
    public Currency getCurrency() {
        throw new UnsupportedOperationException("Bitcoins are not a supported Java currency.");
    }

    public static MtGoxBitcoinUnit createBitcoinInstance(long int_value) {
        return new MtGoxBitcoinUnit(int_value);
    }

    public static MtGoxBitcoinUnit createBitcoinInstance(BigDecimal amount) {
        return new MtGoxBitcoinUnit(amount);
    }
}
