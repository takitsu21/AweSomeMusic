import java.util.logging.Level;

public class Logger {
    public static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Logger.class.getName());

    public static void info(String message) {
        logger.log(Level.INFO, message);
    }

    public static void debug(String message) {
        logger.log(Level.ALL, message);
    }

    public static void error(String message) {
        logger.log(Level.SEVERE, message);
    }
}
