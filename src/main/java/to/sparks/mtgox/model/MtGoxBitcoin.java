package to.sparks.mtgox.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

/**
 *
 * @author SparksG
 */
public class MtGoxBitcoin extends MtGoxUnitOfCredit {

    public static final int BITCOIN_INT_SCALE = 8;

    private MtGoxBitcoin(long int_value) {
        super(new BigDecimal(BigInteger.valueOf(int_value), BITCOIN_INT_SCALE), null);
    }

    private MtGoxBitcoin(BigDecimal amount) {
        super(amount, null);
    }

    @Override
    public Currency getCurrency() {
        throw new UnsupportedOperationException("Bitcoins are not a supported Java currency.");
    }

    public static MtGoxBitcoin createBitcoinInstance(long int_value) {
        return new MtGoxBitcoin(int_value);
    }

    public static MtGoxBitcoin createBitcoinInstance(BigDecimal amount) {
        return new MtGoxBitcoin(amount);
    }
}
