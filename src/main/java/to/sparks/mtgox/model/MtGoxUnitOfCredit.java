package to.sparks.mtgox.model;

import java.math.BigDecimal;

/**
 * A class to encapsulate all the weird mtgox money conversions. See...
 * https://en.bitcoin.it/wiki/MtGox/API
 *
 * @author SparksG
 */
public abstract class MtGoxUnitOfCredit<T> {

    private BigDecimal amount;
    protected CurrencyInfo currencyInfo;

    protected MtGoxUnitOfCredit(BigDecimal amount, CurrencyInfo currency) {
        this.amount = amount;
        this.currencyInfo = currency;
    }

    public BigDecimal getCredits() {
        return amount;
    }

    public abstract CurrencyInfo getCurrencyInfo();
}
