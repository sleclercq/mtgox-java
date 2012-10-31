package to.sparks;

import java.util.logging.Logger;
import to.sparks.mtgox.MTGOXAPI;

/**
 * Pass your MtGox API key and secret as the command line arguments.
 *
 */
public class App {

    public static void main(String[] args) {
        MTGOXAPI mtgox = new MTGOXAPI(Logger.getGlobal(), args[0], args[1]);

    }
}
