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
package to.sparks.mtgox.service;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import to.sparks.mtgox.net.MtGoxHTTPAuthenticator;
import to.sparks.mtgox.net.MtGoxUrlFactory;

/**
 * A simple implementation of a client for the MtGox HTTP API version 0.
 *
 * @author SparksG
 */
public class MtGoxHTTPClientV0 extends MtGoxHTTPAuthenticator {

    public MtGoxHTTPClientV0(final Logger logger, String apiKey, String secret) {
        super(logger, apiKey, secret);

    }

    public String cancelOrder(HashMap<String, String> params) throws Exception {
        InputStream is = getMtGoxHTTPInputStream(MtGoxUrlFactory.getUrlForRestCommand("", MtGoxUrlFactory.RestCommand.PrivateOrderCancel), params);
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer);
        return writer.toString();
    }
}
