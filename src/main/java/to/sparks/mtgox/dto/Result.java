package to.sparks.mtgox.dto;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class Result<T> extends DtoBase {

    private String result;
    private T ret;
    private String error;
    private String token;

    @JsonCreator
    public Result(@JsonProperty("result") String result,
            @JsonProperty("return") T ret,
            @JsonProperty("error") String error,
            @JsonProperty("token") String token) {
        this.result = result;
        this.ret = ret;
        this.error = error;
        this.token = token;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @return the ret
     */
    public T getReturn() {
        return ret;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }
}
