package to.sparks.mtgox;

import biz.source_code.base64Coder.Base64Coder;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import to.sparks.mtgox.dto.Order;
import to.sparks.mtgox.dto.OrderResult;
import to.sparks.mtgox.dto.Result;
import to.sparks.mtgox.util.JSONSource;

/**
 *
 * @author SparksG
 */
public class MTGOXAPI {

    public static double AUD_INT_MULTIPLIER = 100000.0D;
    public static double BTC_VOL_INT_MULTIPLIER = 100000000.0D;

    public enum OrderType {

        bid, ask
    }
    private String apiKey;
    private String secret;
    private Logger log;
    private JSONSource<Result<Order[]>> openOrdersJSON;
    private JSONSource<Result<String>> stringJSON;
    private JSONSource<Result<OrderResult>> orderResultJSON;

    public MTGOXAPI(Logger log, String apiKey, String secret) {
        this.log = log;
        this.apiKey = apiKey;
        this.secret = secret;
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

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

            log.log(Level.SEVERE, null, e);
        }

        openOrdersJSON = new JSONSource<>();
        stringJSON = new JSONSource<>();
        orderResultJSON = new JSONSource<>();
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

        Result<String> result = stringJSON.getResultFromStream(query("1/BTCAUD/private/order/add", params), String.class);
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

        Result<OrderResult> result = orderResultJSON.getResultFromStream(query("1/generic/private/order/result", params), OrderResult.class);
        if (result.getError() != null) {
            throw new RuntimeException(result.getToken() + ": " + result.getError());
        }
        return result.getReturn();
    }

    public Order[] getOpenOrders() throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        Result<Order[]> openOrders = openOrdersJSON.getResultFromStream(query("1/generic/private/orders"), Order[].class);
        return openOrders.getReturn();
    }

    private InputStream query(String path) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return query(path, new HashMap<String, String>());
    }

    private InputStream query(String path, HashMap<String, String> args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        HttpURLConnection connection;

        // add nonce and build arg list
        args.put("nonce", String.valueOf(System.currentTimeMillis()));
        String post_data = this.buildQueryString(args);

        // args signature
        Mac mac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secret_spec = new SecretKeySpec(Base64Coder.decode(this.secret), "HmacSHA512");
        mac.init(secret_spec);
        String signature = new String(Base64Coder.encode(mac.doFinal(post_data.getBytes())));

        System.setProperty("jsse.enableSNIExtension", "false");

        // build URL
        URL queryUrl = new URL("https://mtgox.com/api/" + path);

        // create connection
        connection = (HttpURLConnection) queryUrl.openConnection();
        connection.setDoOutput(true);
        // set signature
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; Java Test client)");
        connection.setRequestProperty("Rest-Key", apiKey);
        connection.setRequestProperty("Rest-Sign", signature.replaceAll("\n", ""));

        // write post
        connection.getOutputStream().write(post_data.getBytes());

        return connection.getInputStream();
    }

    private String buildQueryString(HashMap<String, String> args) {
        String result = new String();
        for (String hashkey : args.keySet()) {
            if (result.length() > 0) {
                result += '&';
            }
            try {
                result += URLEncoder.encode(hashkey, "UTF-8") + "="
                        + URLEncoder.encode(args.get(hashkey), "UTF-8");
            } catch (Exception ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
