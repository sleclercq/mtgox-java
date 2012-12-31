package to.sparks.mtgox.model;

import java.util.Date;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author SparksG
 */
@JsonAutoDetect
public class AccountInfo extends DtoBase implements CurrencyKludge {

    private String login;
    private String[] rights;
    private Date last_Login;
    private Date created;
    private long index;
    private TickerPrice monthly_Volume;
    private String language;
    private String id;
    private DynaBean wallets;
    private double trade_Fee;

    public AccountInfo(@JsonProperty("Login") String login,
            @JsonProperty("Rights") String[] rights,
            @JsonProperty("Last_Login") Date last_Login,
            @JsonProperty("Created") Date created,
            @JsonProperty("Index") long index,
            @JsonProperty("Monthly_Volume") TickerPrice monthly_Volume,
            @JsonProperty("Language") String language,
            @JsonProperty("Id") String id,
            @JsonProperty("Wallets") DynaBean wallets,
            @JsonProperty("Trade_Fee") double trade_Fee) {
        this.login = login;
        this.rights = rights;
        this.last_Login = last_Login;
        this.created = created;
        this.index = index;
        this.monthly_Volume = monthly_Volume;
        this.language = language;
        this.id = id;
        this.wallets = wallets;
        this.trade_Fee = trade_Fee;
    }

    /*
     * This is a bit of a kludge that ensures the offers know what currency they
     * are in.
     */
    public void setCurrencyInfo(CurrencyInfo currencyInfo) {
        monthly_Volume.setCurrencyInfo(currencyInfo);
    }

    public String getLogin() {
        return login;
    }

    public String[] getRights() {
        return rights;
    }

    public Date getLast_Login() {
        return last_Login;
    }

    public Date getCreated() {
        return created;
    }

    public long getIndex() {
        return index;
    }

    public TickerPrice getMonthly_Volume() {
        return monthly_Volume;
    }

    public String getLanguage() {
        return language;
    }

    public String getId() {
        return id;
    }

    public DynaBean getWallets() {
        return wallets;
    }

    public double getTrade_Fee() {
        return trade_Fee;
    }
}
