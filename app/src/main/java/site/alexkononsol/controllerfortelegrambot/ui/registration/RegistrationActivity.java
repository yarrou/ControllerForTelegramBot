package site.alexkononsol.controllerfortelegrambot.ui.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import site.alexkononsol.controllerfortelegrambot.MainActivity;
import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.AuthConnector;
import site.alexkononsol.controllerfortelegrambot.entity.RegistrationForm;
import site.alexkononsol.controllerfortelegrambot.entity.result.AuthResult;
import site.alexkononsol.controllerfortelegrambot.ui.login.LoginActivity;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.RegistrationFormValidator;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class RegistrationActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordRepeatEditText;
    private TextView resultRegistrationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
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
        RegistrationForm form = new RegistrationForm(usernameEditText.getText().toString(),passwordEditText.getText().toString(),passwordRepeatEditText.getText().toString());
        return form;
    }
    public void onRegistration(View view){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        RegistrationForm form = getRegistrationForm();
        executor.execute(() -> {
            //Background work here
            RegistrationFormValidator validator = new RegistrationFormValidator(this);
            AuthResult result = validator.regFormValidate(form);
            if(result.getStatus()<200){
                result =new AuthConnector(this).authRequest(form.getUserLogin(),form.getPassword(), Constants.ENDPOINT_REGISTRATION);
            }
            AuthResult finalResult = result;
            handler.post(() -> {
                //UI Thread work here
                if(finalResult.getStatus()<200){
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    intent.putExtra("messageSuccess",getString(R.string.registration_success_message));
                    startActivity(intent);
                    finish();
                }else resultRegistrationView.setText(finalResult.getMessage());//Toast.makeText(getBaseContext(), finalResult.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }
}