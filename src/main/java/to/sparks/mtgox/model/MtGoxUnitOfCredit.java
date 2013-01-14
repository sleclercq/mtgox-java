package to.sparks.mtgox.model;

import java.math.BigDecimal;

/**
 * A class to encapsulate all the weird mtgox money conversions. See...
 * https://en.bitcoin.it/wiki/MtGox/API
 *
 * @author SparksG
 */
abstract class MtGoxUnitOfCredit extends DtoBase {

    protected BigDecimal amount;
    protected CurrencyInfo currencyInfo;

    public BigDecimal getCredits() {
        return amount;
    }

    public CurrencyInfo getCurrencyInfo() {
        return currencyInfo;
    }
}
