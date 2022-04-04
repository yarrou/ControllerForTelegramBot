package site.alexkononsol.controllerfortelegrambot.ui.settings;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;

import java.io.File;
import java.io.IOException;

import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.ui.login.LoginActivity;
import site.alexkononsol.controllerfortelegrambot.utils.BackupHelper;
import site.alexkononsol.controllerfortelegrambot.utils.DeviceTypeHelper;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class SettingActivity extends AppCompatActivity {

    private ShareActionProvider shareActionProvider;
    private Button logoutButton;
    private TextView authInfo;
    private EditText backupFileNameEditText;

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
            file = new File(BackupHelper.createTempBackup(SettingActivity.this));
        } catch (IOException e) {
            LogHelper.logError(this, e.getMessage(), e);
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
        String nameFile = backupFileNameEditText.getText().toString();
        if (nameFile != null && !nameFile.equals("")) {
            SettingsManager.getSettings().setBackupName(nameFile);
        }
        String host = ((EditText) findViewById(R.id.hostName)).getText().toString();//input field is in HostSettingsFragment
        SettingsManager.getSettings().setHostName(host);
        SettingsManager.save();
        String toastTextSavedSettings = getString(R.string.saveSettingsToast);
        Toast.makeText(this, toastTextSavedSettings, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        interfaceView();
    }


    private void interfaceView() {
        //viewNameBackup();
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
}