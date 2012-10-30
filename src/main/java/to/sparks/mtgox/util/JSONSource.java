package to.sparks.mtgox.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import to.sparks.dto.Result;

/**
 *
 * @author SparksG
 */
public class JSONSource<T> {

    private JsonFactory factory = new JsonFactory();
    private ObjectMapper mapper = new ObjectMapper();

    public JSONSource() {
        factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
    }

    public T getResultFromStream(InputStream in, Class clazz) throws IOException {
        JsonParser jp = factory.createJsonParser(in);
        JavaType topMost = mapper.getTypeFactory().constructParametricType(Result.class, clazz);
        return mapper.readValue(jp, topMost);
    }

    public T getResultFromFile(String filename, Class clazz) throws IOException {
        return getResultFromStream(new FileInputStream(filename), clazz);
    }
}
