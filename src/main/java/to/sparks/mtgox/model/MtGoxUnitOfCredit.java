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
    private final MathContext mc = new MathContext(8, RoundingMode.HALF_EVEN);

    public MtGoxUnitOfCredit(BigDecimal numUnits, CurrencyInfo currencyInfo) {
        this.numUnits = numUnits;
        this.currencyInfo = currencyInfo;
    }

    public MtGoxUnitOfCredit(long int_value, CurrencyInfo currencyInfo) {
        this.numUnits = new BigDecimal(BigInteger.valueOf(int_value), currencyInfo.getDecimals());
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
        return add(target.getNumUnits());
    }

    public BigDecimal subtract(MtGoxUnitOfCredit target) {
        return subtract(target.getNumUnits());
    }

    public BigDecimal multiply(BigDecimal target) {
        return numUnits.multiply(target);
    }

    public BigDecimal multiply(MtGoxUnitOfCredit target) {
        return multiply(target.getNumUnits());
    }

    public BigDecimal divide(BigDecimal target) {
        return numUnits.divide(target);
    }

    public BigDecimal divide(MtGoxUnitOfCredit target) {
        return divide(target.getNumUnits());
    }

    public BigInteger unscaledValue() {
        return numUnits.unscaledValue();
    }

    public String toPlainString() {
        return numUnits.toPlainString();
    }

    public boolean equals(MtGoxUnitOfCredit target) {
        // TODO:  Also check currency for equality?
        return equals(target.getNumUnits());
    }

    public boolean equals(BigDecimal target) {
        return numUnits.compareTo(target) == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof MtGoxUnitOfCredit) {
            final MtGoxUnitOfCredit o = (MtGoxUnitOfCredit) obj;
            if ((currencyInfo == null && o.getCurrencyInfo() == null) || (currencyInfo != null && currencyInfo.equals(o.getCurrencyInfo()))) {
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
