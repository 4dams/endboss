package helpers;

/**
 * @author Juri Adams (@4dams)
 * @author Felix Rein
 * 
 * @version 1.0.0-Snapshot
 */
public class Logger {
    public static void println(String type, String message) {
        System.out.println(String.format("%-10s | %s", type, message));
    }

    public static void error(String message) {
        Logger.println("ERROR", message);
    }

    public static void info(String message) {
        Logger.println("INFO", message);
    }

    public static void debug(String message) {
        Logger.println("DEBUG", message);
    }

    public static void success(String message) {
        Logger.println("SUCCESS", message);
    }
}
