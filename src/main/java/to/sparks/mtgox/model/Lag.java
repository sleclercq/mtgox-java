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
    private String lagText;
    private long length;

    public Lag(@JsonProperty("lag") long lag, @JsonProperty("lag_secs") long lagSecs, @JsonProperty("lag_text") String lagText, @JsonProperty("length") long length) {
        this.lag = lag;
        this.lagSecs = lagSecs;
        this.lagText = lagText;
        this.length = length;
    }

    public long getLag() {
        return lag;
    }

    public long getLagSecs() {
        return lagSecs;
    }

    public String getLagText() {
        return lagText;
    }

    public long getLength() {
        return length;
    }
}
