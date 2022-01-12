package site.alexkononsol.controllerfortelegrambot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import site.alexkononsol.controllerfortelegrambot.ui.login.LoginActivity;
import site.alexkononsol.controllerfortelegrambot.ui.settings.SettingActivity;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;
import site.alexkononsol.controllerfortelegrambot.utils.SharedPreferenceAssistant;

public class Splash_activity extends AppCompatActivity {
    private static boolean isFirstRun = true;
    private ShareActionProvider shareActionProvider;

    /*public Splash_activity() {
       if (BuildConfig.DEBUG) StrictMode.enableDefaults();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       //



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.greeting)).append("\n").append(getString(R.string.helpText));
        String text = getString(R.string.helpText);
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

    private void runMainProcess(){
        Intent intent = new Intent(Splash_activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstRun){
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
                    }else {
                        runMainProcess();}
                }
            }, secondsDelayed * 1000);
        }else {
            runMainProcess();
        }
    }
}