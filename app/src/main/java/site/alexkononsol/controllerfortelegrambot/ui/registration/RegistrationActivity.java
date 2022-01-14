package site.alexkononsol.controllerfortelegrambot.ui.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import site.alexkononsol.controllerfortelegrambot.R;

public class RegistrationActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordRepeatEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        usernameEditText = (EditText)findViewById(R.id.username_reg);
        passwordEditText =(EditText) findViewById(R.id.password_reg);
        passwordRepeatEditText = (EditText) findViewById(R.id.repeat_password);
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
}