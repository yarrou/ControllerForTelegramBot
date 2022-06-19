package site.alexkononsol.controllerfortelegrambot.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import site.alexkononsol.controllerfortelegrambot.MainActivity;
import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ServerResponse;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestToServer;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestType;
import site.alexkononsol.controllerfortelegrambot.entity.UserForm;
import site.alexkononsol.controllerfortelegrambot.ui.registration.RegistrationActivity;
import site.alexkononsol.controllerfortelegrambot.utils.DeviceTypeHelper;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class LoginActivity extends AppCompatActivity {

    private TextView textViewLogin;
    private TextView textViewPassword;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(!DeviceTypeHelper.isTablet(this)) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textViewLogin = (TextView) findViewById(R.id.username);
        textViewPassword = (TextView)findViewById(R.id.password);
        textViewResult = (TextView) findViewById(R.id.resultOnLoginView);

    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        String message = intent.getStringExtra("messageSuccess");
        if (message!=null){
            textViewResult.post(() -> textViewResult.setText(message));
        }
    }

    public void onLogin(View view){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            String userName = textViewLogin.getText().toString();
            UserForm userForm = new UserForm(userName, textViewPassword.getText().toString());
            /*RequestToServer loginRequest = new RequestToServer(Constants.ENDPOINT_LOGIN, RequestType.POST);
            loginRequest.addAuthHeader();
            loginRequest.addLangParam();
            loginRequest.addJsonHeaders();
            loginRequest.setBody(userForm);*/
            RetrofitRequestToServer requestToServer = new RetrofitRequestToServer();

            ServerResponse response = requestToServer.loginOrRegistration(userForm, RetrofitRequestType.LOGIN);//loginRequest.send();

            handler.post(() -> {
                //UI Thread work here
                String output = null;
                if(response.getCode()==200){
                    SettingsManager.getSettings().setUserName(userName);
                    SettingsManager.getSettings().setAuthToken(response.getData().toString());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else textViewResult.setText(response.getData().toString());
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