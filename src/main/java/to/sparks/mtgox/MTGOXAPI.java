package to.sparks.mtgox;

import biz.source_code.base64Coder.Base64Coder;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.jwebsocket.kit.WebSocketException;
import to.sparks.mtgox.dto.*;
import to.sparks.mtgox.util.JSONSource;
import to.sparks.mtgox.util.MtGoxRealTime;

/**
 * All MtGox API interactions (both HTTP and Websocket) are handled by this
 * class.
 *
 * @author SparksG
 */
public class MTGOXAPI {

    // TODO:  This value is currency dependent.  JPY is different from USD for example.
    public static double USD_INT_MULTIPLIER = 100000000.0D;
    public static double AUD_INT_MULTIPLIER = 100000.0D;
    public static double BTC_VOL_INT_MULTIPLIER = 100000000.0D;

    public enum OrderType {

        bid, ask
    }

    public enum Currency {

        USD, AUD
    }
    private static String MTGOX_HTTP_API_URL = "https://mtgox.com/api/";
    private static String ORDER_ADD_URL = "1/BTCUSD/private/order/add";
    private static String ORDER_RESULT_URL = "1/generic/private/order/result";
    private static String PRIVATE_ORDERS_URL = "1/generic/private/orders";
    private static String BTC_USD_FULL_DEPTH = "https://mtgox.com/api/1/BTCUSD/fulldepth";
    private String apiKey;
    private String secret;
    private Logger logger;
    private JSONSource<Result<Order[]>> openOrdersJSON;
    private JSONSource<Result<String>> stringJSON;
    private JSONSource<Result<OrderResult>> orderResultJSON;
    private JSONSource<Result<FullDepth>> fullDepthJSON;
    private static MtGoxRealTime mtGoxRealTime;

    public MTGOXAPI(final Logger logger, String apiKey, String secret) {
        this.logger = logger;
        this.apiKey = apiKey;
        this.secret = secret;
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

        openOrdersJSON = new JSONSource<>();
        stringJSON = new JSONSource<>();
        orderResultJSON = new JSONSource<>();
        fullDepthJSON = new JSONSource<>();

        Thread t = new Thread() {

            public void run() {
                try {
                    mtGoxRealTime = new MtGoxRealTime(getFullDepth());
                } catch (WebSocketException | IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        };
        t.start();

    }

    public List<Offer> getRealtimeAsks() {
        return mtGoxRealTime != null ? mtGoxRealTime.getAsks() : null;
    }

    public List<Offer> getRealtimeBids() {
        return mtGoxRealTime != null ? mtGoxRealTime.getBids() : null;
    }

    public FullDepth getFullDepth() throws IOException {
        return fullDepthJSON.getResultFromStream(new URL(BTC_USD_FULL_DEPTH).openStream(), FullDepth.class).getReturn();
    }

    public static int convertVolumeBTCtoInt(double d) {
        double total = d * BTC_VOL_INT_MULTIPLIER;
        return (int) total;
    }

    public static int convertPriceAUDtoInt(double d) {
        double total = d * AUD_INT_MULTIPLIER;
        return (int) total;
    }

    public String placeOrder(OrderType orderType, Double price, double volume) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        HashMap<String, String> params = new HashMap<>();
        if (orderType == OrderType.bid) {
            params.put("type", "bid");
        } else {
            params.put("type", "ask");
        }
        if (price != null) {
            params.put("price_int", String.valueOf(convertPriceAUDtoInt(price)));
        }
        params.put("amount_int", String.valueOf(convertVolumeBTCtoInt(volume)));

        Result<String> result = stringJSON.getResultFromStream(getMtGoxHTTPInputStream(ORDER_ADD_URL, params), String.class);
        if (result.getError() != null) {
            throw new RuntimeException(result.getToken() + ": " + result.getError());
        }
        return result.getReturn();
    }

    public String placeMarketOrder(OrderType orderType, double volume) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return placeOrder(orderType, null, volume);
    }

    public OrderResult getOrderResult(OrderType orderType, String orderRef) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        HashMap<String, String> params = new HashMap<>();
        if (orderType == OrderType.bid) {
            params.put("type", "bid");
        } else {
            params.put("type", "ask");
        }
        params.put("order", orderRef);

        Result<OrderResult> result = orderResultJSON.getResultFromStream(getMtGoxHTTPInputStream(ORDER_RESULT_URL, params), OrderResult.class);
        if (result.getError() != null) {
            throw new RuntimeException(result.getToken() + ": " + result.getError());
        }
        return result.getReturn();
    }

    public Order[] getOpenOrders() throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        Result<Order[]> openOrders = openOrdersJSON.getResultFromStream(getMtGoxHTTPInputStream(PRIVATE_ORDERS_URL), Order[].class);
        return openOrders.getReturn();
    }

    private InputStream getMtGoxHTTPInputStream(String path) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return getMtGoxHTTPInputStream(path, new HashMap<String, String>());
    }

    /*
     * This is based on an original idea by github user christopherobin See
     * https://gist.github.com/2396722
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

        URL queryUrl = new URL(MTGOX_HTTP_API_URL + path);
        connection = (HttpURLConnection) queryUrl.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; mtgox-java client)");
        connection.setRequestProperty("Rest-Key", apiKey);
        connection.setRequestProperty("Rest-Sign", signature);
        connection.getOutputStream().write(post_data.getBytes());
        return connection.getInputStream();
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