package to.sparks.mtgox.impl;

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
import to.sparks.mtgox.MtGoxAPI;
import to.sparks.mtgox.dto.*;
import to.sparks.mtgox.util.JSONSource;
import to.sparks.mtgox.util.MtGoxRealTime;
import to.sparks.mtgox.util.MtGoxUrlFactory;

/**
 * All MtGox API interactions (both HTTP and Websocket) are handled by this
 * class.
 *
 * @author SparksG
 */
public class MtGoxApiImpl implements MtGoxAPI {

    // TODO:  This value is currency dependent.  JPY is different from USD for example.
    public static double USD_INT_MULTIPLIER = 100000000.0D;
    public static double AUD_INT_MULTIPLIER = 100000.0D;
    public static double BTC_VOL_INT_MULTIPLIER = 100000000.0D;
    private String apiKey;
    private String secret;
    private static Logger logger;
    private JSONSource<Result<Order[]>> openOrdersJSON;
    private JSONSource<Result<String>> stringJSON;
    private JSONSource<Result<OrderResult>> orderResultJSON;
    private JSONSource<Result<FullDepth>> fullDepthJSON;
    private JSONSource<Result<Ticker>> tickerJSON;
    private static MtGoxRealTime mtGoxRealTime;
    private Currency currency;

    public MtGoxApiImpl(final Logger logger, Currency currency, String apiKey, String secret) {
        this.logger = logger;
        this.apiKey = apiKey;
        this.secret = secret;
        this.currency = currency;
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
        tickerJSON = new JSONSource<>();


        Thread t = new Thread() {

            public void run() {
                try {
                    mtGoxRealTime = new MtGoxRealTime(getFullDepth());
                } catch (WebSocketException | IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        };
        t.start();

    }

    @Override
    public List<Offer> getRealtimeAsks() {
        return mtGoxRealTime != null ? mtGoxRealTime.getAsks() : null;
    }

    @Override
    public List<Offer> getRealtimeBids() {
        return mtGoxRealTime != null ? mtGoxRealTime.getBids() : null;
    }

    @Override
    public FullDepth getFullDepth() throws IOException, Exception {
        return fullDepthJSON.getResultFromStream(new URL(MtGoxUrlFactory.getUrlForRestCommand(currency, MtGoxUrlFactory.RestCommand.FullDepth)).openStream(), FullDepth.class).getReturn();
    }

    private static int convertVolumeBTCtoInt(double d) {
        double total = d * BTC_VOL_INT_MULTIPLIER;
        return (int) total;
    }

    private static int convertPricetoInt(String currencyCode, double d) {
        double multiplier;
        // TODO:  Verify what the correct multiplier is for each currency
        switch (currencyCode.toLowerCase()) {
            case "aud":
                multiplier = AUD_INT_MULTIPLIER;
                break;
            default:
                multiplier = USD_INT_MULTIPLIER;
                break;
        }
        double total = d * multiplier;
        return (int) total;
    }

    @Override
    public String placeOrder(OrderType orderType, Double price, double volume) throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        HashMap<String, String> params = new HashMap<>();
        if (orderType == OrderType.Bid) {
            params.put("type", "bid");
        } else {
            params.put("type", "ask");
        }
        if (price != null) {
            params.put("price_int", String.valueOf(convertPricetoInt(currency.getCurrencyCode(), price)));
        }
        params.put("amount_int", String.valueOf(convertVolumeBTCtoInt(volume)));

        Result<String> result = stringJSON.getResultFromStream(getMtGoxHTTPInputStream(MtGoxUrlFactory.getUrlForRestCommand(currency, MtGoxUrlFactory.RestCommand.PrivateOrderAdd), params), String.class);
        if (result.getError() != null) {
            throw new RuntimeException(result.getToken() + ": " + result.getError());
        }
        return result.getReturn();
    }

    @Override
    public String placeMarketOrder(OrderType orderType, double volume) throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        return placeOrder(orderType, null, volume);
    }

    @Override
    public OrderResult getOrderResult(OrderType orderType, String orderRef) throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {
        HashMap<String, String> params = new HashMap<>();
        if (orderType == OrderType.Bid) {
            params.put("type", "bid");
        } else {
            params.put("type", "ask");
        }
        params.put("order", orderRef);

        Result<OrderResult> result = orderResultJSON.getResultFromStream(getMtGoxHTTPInputStream(MtGoxUrlFactory.getUrlForRestCommand(null, MtGoxUrlFactory.RestCommand.PrivateOrderResult), params), OrderResult.class);
        if (result.getError() != null) {
            throw new RuntimeException(result.getToken() + ": " + result.getError());
        }
        return result.getReturn();
    }

    @Override
    public Order[] getOpenOrders() throws IOException, NoSuchAlgorithmException, InvalidKeyException, Exception {

        Result<Order[]> openOrders = openOrdersJSON.getResultFromStream(getMtGoxHTTPInputStream(MtGoxUrlFactory.getUrlForRestCommand(null, MtGoxUrlFactory.RestCommand.PrivateOrders)), Order[].class);
        return openOrders.getReturn();
    }

    @Override
    public Ticker getTicker() throws IOException, Exception {
        Result<Ticker> tickerUSD = tickerJSON.getResultFromStream(new URL(MtGoxUrlFactory.getUrlForRestCommand(currency, MtGoxUrlFactory.RestCommand.Ticker)).openStream(), Ticker.class);
        return tickerUSD.getReturn();
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

        URL queryUrl = new URL(path);
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