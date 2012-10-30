package to.sparks.dto;

//{
import org.codehaus.jackson.annotate.JsonProperty;

//    "result":"success",
//    "return":{
//        "order_id":"4b897241-1cf3-458d-a9e3-a52e7d32742f",
//        "trades":[{
//            "trade_id":"1348808659835758",
//            "primary":"Y",
//            "currency":"AUD",
//            "type":"bid",
//            "properties":"market,mixed_currency",
//            "item":"BTC",
//            "amount":{
//                "value":"0.10000000",
//                "value_int":"10000000",
//                "display":"0.10000000\u00a0BTC",
//                "display_short":"0.10\u00a0BTC",
//                "currency":"BTC"
//            },
//            "price":{
//                "value":"12.12777",
//                "value_int":"1212777",
//                "display":"AU$12.12777",
//                "display_short":"AU$12.13",
//                "currency":"AUD"
//            },
//            "spent":{
//                "value":"1.21278",
//                "value_int":"121278",
//                "display":"AU$1.21278",
//                "display_short":"AU$1.21",
//                "currency":"AUD"
//            },
//            "date":"2012-09-28 05:04:19"
//        }],
//        "total_amount":{
//            "value":"0.10000000",
//            "value_int":"10000000",
//            "display":"0.10000000\u00a0BTC",
//            "display_short":"0.10\u00a0BTC",
//            "currency":"BTC"
//        },
//        "total_spent":{
//            "value":"1.21278",
//            "value_int":"121278",
//            "display":"AU$1.21278",
//            "display_short":"AU$1.21",
//            "currency":"AUD"
//        },
//        "avg_cost":{
//            "value":"12.12777",
//            "value_int":"1212777",
//            "display":"AU$12.12777",
//            "display_short":"AU$12.13",
//            "currency":"AUD"
//        }
//    }
//}
/**
 *
 * @author SparksG
 */
public class OrderResult {

    private String order_id;
    private Trade[] trades;
    private TickerPrice total_amount;
    private TickerPrice total_spent;
    private TickerPrice avg_cost;

    public OrderResult(@JsonProperty("order_id") String order_id,
            @JsonProperty("trades") Trade[] trades,
            @JsonProperty("total_amount") TickerPrice total_amount,
            @JsonProperty("total_spent") TickerPrice total_spent,
            @JsonProperty("avg_cost") TickerPrice avg_cost) {

        this.order_id = order_id;
        this.trades = trades;
        this.total_amount = total_amount;
        this.total_spent = total_spent;
        this.avg_cost = avg_cost;

    }

    /**
     * @return the order_id
     */
    public String getOrderId() {
        return order_id;
    }

    /**
     * @return the trades
     */
    public Trade[] getTrades() {
        return trades;
    }

    /**
     * @return the total_amount
     */
    public TickerPrice getTotalAmount() {
        return total_amount;
    }

    /**
     * @return the total_spent
     */
    public TickerPrice getTotalSpent() {
        return total_spent;
    }

    /**
     * @return the avg_cost
     */
    public TickerPrice getAvgCost() {
        return avg_cost;
    }
}
