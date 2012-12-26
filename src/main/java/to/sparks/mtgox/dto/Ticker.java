package to.sparks.mtgox.dto;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class Ticker extends DtoBase {

    private TickerPrice high;
    private TickerPrice low;
    private TickerPrice avg;
    private TickerPrice vwap;
    private TickerPrice vol;
    private TickerPrice last_local;
    private TickerPrice last;
    private TickerPrice last_orig;
    private TickerPrice last_all;
    private TickerPrice buy;
    private TickerPrice sell;

    public Ticker(@JsonProperty("high") TickerPrice high,
            @JsonProperty("low") TickerPrice low,
            @JsonProperty("avg") TickerPrice avg,
            @JsonProperty("vwap") TickerPrice vwap,
            @JsonProperty("vol") TickerPrice vol,
            @JsonProperty("last_local") TickerPrice last_local,
            @JsonProperty("last") TickerPrice last,
            @JsonProperty("last_orig") TickerPrice last_orig,
            @JsonProperty("last_all") TickerPrice last_all,
            @JsonProperty("buy") TickerPrice buy,
            @JsonProperty("sell") TickerPrice sell) {
        this.high = high;
        this.low = low;
        this.avg = avg;
        this.vwap = vwap;
        this.vol = vol;
        this.last_local = last_local;
        this.last = last;
        this.last_orig = last_orig;
        this.last_all = last_all;
        this.buy = buy;
        this.sell = sell;
    }

    /**
     * @return the high
     */
    public TickerPrice getHigh() {
        return high;
    }

    /**
     * @param high the high to set
     */
    public void setHigh(TickerPrice high) {
        this.high = high;
    }

    /**
     * @return the low
     */
    public TickerPrice getLow() {
        return low;
    }

    /**
     * @param low the low to set
     */
    public void setLow(TickerPrice low) {
        this.low = low;
    }

    /**
     * @return the avg
     */
    public TickerPrice getAvg() {
        return avg;
    }

    /**
     * @param avg the avg to set
     */
    public void setAvg(TickerPrice avg) {
        this.avg = avg;
    }

    /**
     * @return the vwap
     */
    public TickerPrice getVwap() {
        return vwap;
    }

    /**
     * @param vwap the vwap to set
     */
    public void setVwap(TickerPrice vwap) {
        this.vwap = vwap;
    }

    /**
     * @return the vol
     */
    public TickerPrice getVol() {
        return vol;
    }

    /**
     * @param vol the vol to set
     */
    public void setVol(TickerPrice vol) {
        this.vol = vol;
    }

    /**
     * @return the last_local
     */
    public TickerPrice getLast_local() {
        return last_local;
    }

    /**
     * @param last_local the last_local to set
     */
    public void setLast_local(TickerPrice last_local) {
        this.last_local = last_local;
    }

    /**
     * @return the last
     */
    public TickerPrice getLast() {
        return last;
    }

    /**
     * @param last the last to set
     */
    public void setLast(TickerPrice last) {
        this.last = last;
    }

    /**
     * @return the last_orig
     */
    public TickerPrice getLast_orig() {
        return last_orig;
    }

    /**
     * @param last_orig the last_orig to set
     */
    public void setLast_orig(TickerPrice last_orig) {
        this.last_orig = last_orig;
    }

    /**
     * @return the last_all
     */
    public TickerPrice getLast_all() {
        return last_all;
    }

    /**
     * @param last_all the last_all to set
     */
    public void setLast_all(TickerPrice last_all) {
        this.last_all = last_all;
    }

    /**
     * @return the buy
     */
    public TickerPrice getBuy() {
        return buy;
    }

    /**
     * @param buy the buy to set
     */
    public void setBuy(TickerPrice buy) {
        this.buy = buy;
    }

    /**
     * @return the sell
     */
    public TickerPrice getSell() {
        return sell;
    }

    /**
     * @param sell the sell to set
     */
    public void setSell(TickerPrice sell) {
        this.sell = sell;
    }
}
