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
package to.sparks.mtgox;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Currency;
import java.util.HashMap;
import to.sparks.mtgox.model.*;

/**
 * See MtGoxAPI.java for javadocs
 *
 * @author SparksG
 */
public interface HTTPClientV1 {

    CurrencyInfo getCurrencyInfo(Currency currency) throws IOException, Exception;

    CurrencyInfo getCurrencyInfo(String currencyCode) throws IOException, Exception;

    FullDepth getFullDepth(Currency currency) throws Exception;

    Order[] getOpenOrders() throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception;

    AccountInfo getPrivateInfo() throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception;

    OrderResult getPrivateOrderResult(HashMap<String, String> params) throws Exception;

    Ticker getTicker(Currency currency) throws IOException, Exception;

    String placeOrder(Currency currency, HashMap<String, String> params) throws Exception;

    SendBitcoinsTransaction sendBitcoins(HashMap<String, String> params) throws Exception;
}
