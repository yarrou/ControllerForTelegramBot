package site.alexkononsol.controllerfortelegrambot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import site.alexkononsol.controllerfortelegrambot.settings.SettingActivity;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;

public class MainActivity extends AppCompatActivity {
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences(Constants.SHARED_PREFERENCES_SETTINGS, MODE_PRIVATE);
        /*if(!settings.contains("hostName")){
            SharedPreferences.Editor prefEditor = settings.edit();
            prefEditor.putString(Constans.HOST_NAME,Constans.DEFAULT_HOST_NAME );
            prefEditor.apply();
        }*/
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