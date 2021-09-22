package site.alexkononsol.controllerfortelegrambot;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ContentUrlProvider;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.RequestEncoder;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class DelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del);
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

}