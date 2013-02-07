package to.sparks.mtgox.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public String getChannel() {
        return channel;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getOrigin() {
        return origin;
    }
}
