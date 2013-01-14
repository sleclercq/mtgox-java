package to.sparks.mtgox.model;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import to.sparks.mtgox.net.JSONSource;

/**
 * A representation of bitcoins as used with MtGox
 *
 * @author SparksG
 */
public class MtGoxBitcoin extends MtGoxUnitOfCredit {

    private static final String BITCOIN_INFO_JSON = "{\"result\":\"success\",\"return\":{\"currency\":\"BTC\",\"name\":\"Bitcoin\",\"symbol\":\"BTC\",\"decimals\":\"8\",\"display_decimals\":\"2\",\"symbol_position\":\"after\",\"virtual\":\"Y\",\"ticker_channel\":\"13edff67-cfa0-4d99-aa76-52bd15d6a058\",\"depth_channel\":\"7d3d7ae3-7da7-48cf-9c82-51d7ab3fe60f\"}}";
    public static CurrencyInfo BitcoinCurrencyInfo;

    static {
        try {
            BitcoinCurrencyInfo = new JSONSource<Result<CurrencyInfo>>().getResultFromString(BITCOIN_INFO_JSON, CurrencyInfo.class).getReturn();
        } catch (IOException ex) {
            Logger.getLogger(MtGoxUnitOfCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * A convenience method for creating Bitcoin units of currency. Not
     * recommended though because you shouldn't really be storing monetary
     * values as doubles anyway. Use BigDecimal instead.
     */
    public MtGoxBitcoin(double float_value) {
        this(BigDecimal.valueOf(float_value).setScale(BitcoinCurrencyInfo.getDecimals()));
    }

    /*
     * Create units of bitcoin currency
     */
    public MtGoxBitcoin(BigDecimal amount) {
        this.amount = amount;
        this.currencyInfo = BitcoinCurrencyInfo;
    }

    /*
     * Create bitcoin units of currency This is an attempt to be directly
     * compatible with the MtGox API price_int parameter, which has variable
     * scales depending on currency code. See
     * https://en.bitcoin.it/wiki/MtGox/API#Number_Formats
     */
    public MtGoxBitcoin(long int_value) {
        this(new BigDecimal(BigInteger.valueOf(int_value), BitcoinCurrencyInfo.getDecimals()));
    }
}
