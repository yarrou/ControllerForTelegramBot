package site.alexkononsol.controllerfortelegrambot;

import static java.lang.String.format;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.ui.requestingPermission.RequestingPermissionsActivity;
import site.alexkononsol.controllerfortelegrambot.ui.settings.SettingActivity;
import site.alexkononsol.controllerfortelegrambot.utils.DeviceTypeHelper;
import site.alexkononsol.controllerfortelegrambot.utils.FileUtils;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;
import site.alexkononsol.controllerfortelegrambot.utils.SharedPreferenceAssistant;

public class Splash_activity extends AppCompatActivity {
    private static boolean isFirstRun = true;
    private ShareActionProvider shareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            LogHelper.setLogDirPath(this.getExternalFilesDir(null).getCanonicalPath());
            Log.d(LogHelper.TAG,"dir path is " + this.getExternalFilesDir(null).getCanonicalPath());
        } catch (IOException e) {
            Log.e(LogHelper.TAG,"unable to get the path to the folder with the application files",e);
            e.printStackTrace();
        }
        if (!DeviceTypeHelper.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        showVersionApp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.greeting)).append("\n").append(getString(R.string.helpText));
        setShareActionIntent(stringBuilder.toString());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }

    private void runMainProcess() {
        File cashFolder = this.getCacheDir();
        FileUtils.cleanFiles(".bp", cashFolder);
        /* this part of the code is needed in order to be able to log in before the main window of the program is displayed
        Intent intent = null;
        if(SettingsManager.getSettings().getUserName()!=null){
            intent = new Intent(Splash_activity.this,MainActivity.class);
        }else intent = new Intent(Splash_activity.this, LoginActivity.class);*/
        Intent intent = new Intent(Splash_activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!checkStoragePermission()){
            Intent intent = new Intent(Splash_activity.this, RequestingPermissionsActivity.class);
            startActivity(intent);
        }else {
            if (isFirstRun) {
                int secondsDelayed = 1;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        isFirstRun = false;
                        SharedPreferenceAssistant.initSharedPreferences(Splash_activity.this);
                        SettingsManager.initSettings();
                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);
                        boolean viewHelpOnStart = SettingsManager.getSettings().isViewHelpOnStart();
                        if (viewHelpOnStart) {
                            Intent intent = new Intent(Splash_activity.this, HelpActivity.class);
                            startActivity(intent);
                        } else {
                            runMainProcess();
                        }
                    }
                }, secondsDelayed * 1000);
            } else {
                runMainProcess();
            }
        }
    }

    private void showVersionApp() {
        TextView versionTextView = (TextView) findViewById(R.id.version_text_view);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versionTextView.setText(format(getString(R.string.version), version));

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LogHelper.TAG,"the program version cannot be displayed",e);
            e.printStackTrace();
        }
    }
    private boolean checkStoragePermission() {
        Log.d(LogHelper.TAG,"checked permission");
        int permissionWriteExternal = ContextCompat.checkSelfPermission(Splash_activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.d(LogHelper.TAG,"permission is " + (permissionWriteExternal == PackageManager.PERMISSION_GRANTED));
        return permissionWriteExternal == PackageManager.PERMISSION_GRANTED;
    }
}