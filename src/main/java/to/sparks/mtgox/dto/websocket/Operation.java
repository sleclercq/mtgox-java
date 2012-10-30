package to.sparks.mtgox.dto.websocket;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
public abstract class Operation {

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
