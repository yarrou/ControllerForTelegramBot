package site.alexkononsol.controllerfortelegrambot;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import site.alexkononsol.controllerfortelegrambot.settings.SettingActivity;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;
import site.alexkononsol.controllerfortelegrambot.utils.SharedPreferenceAssistant;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferenceAssistant.initSharedPreferences(this);
        SettingsManager.initSettings();
        boolean viewHelpOnStart =  SettingsManager.getSettings().isViewHelpOnStart();
        if(viewHelpOnStart){
            Intent intent = new Intent(this,HelpActivity.class);
            startActivity(intent);
        }
        boolean isSaveSettings = getIntent().getBooleanExtra("isSaveSettings",false);
        if(isSaveSettings){
            String toastTextSavedSettings = getString(R.string.saveSettingsToast);
            Toast.makeText(this, toastTextSavedSettings, Toast.LENGTH_SHORT).show();
        }
        String isSavedHost = SettingsManager.getSettings().getHostName();
        if(isSavedHost == null){
            String toastTextNotHost = getString(R.string.toastTextNotHost);
            Toast.makeText(this,toastTextNotHost , Toast.LENGTH_SHORT).show();
        }
    }


    //Menu of Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onSearch(View view){
        Intent intent = new Intent(this,SearchActivity.class);
        startActivity(intent);
    }
}