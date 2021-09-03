package site.alexkononsol.controllerfortelegrambot.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import site.alexkononsol.controllerfortelegrambot.HelpActivity;
import site.alexkononsol.controllerfortelegrambot.MainActivity;
import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;

public class SettingActivity extends AppCompatActivity {
    SharedPreferences settings ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settings = getSharedPreferences(Constants.SHARED_PREFERENCES_SETTINGS,0);
        EditText editText = (EditText) findViewById(R.id.hostName);
        editText.setHint(settings.getString(Constants.HOST_NAME, Constants.DEFAULT_HOST_NAME));

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioTextSize);
        int id = radioGroup.getCheckedRadioButtonId();
        SharedPreferences.Editor prefEditor = settings.edit();
        if(id == -1){
            String textSize = settings.getString(Constants.TEXT_SIZE,"normal");
            int idRadioTextSize = 1;
            switch (textSize){
                case "small": idRadioTextSize = 0;
                break;
                case "large": idRadioTextSize = 2;
                break;
            }
            RadioButton savedCheckedRadioButton = (RadioButton)radioGroup.getChildAt(idRadioTextSize);
            savedCheckedRadioButton.setChecked(true);
        }


    }
    public void onSaveSetting(View view){
        EditText editText = (EditText) findViewById(R.id.hostName);
        String host = editText.getText().toString();
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString(Constants.HOST_NAME, host);
        prefEditor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isSaveSettings",true);
        startActivity(intent);
    }

    public void onSettingsHelpInfo(View view){
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
    public void onTextSizeButton(View view){
        settings = getSharedPreferences(Constants.SHARED_PREFERENCES_SETTINGS,0);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioTextSize);
        int id = radioGroup.getCheckedRadioButtonId();
        SharedPreferences.Editor prefEditor = settings.edit();
        String textSize = "normal";
        switch (id){
            case R.id.textSizeLargeRadio: textSize ="large";
                break;
            case R.id.textSizeSmallRadio: textSize = "small";
        }
        prefEditor.putString(Constants.TEXT_SIZE,textSize);
        prefEditor.apply();
    }
}