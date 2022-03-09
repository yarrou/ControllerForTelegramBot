package site.alexkononsol.controllerfortelegrambot.logHelper;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import site.alexkononsol.controllerfortelegrambot.utils.FileUtils;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class LogHelper {
    public static final String TAG = "ControllerForTelegramBot";
    private static final String LOG_NAME = "programLog.txt";

    public static String getLogDirPath(Context context) throws IOException {
        return context.getExternalFilesDir(null).getCanonicalPath();
    }

    public static String getLogFilePath(Context context) throws IOException {
        return getLogDirPath(context) + "/" + LOG_NAME;
    }

    public static void logError(Object object, String message, Throwable e) {
        Log.e(TAG, message, e);
        if (SettingsManager.getSettings().isLogging()) {
            FileUtils.writeLog(object, object.toString() + " : " + message + " - " + e.getCause());
        }
    }

    public static void logError(Object object, String message) {
        Log.e(TAG, message);
        if (SettingsManager.getSettings().isLogging()) {
            FileUtils.writeLog(object, object.toString() + " : " + message);
        }
    }

    public static void logDebug(Object object, String message) {
        Log.d(TAG, message);
        if (SettingsManager.getSettings().isLogging()) {
            FileUtils.writeLog(object, object.toString() + " : " + message);
        }
    }
}
