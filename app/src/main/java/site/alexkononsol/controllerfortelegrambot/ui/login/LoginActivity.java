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
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.AuthConnector;
import site.alexkononsol.controllerfortelegrambot.entity.result.AuthResult;
import site.alexkononsol.controllerfortelegrambot.ui.registration.RegistrationActivity;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class LoginActivity extends AppCompatActivity {

    private TextView textViewLogin;
    private TextView textViewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textViewLogin = (TextView) findViewById(R.id.username);
        textViewPassword = (TextView)findViewById(R.id.password);

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public void onLogin(View view){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            String userName = textViewLogin.getText().toString();
            String hostPath = SettingsManager.getSettings().getHostName();
            AuthResult result = AuthConnector.authRequest(userName,textViewPassword.getText().toString(), Constants.ENDPOINT_LOGIN);

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
    public void onRegistration(View view){
        String login = textViewLogin.getText().toString();
        String password = textViewPassword.getText().toString();
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        intent.putExtra("username",login);
        intent.putExtra("password",password);
        startActivity(intent);
    }
}