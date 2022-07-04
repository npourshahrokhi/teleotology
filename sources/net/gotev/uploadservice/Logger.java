package net.gotev.uploadservice;

public class Logger {
    private LoggerDelegate mDelegate;
    private LogLevel mLogLevel;

    public enum LogLevel {
        DEBUG,
        INFO,
        ERROR,
        OFF
    }

    public interface LoggerDelegate {
        void debug(String str, String str2);

        void error(String str, String str2);

        void error(String str, String str2, Throwable th);

        void info(String str, String str2);
    }

    private Logger() {
        this.mLogLevel = LogLevel.OFF;
        this.mDelegate = new DefaultLoggerDelegate();
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final Logger instance = new Logger();

        private SingletonHolder() {
        }
    }

    public static void resetLoggerDelegate() {
        synchronized (Logger.class) {
            SingletonHolder.instance.mDelegate = new DefaultLoggerDelegate();
        }
    }

    public static void setLoggerDelegate(LoggerDelegate delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("delegate MUST not be null!");
        }
        synchronized (Logger.class) {
            SingletonHolder.instance.mDelegate = delegate;
        }
    }

    public static void setLogLevel(LogLevel level) {
        synchronized (Logger.class) {
            SingletonHolder.instance.mLogLevel = level;
        }
    }

    public static void error(String tag, String message) {
        if (SingletonHolder.instance.mLogLevel.compareTo(LogLevel.ERROR) <= 0) {
            SingletonHolder.instance.mDelegate.error(tag, message);
        }
    }

    public static void error(String tag, String message, Throwable exception) {
        if (SingletonHolder.instance.mLogLevel.compareTo(LogLevel.ERROR) <= 0) {
            SingletonHolder.instance.mDelegate.error(tag, message, exception);
        }
    }

    public static void info(String tag, String message) {
        if (SingletonHolder.instance.mLogLevel.compareTo(LogLevel.INFO) <= 0) {
            SingletonHolder.instance.mDelegate.info(tag, message);
        }
    }

    public static void debug(String tag, String message) {
        if (SingletonHolder.instance.mLogLevel.compareTo(LogLevel.DEBUG) <= 0) {
            SingletonHolder.instance.mDelegate.debug(tag, message);
        }
    }
}
