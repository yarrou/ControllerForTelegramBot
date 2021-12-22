package site.alexkononsol.controllerfortelegrambot;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ContentUrlProvider;
import site.alexkononsol.controllerfortelegrambot.settings.SettingActivity;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.RequestEncoder;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class DelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView contentView = (TextView) findViewById(R.id.delResponse);
        Button delButton = (Button)findViewById(R.id.buttonDel);
        String host = SettingsManager.getSettings().getHostName();
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentView.setText("Загрузка...");
                new Thread(new Runnable() {
                    public void run() {
                        try{
                            TextView getTextView = (TextView) findViewById(R.id.delRequest);
                            String request = getTextView.getText().toString();
                            String query =  Constants.DEL_PATH + RequestEncoder.getRequest(request);
                            String content = ContentUrlProvider.getContentDel(host+query);
                            contentView.post(new Runnable() {
                                public void run() {
                                    contentView.setText(content);
                                }
                            });
                        }
                        catch (IOException ex){
                            contentView.post(new Runnable() {
                                public void run() {
                                    contentView.setText("Ошибка: " + ex.getMessage() + ex.getLocalizedMessage());
                                    Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
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