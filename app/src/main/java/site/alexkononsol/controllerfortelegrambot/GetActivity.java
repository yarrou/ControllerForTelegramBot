package site.alexkononsol.controllerfortelegrambot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ContentUrlProvider;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.RequestEncoder;

public class GetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        TextView contentView = (TextView) findViewById(R.id.getResponse);
        Button getButton = (Button)findViewById(R.id.buttonGet);
        String host = getSharedPreferences(Constants.SHARED_PREFERENCES_SETTINGS,0).getString(Constants.HOST_NAME, Constants.DEFAULT_HOST_NAME);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentView.setText("Загрузка...");
                new Thread(new Runnable() {
                    public void run() {
                        try{
                            TextView getTextView = (TextView) findViewById(R.id.getRequest);
                            String request = getTextView.getText().toString();
                            String query = Constants.GET_PATH + RequestEncoder.getRequest(request);
                            String content = ContentUrlProvider.getContentGet(host + query);
                            contentView.post(new Runnable() {
                                public void run() {
                                    contentView.setText(content);
                                }
                            });
                        }
                        catch (IOException ex){
                            contentView.post(new Runnable() {
                                public void run() {
                                    contentView.setText("Ошибка: " + ex.getMessage()+ex.toString());
                                    Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

}