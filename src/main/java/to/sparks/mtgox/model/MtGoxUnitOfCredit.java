package to.sparks.mtgox.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * A class to encapsulate all the weird mtgox money conversions. See...
 * https://en.bitcoin.it/wiki/MtGox/API
 *
 * @author SparksG
 */
public abstract class MtGoxUnitOfCredit implements Comparable<BigDecimal> {

    private CurrencyInfo currencyInfo;
    private BigDecimal numUnits;
    private final MathContext mc = new MathContext(0, RoundingMode.HALF_EVEN);

    public MtGoxUnitOfCredit(BigDecimal numUnits, CurrencyInfo currencyInfo) {
        this.numUnits = numUnits;
        this.currencyInfo = currencyInfo;
    }

    public MtGoxUnitOfCredit(long int_value, CurrencyInfo currencyInfo) {
        this.numUnits = new BigDecimal(BigInteger.valueOf(int_value), currencyInfo.getDecimals(), mc);
        this.currencyInfo = currencyInfo;
    }

    public MtGoxUnitOfCredit(double units, CurrencyInfo currencyInfo) {
        this.numUnits = new BigDecimal(units, mc).setScale(currencyInfo.getDecimals());
        this.currencyInfo = currencyInfo;
    }

    @Override
    public int compareTo(BigDecimal competitor) {
        return numUnits.compareTo(competitor);
    }

    public int compareTo(MtGoxUnitOfCredit competitor) {
        if (isCurrenciesEquivalent(competitor)) {
            return compareTo(competitor.getNumUnits());
        } else {
            throw new UnsupportedOperationException("Currency must be the same.");
        }
    }

    public CurrencyInfo getCurrencyInfo() {
        return currencyInfo;
    }

    public void setCurrencyInfo(CurrencyInfo currencyInfo) {
        this.currencyInfo = currencyInfo;
    }

    public BigDecimal getNumUnits() {
        return numUnits;
    }

    public long longValueExact() {
        return numUnits.longValueExact();
    }

    public long longValue() {
        return numUnits.longValue();
    }

    public BigDecimal add(BigDecimal target) {
        return numUnits.add(target);
    }

    public BigDecimal subtract(BigDecimal target) {
        return numUnits.subtract(target);
    }

    public BigDecimal add(MtGoxUnitOfCredit target) {
        if (isCurrenciesEquivalent(target)) {
            return add(target.getNumUnits());
        } else {
            throw new UnsupportedOperationException("Currency must be the same.");
        }
    }

    public BigDecimal subtract(MtGoxUnitOfCredit target) {
        if (isCurrenciesEquivalent(target)) {
            return subtract(target.getNumUnits());
        } else {
            throw new UnsupportedOperationException("Currency must be the same.");
        }
    }

    public BigDecimal multiply(BigDecimal target) {
        return numUnits.multiply(target);
    }

    public BigDecimal multiply(MtGoxUnitOfCredit target) {
        if (isCurrenciesEquivalent(target)) {
            return multiply(target.getNumUnits());
        } else {
            throw new UnsupportedOperationException("Currency must be the same.");
        }
    }

    public BigDecimal divide(BigDecimal target) {
        return numUnits.divide(target);
    }

    public BigDecimal divide(MtGoxUnitOfCredit target) {
        if (isCurrenciesEquivalent(target)) {
            return divide(target.getNumUnits());
        } else {
            throw new UnsupportedOperationException("Currency must be the same.");
        }
    }

    public BigInteger unscaledValue() {
        return numUnits.unscaledValue();
    }

    public String toPlainString() {
        return numUnits.toPlainString();
    }

    public boolean equals(MtGoxUnitOfCredit target) {
        if (isCurrenciesEquivalent(target)) {
            return equals(target.getNumUnits());
        } else {
            throw new UnsupportedOperationException("Currency must be the same.");
        }
    }

    public boolean equals(BigDecimal target) {
        return numUnits.compareTo(target) == 0;
    }

    private boolean isCurrenciesEquivalent(MtGoxUnitOfCredit o) {
        return (currencyInfo == null && o.getCurrencyInfo() == null) || (currencyInfo != null && currencyInfo.equals(o.getCurrencyInfo()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof MtGoxUnitOfCredit) {
            final MtGoxUnitOfCredit o = (MtGoxUnitOfCredit) obj;
            if (isCurrenciesEquivalent(o)) {
                return equals(o.getNumUnits());
            }
        } else if (obj instanceof BigDecimal) {
            return equals((BigDecimal) obj);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 13;
        hash = 79 * hash + Objects.hashCode(this.currencyInfo);
        hash = 79 * hash + Objects.hashCode(this.numUnits);
        return hash;
    }
}
