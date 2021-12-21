package site.alexkononsol.controllerfortelegrambot;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import site.alexkononsol.controllerfortelegrambot.settings.SettingActivity;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String textSize = SettingsManager.getSettings().getTextSize();
        TextView textAbout = (TextView) findViewById(R.id.textAbout);
        if(textSize.equals("large")){
            textAbout.setTextSize(32);
        }else if (textSize.equals("small")){
            textAbout.setTextSize(20);
        }else textAbout.setTextSize(26);
        textAbout.setMovementMethod(LinkMovementMethod.getInstance());
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
}