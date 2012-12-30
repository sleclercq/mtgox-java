/*
 * The MtGox-Java API is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The MtGox-Java API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the Lesser GNU General Public License
 * along with the MtGox-Java API .  If not, see <http://www.gnu.org/licenses/>.
 */
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
