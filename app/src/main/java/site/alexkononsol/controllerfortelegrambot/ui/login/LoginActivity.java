package site.alexkononsol.controllerfortelegrambot.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import site.alexkononsol.controllerfortelegrambot.MainActivity;
import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.LoginConnector;
import site.alexkononsol.controllerfortelegrambot.entity.result.LoginResult;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class LoginActivity extends AppCompatActivity {

    private TextView textViewLogin;
    private TextView textViewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume(){
        super.onResume();
        textViewLogin = (TextView) findViewById(R.id.username);
        textViewPassword = (TextView) findViewById(R.id.password);

    }

    public void onLogin(View view){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            String userName = textViewLogin.getText().toString();
            String hostPath = SettingsManager.getSettings().getHostName();
            LoginResult result = LoginConnector.loginRequest(userName,textViewPassword.getText().toString(),hostPath);

            handler.post(() -> {
                //UI Thread work here
                String output = null;
                if(result.getStatus()<200){
                    SettingsManager.getSettings().setUserName(userName);
                    SettingsManager.getSettings().setAuthToken(result.getMessage());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else Toast.makeText(getBaseContext(), result.getMessage(),
                        Toast.LENGTH_SHORT).show();
            });
        });
    }
    public void onContinue(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}