package site.alexkononsol.controllerfortelegrambot;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;

public class AppHelperService extends IntentService {

    private static final String LOG_ERROR = "site.alexkononsol.controllerfortelegrambot.action.LOG_ERROR";
    private static final String LOG_DEBUG = "site.alexkononsol.controllerfortelegrambot.action.LOG_DEBUG";

    private static final String EXTRA_PARAM1 = "site.alexkononsol.controllerfortelegrambot.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "site.alexkononsol.controllerfortelegrambot.extra.PARAM2";

    public AppHelperService() {
        super("AppHelperService");
    }


    public static void startActionLogError(Context context, String logMessage) {
        Intent intent = new Intent(context, AppHelperService.class);
        intent.setAction(LOG_ERROR);
        intent.putExtra(EXTRA_PARAM1, logMessage);
        intent.putExtra(EXTRA_PARAM2,context.getClass().getName());
        context.startService(intent);
    }
    public static void startActionLogDebug(Context context, String logMessage) {
        Intent intent = new Intent(context, AppHelperService.class);
        intent.setAction(LOG_DEBUG);
        intent.putExtra(EXTRA_PARAM1, logMessage);
        intent.putExtra(EXTRA_PARAM2,context.getClass().getName());
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (LOG_DEBUG.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionLogDebug(param1, param2);
            }else if(LOG_ERROR.equals(action)){
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionLogError(param1, param2);
            }
        }
    }

    private void handleActionLogDebug(String param1, String param2) {
        LogHelper logHelper = new LogHelper(this);
        logHelper.logDebug(param2,param1);
    }
    private void handleActionLogError(String param1, String param2) {
        LogHelper logHelper = new LogHelper(this);
        logHelper.logError(param2,param1);
    }
}