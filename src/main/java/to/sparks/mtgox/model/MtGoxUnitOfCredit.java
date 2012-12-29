package to.sparks.mtgox.model;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * A class to encapsulate all the weird mtgox money conversions. See...
 * https://en.bitcoin.it/wiki/MtGox/API
 *
 * @author SparksG
 */
public abstract class MtGoxUnitOfCredit<T> {

    private BigDecimal amount;
    protected Currency currency;

    protected MtGoxUnitOfCredit(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getCredits() {
        return amount;
    }

    public abstract Currency getCurrency();
}
