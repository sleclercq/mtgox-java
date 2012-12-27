package to.sparks.mtgox.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public abstract class OpPrivate<T> extends Operation {

    private String channel;
    private String messageType;
    private String origin;

    public OpPrivate(@JsonProperty("op") String op,
            @JsonProperty("channel") String channel,
            @JsonProperty("private") String messageType,
            @JsonProperty("origin") String origin) {
        super(op);
        this.channel = channel;
        this.messageType = messageType;
        this.origin = origin;
    }

    /**
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * @return the messageType
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }
}
