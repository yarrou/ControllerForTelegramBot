package site.alexkononsol.controllerfortelegrambot.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import site.alexkononsol.controllerfortelegrambot.HelpActivity;
import site.alexkononsol.controllerfortelegrambot.MainActivity;
import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ContentUrlProvider;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.RequestEncoder;
import site.alexkononsol.controllerfortelegrambot.utils.BackupHelper;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.FileUtils;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class SettingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //Menu of Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_settings:
                onSaveSetting(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSaveSetting(View view) {
        EditText editText = (EditText) findViewById(R.id.hostName);
        String host = editText.getText().toString();
        SettingsManager.getSettings().setHostName(host);
        SettingsManager.save();
        String toastTextSavedSettings = getString(R.string.saveSettingsToast);
        Toast.makeText(this, toastTextSavedSettings, Toast.LENGTH_SHORT).show();
    }

    public void onSettingsHelpInfo(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    public void onTextSizeButton(View view) {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioTextSize);
        int id = radioGroup.getCheckedRadioButtonId();
        String textSize = "normal";
        switch (id) {
            case R.id.textSizeLargeRadio:
                textSize = "large";
                break;
            case R.id.textSizeSmallRadio:
                textSize = "small";
        }
        SettingsManager.getSettings().setTextSize(textSize);
        SettingsManager.save();
    }

    public void onViewHelpOnStart(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            SettingsManager.getSettings().setViewHelpOnStart(true);
        } else {
            SettingsManager.getSettings().setViewHelpOnStart(false);
        }
        SettingsManager.save();
    }

    public void onSaveBacup(View view) {
        // write on SD card file data in the text box
        try {
            String backupPath = BackupHelper.createBackup("null");
            Toast.makeText(getBaseContext(),getString(R.string.bacupToadstSuccessfully) + backupPath,
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private static final int FILE_SELECT_CODE = 1;

    public void onShowFileChooser(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    try {
                        SettingsManager.restoreSettings(FileUtils.convertStreamToString(getContentResolver().openInputStream(uri)));
                        interfaceView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        interfaceView();
    }


    private void interfaceView() {
        EditText editText = (EditText) findViewById(R.id.hostName);
        String hostName = SettingsManager.getSettings().getHostName() == null ? Constants.DEFAULT_HOST_NAME : SettingsManager.getSettings().getHostName();
        editText.setHint(hostName);
        TextView nameBotView = (TextView) findViewById(R.id.nameBot);
        /*if(!hostName.equals(Constants.DEFAULT_HOST_NAME)){
            nameBotView.setText(getNameBot());
        }*/
        Button testButton = (Button) findViewById(R.id.buttonSettingsGetNameBot);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {

                        String content = getNameBot();
                        nameBotView.post(new Runnable() {
                            public void run() {
                                nameBotView.setText(content);
                            }
                        });

                    }
                }).start();
            }
        });

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioTextSize);
        int id = radioGroup.getCheckedRadioButtonId();

        String textSize = SettingsManager.getSettings().getTextSize();
        int idRadioTextSize = 1;
        switch (textSize) {
            case "small":
                idRadioTextSize = 0;
                break;
            case "large":
                idRadioTextSize = 2;
                break;
        }

        RadioButton savedCheckedRadioButton = (RadioButton) radioGroup.getChildAt(idRadioTextSize);
        savedCheckedRadioButton.setChecked(true);

        boolean viewHelpOnStart = SettingsManager.getSettings().isViewHelpOnStart();
        CheckBox helpOnStart = (CheckBox) findViewById(R.id.viewHelpOnStart);
        helpOnStart.setChecked(viewHelpOnStart);
    }

    private String getNameBot(){
        String request = ((TextView) findViewById(R.id.hostName)).getText().toString();
        if(request.equals("")){
            request = ((TextView) findViewById(R.id.hostName)).getHint().toString();
        }
        String content = null;
        try{
            content = ContentUrlProvider.getContentNameBot(request + "/name");
        }
        catch (IOException ex){
                    content = getString(R.string.nameBotNotAnswer);
        }
        return content;
    }
}