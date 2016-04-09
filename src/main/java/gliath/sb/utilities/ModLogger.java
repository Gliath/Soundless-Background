package gliath.sb.utilities;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModLogger {
    private static Logger logger = LogManager.getLogger("SoundlessBackground");

    private static void log(Level logLevel, Object obj) {
        logger.log(logLevel, String.valueOf(obj));
    }

    public static void all(Object obj)
    {
        log(Level.ALL, obj);
    }

    public static void trace(Object obj)
    {
        log(Level.TRACE, obj);
    }

    public static void debug(Object obj)
    {
        log(Level.DEBUG, obj);
    }

    public static void info(Object obj)
    {
        log(Level.INFO, obj);
    }

    public static void warn(Object obj)
    {
        log(Level.WARN, obj);
    }

    public static void error(Object obj)
    {
        log(Level.ERROR, obj);
    }

    public static void fetal(Object obj)
    {
        log(Level.FATAL, obj);
    }

    public static void off(Object obj)
    {
        log(Level.OFF, obj);
    }
}