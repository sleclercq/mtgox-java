package to.sparks.mtgox.dto;

import org.codehaus.jackson.annotate.JsonProperty;

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
