package to.sparks.mtgox.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public abstract class Operation extends DtoBase {

    private String op;

    public Operation(@JsonProperty("op") String op) {
        this.op = op;
    }

    /**
     * @return the op
     */
    public String getOp() {
        return op;
    }
}
