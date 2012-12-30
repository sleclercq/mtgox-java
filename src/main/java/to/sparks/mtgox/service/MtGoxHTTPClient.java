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

import biz.source_code.base64Coder.Base64Coder;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Currency;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import to.sparks.mtgox.model.*;
import to.sparks.mtgox.net.MtGoxUrlFactory;
import to.sparks.mtgox.util.JSONSource;

/**
 * A simple implementation of a client for the MtGox HTTP API version 1.
 *
 * @author SparksG
 */
class MtGoxHTTPClient {

    private JSONSource<Result<Info>> privateInfoJSON;
    private JSONSource<Result<Order[]>> openOrdersJSON;
    private JSONSource<Result<String>> stringJSON;
    private JSONSource<Result<OrderResult>> orderResultJSON;
    private JSONSource<Result<FullDepth>> fullDepthJSON;
    private JSONSource<Result<Ticker>> tickerJSON;
    private String apiKey;
    private String secret;
    private static Logger logger;

    public MtGoxHTTPClient(final Logger logger, String apiKey, String secret) {
        this.apiKey = apiKey;
        this.secret = secret;
        openOrdersJSON = new JSONSource<>();
        stringJSON = new JSONSource<>();
        orderResultJSON = new JSONSource<>();
        fullDepthJSON = new JSONSource<>();
        tickerJSON = new JSONSource<>();
        privateInfoJSON = new JSONSource<>();

        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        try {

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {

            logger.log(Level.SEVERE, null, e);
        }

    }

    public FullDepth getFullDepth(Currency currency) throws Exception {
        FullDepth fullDepth = fullDepthJSON.getResultFromStream(new URL(MtGoxUrlFactory.getUrlForRestCommand(currency, MtGoxUrlFactory.RestCommand.FullDepth)).openStream(), FullDepth.class).getReturn();
        fullDepth.setCurrency(currency);  // Kludge that ensures the Offer arrays know what currency they are in.
        return fullDepth;
    }

    public String placeOrder(Currency currency, HashMap<String, String> params) throws Exception {
        Result<String> result = stringJSON.getResultFromStream(getMtGoxHTTPInputStream(MtGoxUrlFactory.getUrlForRestCommand(currency, MtGoxUrlFactory.RestCommand.PrivateOrderAdd), params), String.class);
        if (result.getError() != null) {
            throw new RuntimeException(result.getToken() + ": " + result.getError());
        }
        return result.getReturn();
    }

    public OrderResult getPrivateOrderResult(HashMap<String, String> params) throws Exception {
        Result<OrderResult> result = orderResultJSON.getResultFromStream(getMtGoxHTTPInputStream(MtGoxUrlFactory.getUrlForRestCommand(null, MtGoxUrlFactory.RestCommand.PrivateOrderResult), params), OrderResult.class);
        if (result.getError() != null) {
            throw new RuntimeException(result.getToken() + ": " + result.getError());
        }
        return result.getReturn();
    }

    public Order[] getOpenOrders() throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {

        Result<Order[]> openOrders = openOrdersJSON.getResultFromStream(getMtGoxHTTPInputStream(MtGoxUrlFactory.getUrlForRestCommand(null, MtGoxUrlFactory.RestCommand.PrivateOrders)), Order[].class);
        return openOrders.getReturn();
    }

    public Info getPrivateInfo() throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {

        Result<Info> privateInfo = privateInfoJSON.getResultFromStream(getMtGoxHTTPInputStream(MtGoxUrlFactory.getUrlForRestCommand(null, MtGoxUrlFactory.RestCommand.PrivateInfo)), Info.class);
        return privateInfo.getReturn();
    }

    public Ticker getTicker(Currency currency) throws IOException, Exception {
        Result<Ticker> tickerUSD = tickerJSON.getResultFromStream(new URL(MtGoxUrlFactory.getUrlForRestCommand(currency, MtGoxUrlFactory.RestCommand.Ticker)).openStream(), Ticker.class);
        return tickerUSD.getReturn();
    }

    /*
     * This function is based on an original idea by github user christopherobin
     * See https://gist.github.com/2396722
     */
    private InputStream getMtGoxHTTPInputStream(String path, HashMap<String, String> args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        HttpURLConnection connection;

        args.put("nonce", String.valueOf(System.currentTimeMillis()));
        String post_data = buildQueryString(args);

        // args signature
        Mac mac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secret_spec = new SecretKeySpec(Base64Coder.decode(this.secret), "HmacSHA512");
        mac.init(secret_spec);
        String signature = new String(Base64Coder.encode(mac.doFinal(post_data.getBytes()))).replaceAll("\n", "");

        System.setProperty("jsse.enableSNIExtension", "false");

        URL queryUrl = new URL(path);
        connection = (HttpURLConnection) queryUrl.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; mtgox-java client)");
        connection.setRequestProperty("Rest-Key", apiKey);
        connection.setRequestProperty("Rest-Sign", signature);
        connection.getOutputStream().write(post_data.getBytes());
        return connection.getInputStream();
    }

    private InputStream getMtGoxHTTPInputStream(String path) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return getMtGoxHTTPInputStream(path, new HashMap<String, String>());
    }

    private static String buildQueryString(HashMap<String, String> args) throws UnsupportedEncodingException {
        String result = new String();
        for (String hashkey : args.keySet()) {
            if (result.length() > 0) {
                result += '&';
            }
            result += URLEncoder.encode(hashkey, "UTF-8") + "="
                    + URLEncoder.encode(args.get(hashkey), "UTF-8");

        }
        return result;
    }
}
