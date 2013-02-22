package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class Lag extends DtoBase {

    private long lag;
    private long lagSecs;

    public Lag(@JsonProperty("lag") long lag, @JsonProperty("lag_secs") long lagSecs) {
        this.lag = lag;
        this.lagSecs = lagSecs;
    }

    public long getLag() {
        return lag;
    }

    public long getLagSecs() {
        return lagSecs;
    }
}
