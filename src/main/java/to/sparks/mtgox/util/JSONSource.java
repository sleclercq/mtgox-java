package to.sparks.mtgox.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import to.sparks.mtgox.model.DtoBase;
import to.sparks.mtgox.model.Result;

/**
 * Parse JSON into java objects.
 *
 * @author SparksG
 */
public class JSONSource<T extends DtoBase> {

    private JsonFactory factory = new JsonFactory();
    private ObjectMapper mapper = new ObjectMapper();

    public JSONSource() {
        factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
    }

    public T getResultFromStream(InputStream in, Class clazz) throws IOException {
        JsonParser jp = factory.createJsonParser(in);
        JavaType topMost = mapper.getTypeFactory().constructParametricType(Result.class, clazz);
        return mapper.readValue(jp, topMost);
    }

    public T getResultFromFile(String filename, Class clazz) throws IOException {
        return getResultFromStream(new FileInputStream(filename), clazz);
    }

    public String getDTOasJSON(DtoBase dto) throws IOException {
        return mapper.writeValueAsString(dto);
    }
}
