package site.alexkononsol.controllerfortelegrambot.ui.settings;

import static site.alexkononsol.controllerfortelegrambot.R.id.textSizeLargeRadio;
import static site.alexkononsol.controllerfortelegrambot.R.id.textSizeSmallRadio;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;

import java.io.File;

import site.alexkononsol.controllerfortelegrambot.BackupActivity;
import site.alexkononsol.controllerfortelegrambot.HelpActivity;
import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestToServer;
import site.alexkononsol.controllerfortelegrambot.ui.login.LoginActivity;
import site.alexkononsol.controllerfortelegrambot.utils.BackupHelper;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;
import site.alexkononsol.controllerfortelegrambot.utils.TextValidator;

public class SettingActivity extends AppCompatActivity {

    private ShareActionProvider shareActionProvider;
    private Button logoutButton;
    private TextView authInfo;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        authInfo = (TextView) findViewById(R.id.authSettingsStatus);
        logoutButton = (Button) findViewById(R.id.logoutButton) ;
        editText = (EditText) findViewById(R.id.hostName);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //Menu of Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        //!!!Attention!!! needs to be redone
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/ControllerForTelegramBot/backup.bp");
        setShareActionIntent(file);
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

    private void setShareActionIntent(File file) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareActionProvider.setShareIntent(intent);
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
            case textSizeLargeRadio:
                textSize = "large";
                break;
            case textSizeSmallRadio:
                textSize = "small";
        }
        SettingsManager.getSettings().setTextSize(textSize);
        SettingsManager.save();
    }

    public void onViewHelpOnStart(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        SettingsManager.getSettings().setViewHelpOnStart(checked);
        SettingsManager.save();
    }

    public void onSaveBackup(View view) {
        // write on SD card file data in the text box
        try {
            String backupPath = BackupHelper.createBackup("null");
            Toast.makeText(getBaseContext(), getString(R.string.backupToastSuccessfully) + backupPath,
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
                    Intent.createChooser(intent, getString(R.string.toastSelectFile)),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, getString(R.string.toastNeedInstallFileManager),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(this, BackupActivity.class);
                    intent.setData(data.getData());
                    startActivity(intent);
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

        //if the user is logged in , then his login is displayed in the settings
        viewInfoAboutAccount();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    new Thread(new Runnable() {
                        public void run() {
                            if(SettingsManager.getSettings().getUserName()!=null){
                                SettingsManager.getSettings().setAuthToken(null);
                                SettingsManager.getSettings().setUserName(null);
                                SettingsManager.save();

                                authInfo.post(new Runnable() {
                                    public void run() {
                                        authInfo.setText(getString(R.string.anonimous));
                                    }
                                });
                                logoutButton.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        logoutButton.setText(getString(R.string.sign_in_button_text));
                                    }
                                });
                            }else {
                                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }


                        }
                    }).start();
                }
        });




        String host = SettingsManager.getSettings().getHostName();
        if (host == null||host.equals("")) {
            editText.setHint(Constants.DEFAULT_HOST_URL);
        } else editText.setText(SettingsManager.getSettings().getHostName());
        TextView nameBotView = (TextView) findViewById(R.id.nameBot);
        Button testButton = (Button) findViewById(R.id.buttonSettingsGetNameBot);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView hostNameView = (TextView) findViewById(R.id.hostName);
                if (TextValidator.noEmptyValidation(hostNameView)) {  //checking for non-emptiness
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

    private String getNameBot() {
        String serverUrl = ((TextView) findViewById(R.id.hostName)).getText().toString();
        if (serverUrl.equals("")) {
            serverUrl = ((TextView) findViewById(R.id.hostName)).getHint().toString();
        }
        String content = null;
        try {
            RequestToServer request = new RequestToServer(serverUrl,Constants.ENDPOINT_BOT_NAME);
            request.addLangParam();
            content = request.send().getData();
        } catch (Exception ex) {
            content = getString(R.string.nameBotNotAnswer);
        }
        return content;
    }

    private void viewInfoAboutAccount(){

            if(SettingsManager.getSettings().getAuthToken()!=null){
                authInfo.setText(getString(R.string.authInfo) + SettingsManager.getSettings().getUserName());

            }else {
                authInfo.setText(getString(R.string.anonimous));
                logoutButton.setText(getString(R.string.sign_in_button_text));
            }

    }

}