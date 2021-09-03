package site.alexkononsol.controllerfortelegrambot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import site.alexkononsol.controllerfortelegrambot.utils.Constants;

public class HelpActivity extends AppCompatActivity {
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        settings = getSharedPreferences(Constants.SHARED_PREFERENCES_SETTINGS, MODE_PRIVATE);
        String textSize = settings.getString(Constants.TEXT_SIZE,"normal");
        TextView textAbout = (TextView) findViewById(R.id.textAbout);
        if(textSize.equals("large")){
            textAbout.setTextSize(32);
        }else if (textSize.equals("small")){
            textAbout.setTextSize(20);
        }else textAbout.setTextSize(26);
    }
}