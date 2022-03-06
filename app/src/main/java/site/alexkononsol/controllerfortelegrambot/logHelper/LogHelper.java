package site.alexkononsol.controllerfortelegrambot.logHelper;

import android.util.Log;

import site.alexkononsol.controllerfortelegrambot.utils.FileUtils;

public class LogHelper {
    public static final String TAG = "ControllerForTelegramBot";
    public static void logError(Object object,String message,Throwable e){
        Log.e(TAG,message,e);
        FileUtils.writeLog(object,object.toString() + " : " + message + " - " + e.getCause());
    }
    public static void logError(Object object, String message){
        Log.e(TAG,message);
        FileUtils.writeLog(object,object.toString() + " : " + message);
    }
    public static void logDebug(Object object, String message){
        Log.d(TAG,message);
        FileUtils.writeLog(object,object.toString() + " : " + message );
    }
}
