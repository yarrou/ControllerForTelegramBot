package site.alexkononsol.controllerfortelegrambot.logHelper;


import android.util.Log;

import java.io.File;


import site.alexkononsol.controllerfortelegrambot.utils.FileUtils;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class LogHelper {
    public static final String TAG = "ControllerForTelegramBot";
    private static final String LOG_NAME = "programLog.txt";
    private static String logDirPath ;

    public static String getLogDirPath() {
        return logDirPath;
    }
    public static void setLogDirPath(String logDirPath) {
        LogHelper.logDirPath = logDirPath;
    }


    public static String getLogFilePath(){
        return getLogDirPath() + "/" + LOG_NAME;
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

    public static String getSizeLogFile(){
        File file = new File(getLogFilePath());
        long bytes = file.exists()?file.length():0;
        long kb = (bytes / 1024);
        long mb = (kb / 1024);
        if(mb>0) return mb + " mb";
        else if(kb > 0) return kb + " kb";
        else return bytes + " b";
    }
}
