package to.sparks.mtgox.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jwebsocket.client.java.BaseWebSocket;
import org.springframework.core.task.TaskExecutor;
import to.sparks.mtgox.model.Depth;
import to.sparks.mtgox.model.Ticker;
import to.sparks.mtgox.net.MtGoxListener;
import to.sparks.mtgox.net.MtGoxListener;
import to.sparks.mtgox.net.MtGoxSocketListener;
import to.sparks.mtgox.net.MtGoxSocketListener;

/**
 *
 * @author SparksG
 */
public class MtGoxWebSocketClient implements MtGoxListener, Runnable {

    private Logger logger;
    private List<Depth> depthHistory = new CopyOnWriteArrayList<>();
    private List<Ticker> tickerHistory = new CopyOnWriteArrayList<>();
    final BaseWebSocket websocket = new BaseWebSocket();
    private TaskExecutor taskExecutor;

    public MtGoxWebSocketClient(Logger logger, TaskExecutor taskExecutor) {
        this.logger = logger;
        this.taskExecutor = taskExecutor;
    }

    public void init() {
        taskExecutor.execute(this);
    }

    public void destroy() {
        try {
            websocket.close();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Return ALL the depth items younger than the timestamp
     */
    public List<Depth> getAllDepthSince(long timestamp) {
        List<Depth> result = new ArrayList<>();
        int length = depthHistory.size();
        for (int i = 0; i < length; i++) {
            Depth depth = depthHistory.get(i);
            if (depth != null && depth.getStamp() > timestamp) {
                result = depthHistory.subList(i, length - 1);
                break;
            }
        }
        return result;
    }

    @Override
    public void tickerEvent(Ticker ticker) {
        tickerHistory.add(ticker);
    }

    @Override
    public void depthEvent(Depth depth) {
        depthHistory.add(depth);
    }

    public List<Depth> getDepthHistory() {
        return depthHistory;
    }

    public List<Ticker> getTickerHistory() {
        return tickerHistory;
    }

    @Override
    public void run() {
        try {

            websocket.addListener(new MtGoxSocketListener(logger, this));
            websocket.open("ws://websocket.mtgox.com/mtgox");
            logger.info("WS API started.");

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }

    }
}
