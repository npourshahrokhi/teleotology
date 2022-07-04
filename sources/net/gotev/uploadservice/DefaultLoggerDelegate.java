package net.gotev.uploadservice;

import android.util.Log;
import net.gotev.uploadservice.Logger;

public class DefaultLoggerDelegate implements Logger.LoggerDelegate {
    private static final String TAG = "UploadService";

    public void error(String tag, String message) {
        Log.e(TAG, tag + " - " + message);
    }

    public void error(String tag, String message, Throwable exception) {
        Log.e(TAG, tag + " - " + message, exception);
    }

    public void debug(String tag, String message) {
        Log.d(TAG, tag + " - " + message);
    }

    public void info(String tag, String message) {
        Log.i(TAG, tag + " - " + message);
    }
}
