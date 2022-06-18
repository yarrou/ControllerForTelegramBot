package site.alexkononsol.controllerfortelegrambot.ui.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestToServer;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestType;
import site.alexkononsol.controllerfortelegrambot.entity.RegistrationForm;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ServerResponse;
import site.alexkononsol.controllerfortelegrambot.ui.login.LoginActivity;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.DeviceTypeHelper;
import site.alexkononsol.controllerfortelegrambot.utils.RegistrationFormValidator;

public class RegistrationActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordRepeatEditText;
    private TextView resultRegistrationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if(!DeviceTypeHelper.isTablet(this)) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        usernameEditText = (EditText)findViewById(R.id.username_reg);
        passwordEditText =(EditText) findViewById(R.id.password_reg);
        passwordRepeatEditText = (EditText) findViewById(R.id.repeat_password);
        resultRegistrationView = (TextView) findViewById(R.id.resultRegistrationView);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        String login = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        if(login!=null) usernameEditText.setText(login);
        if(password!=null) passwordEditText.setText(password);

    }
    private RegistrationForm getRegistrationForm(){
        return new RegistrationForm(usernameEditText.getText().toString(),passwordEditText.getText().toString(),passwordRepeatEditText.getText().toString());
    }
    public void onRegistration(View view){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        RegistrationForm form = getRegistrationForm();
        executor.execute(() -> {
            //Background work here
            RegistrationFormValidator validator = new RegistrationFormValidator(this);
            ServerResponse result = validator.regFormValidate(form);
            if(result.getCode()==200){
                RequestToServer loginRequest = new RequestToServer(Constants.ENDPOINT_REGISTRATION, RequestType.POST);
                loginRequest.addLangParam();
                loginRequest.addJsonHeaders();
                loginRequest.setBody(form.getUserForm());
                result = loginRequest.send();
            }
            ServerResponse finalResult = result;
            handler.post(() -> {
                //UI Thread work here
                if(finalResult.getCode()==200){
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    intent.putExtra("messageSuccess",getString(R.string.registration_success_message));
                    startActivity(intent);
                    finish();
                }else resultRegistrationView.setText(finalResult.getData().toString());
            });
        });
    }
}