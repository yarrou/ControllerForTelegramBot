package site.alexkononsol.controllerfortelegrambot;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        String textSize = SettingsManager.getSettings().getTextSize();
        TextView textAbout = (TextView) findViewById(R.id.textAbout);
        if(textSize.equals("large")){
            textAbout.setTextSize(32);
        }else if (textSize.equals("small")){
            textAbout.setTextSize(20);
        }else textAbout.setTextSize(26);
        textAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }
}