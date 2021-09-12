package site.alexkononsol.controllerfortelegrambot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import site.alexkononsol.controllerfortelegrambot.settings.SettingActivity;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;

public class MainActivity extends AppCompatActivity {
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences(Constants.SHARED_PREFERENCES_SETTINGS, MODE_PRIVATE);
        boolean viewHelpOnStart =  settings.getBoolean(Constants.VIEW_HELP_ON_START, true);
        if(viewHelpOnStart){
            Intent intent = new Intent(this,HelpActivity.class);
            startActivity(intent);
        }
        boolean isSaveSettings = getIntent().getBooleanExtra("isSaveSettings",false);
        if(isSaveSettings){
            String toastTextSavedSettings = getString(R.string.saveSettingsToast);
            Toast.makeText(this, toastTextSavedSettings, Toast.LENGTH_SHORT).show();
        }
        String isSavedHost = settings.getString(Constants.HOST_NAME,"null");
        if(isSavedHost.equals("null")){
            String toastTextNotHost = getString(R.string.toastTextNotHost);
            Toast.makeText(this,toastTextNotHost , Toast.LENGTH_SHORT).show();
        }
    }
    public void onGetRequest(View view){
        Intent intent = new Intent(this,GetActivity.class);
        startActivity(intent);
    }
    public void onPostRequest(View view){
        Intent intent = new Intent(this,PostActivity.class);
        startActivity(intent);
    }
    public void onPutRequest(View view){
        Intent intent = new Intent(this,PutActivity.class);
        startActivity(intent);
    }
    public void onDelRequest(View view){
        Intent intent = new Intent(this, DelActivity.class);
        startActivity(intent);
    }
    public void onSettings(View view){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
}