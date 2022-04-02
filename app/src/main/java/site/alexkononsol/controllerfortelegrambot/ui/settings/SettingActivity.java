package site.alexkononsol.controllerfortelegrambot.ui.settings;

import static site.alexkononsol.controllerfortelegrambot.R.id.textSizeLargeRadio;
import static site.alexkononsol.controllerfortelegrambot.R.id.textSizeSmallRadio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;

import java.io.File;
import java.io.IOException;

import site.alexkononsol.controllerfortelegrambot.BackupActivity;
import site.alexkononsol.controllerfortelegrambot.HelpActivity;
import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestToServer;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.ui.login.LoginActivity;
import site.alexkononsol.controllerfortelegrambot.utils.BackupHelper;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.DeviceTypeHelper;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;
import site.alexkononsol.controllerfortelegrambot.utils.TextValidator;

public class SettingActivity extends AppCompatActivity {

    private ShareActionProvider shareActionProvider;
    private Button logoutButton;
    private TextView authInfo;
    private String backupName;
    private EditText backupFileNameEditText;
    private String backupPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (!DeviceTypeHelper.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        authInfo = (TextView) findViewById(R.id.authSettingsStatus);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        backupFileNameEditText = (EditText) findViewById(R.id.backup_file_name_value);

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
        File file = null;
        try {
            file = new File(BackupHelper.createTempBackup(backupName, SettingActivity.this));
        } catch (IOException e) {
            LogHelper.logError(this,"don't create temp file",e);
            e.printStackTrace();
        }
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

    //saving settings
    public void onSaveSetting(View view) {
        String host = ((EditText)findViewById(R.id.hostName)).getText().toString();//input field is in HostSettingsFragment
        SettingsManager.getSettings().setHostName(host);
        SettingsManager.getSettings().setBackupName(backupFileNameEditText.getText().toString());
        SettingsManager.save();
        String toastTextSavedSettings = getString(R.string.saveSettingsToast);
        Toast.makeText(this, toastTextSavedSettings, Toast.LENGTH_SHORT).show();
    }

    public void onSaveBackup(View view) {
        // write on SD card file data in the text box
        String nameFile = backupFileNameEditText.getText().toString();
        try {
            backupPath = BackupHelper.createBackup(nameFile, this);
            LogHelper.logDebug(this,"backup created " + backupPath);
            Toast.makeText(getBaseContext(), getString(R.string.backupToastSuccessfully) + backupPath,
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            LogHelper.logError(this,e.getMessage(),e);
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
        viewNameBackup();
        //backupName = backupFileNameEditText.getText().toString();
        //if the user is logged in , then his login is displayed in the settings
        viewInfoAboutAccount();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                        if (SettingsManager.getSettings().getUserName() != null) {
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
                        } else {
                            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).start();
            }
        });
    }


    private void viewInfoAboutAccount() {
        if (SettingsManager.getSettings().getAuthToken() != null) {
            authInfo.setText(getString(R.string.authInfo) + SettingsManager.getSettings().getUserName());
        } else {
            authInfo.setText(getString(R.string.anonimous));
            logoutButton.setText(getString(R.string.sign_in_button_text));
        }
    }

    private void viewNameBackup() {
        backupName = SettingsManager.getSettings().getBackupName();
        if (backupName == null || backupName == "") {
            backupName = SettingsManager.getSettings().getHostName().split("://")[1].split("/")[0];
        }
        backupFileNameEditText.setText(backupName);
    }
}