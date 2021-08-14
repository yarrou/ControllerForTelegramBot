package site.alexkononsol.controllerfortelegrambot.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import site.alexkononsol.controllerfortelegrambot.MainActivity;
import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;

public class SettingActivity extends AppCompatActivity {
    SharedPreferences settings ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settings = getSharedPreferences(Constants.SHARED_PREFERENCES_SETTINGS,0);
        EditText editText = (EditText) findViewById(R.id.hostName);
        editText.setHint(settings.getString(Constants.HOST_NAME, Constants.DEFAULT_HOST_NAME));
    }
    public void onSaveSetting(View view){
        EditText editText = (EditText) findViewById(R.id.hostName);
        String host = editText.getText().toString();
        //settings = getSharedPreferences("settings",0);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString(Constants.HOST_NAME, host);
        prefEditor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}