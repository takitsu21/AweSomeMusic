package bot;

import java.util.logging.Level;

public class Logger {
    private static final java.util.logging.Logger INSTANCE = java.util.logging.Logger.getLogger(Logger.class.getName());

    public static void info(String message) {
        INSTANCE.log(Level.INFO, message);
    }

    public static void debug(String message) {
        INSTANCE.log(Level.ALL, message);
    }

    public static void error(String message) {
        INSTANCE.log(Level.SEVERE, message);
    }

    public static java.util.logging.Logger getINSTANCE() {
        return INSTANCE;
    }
}
