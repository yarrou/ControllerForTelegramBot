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
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;
import site.alexkononsol.controllerfortelegrambot.utils.TextValidator;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        TextView contentView = (TextView) findViewById(R.id.postResponse);
        Button getButton = (Button) findViewById(R.id.buttonPost);
        String host = SettingsManager.getSettings().getHostName();

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView cityNameView = (TextView) findViewById(R.id.postRequest);
                TextView cityDescriptionView = (TextView) findViewById(R.id.postRequestDescription);
                if (TextValidator.noEmptyValidation(cityNameView)&&TextValidator.noEmptyValidation(cityDescriptionView)) {
                    String cityName = cityNameView.getText().toString();
                    String cityDescription = cityDescriptionView.getText().toString();
                    contentView.setText(getString(R.string.toastLoading));
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                String content = ContentUrlProvider.getContentPost(host, cityName, cityDescription);
                                contentView.post(new Runnable() {
                                    public void run() {
                                        contentView.setText(content);
                                    }
                                });
                            } catch (IOException ex) {

                                contentView.post(new Runnable() {
                                    public void run() {
                                        contentView.setText(getString(R.string.error) + ": " + ex.getMessage());
                                        Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
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