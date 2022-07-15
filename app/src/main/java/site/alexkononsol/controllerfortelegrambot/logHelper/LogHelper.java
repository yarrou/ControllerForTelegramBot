package site.alexkononsol.controllerfortelegrambot.logHelper;


import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;


import site.alexkononsol.controllerfortelegrambot.utils.FileUtils;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class LogHelper {
    public static final String TAG = "ControllerForTelegramBot";
    public static final String LOG_NAME = "programLog.txt";
    private String logDirPath;

    public LogHelper(Context context) {

        try {
            logDirPath = context.getExternalFilesDir(null).getCanonicalPath();
            if (SettingsManager.getSettings().isLogging()) {
                File programDir = new File(logDirPath);
                if (!programDir.exists()) {
                    Log.d("DEBUG", "created log dir");
                    programDir.mkdir();
                }
                File myFile = new File(getLogFilePath());
                if (!myFile.exists()) {
                    myFile.createNewFile();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getLogFilePath() {
        return logDirPath + "/" + LOG_NAME;
    }

    public void logError(String className, String message) {
        Log.e(TAG,className + " : " + message);
        if (SettingsManager.getSettings().isLogging()) {
            FileUtils.writeLog(getLogFilePath(), className + " : " + message);
        }
    }

    public void logDebug(String className, String message) {
        Log.d(TAG, message);
        if (SettingsManager.getSettings().isLogging()) {
            FileUtils.writeLog(getLogFilePath(), className + " : " + message);
        }
    }

    public String getSizeLogFile() {
        File file = new File(getLogFilePath());
        long bytes = file.exists() ? file.length() : 0;
        long kb = (bytes / 1024);
        long mb = (kb / 1024);
        if (mb > 0) return mb + " mb";
        else if (kb > 0) return kb + " kb";
        else return bytes + " b";
    }
}
