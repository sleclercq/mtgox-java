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
import java.util.HashMap;
import java.util.logging.Logger;
import to.sparks.mtgox.model.OrderCancelResult;
import to.sparks.mtgox.net.MtGoxHTTPAuthenticator;
import to.sparks.mtgox.net.MtGoxUrlFactory;
import to.sparks.mtgox.util.JSONSource;

/**
 * A simple implementation of a client for the MtGox HTTP API version 0.
 *
 * @author SparksG
 * @deprecated This class only exists because the Version 1 API does not contain all the functions we need.  It will be removed as these functions become available on later releases of the MtGox HTTP API.
 */
@Deprecated
public class MtGoxHTTPClientV0 extends MtGoxHTTPAuthenticator {

    private JSONSource<OrderCancelResult> orderCancelJSON;

    public MtGoxHTTPClientV0(final Logger logger, String apiKey, String secret) {
        super(logger, apiKey, secret);
        orderCancelJSON = new JSONSource<>();
    }

    public OrderCancelResult cancelOrder(HashMap<String, String> params) throws Exception {
        InputStream is = getMtGoxHTTPInputStream(MtGoxUrlFactory.getUrlForRestCommand("", MtGoxUrlFactory.RestCommand.PrivateOrderCancel), params);
        return orderCancelJSON.getResultFromStream(is, OrderCancelResult.class);
    }
}
