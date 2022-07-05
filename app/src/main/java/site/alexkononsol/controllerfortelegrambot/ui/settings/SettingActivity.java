package site.alexkononsol.controllerfortelegrambot.ui.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.IOException;

import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.ui.fragments.AccountSettingsFragment;
import site.alexkononsol.controllerfortelegrambot.utils.BackupHelper;
import site.alexkononsol.controllerfortelegrambot.utils.DeviceTypeHelper;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class SettingActivity extends AppCompatActivity {

    private ShareActionProvider shareActionProvider;
    private EditText backupFileNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (!DeviceTypeHelper.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        backupFileNameEditText = findViewById(R.id.backup_file_name_value);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    protected void onStart() {
        super.onStart();
        transactionFragment(new AccountSettingsFragment());
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

    @SuppressLint("NonConstantResourceId")
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
        if (!nameFile.equals("")) {
            SettingsManager.getSettings().setBackupName(nameFile);
        }
        String host = ((EditText) findViewById(R.id.hostName)).getText().toString();//input field is in HostSettingsFragment
        if(!SettingsManager.getSettings().getHostName().equals(host)){
            AccountSettingsFragment fragment = (AccountSettingsFragment) getSupportFragmentManager().findFragmentByTag("accountSettingsFragment");
            if (fragment != null) {
                fragment.signOut();
            }
        }
        SettingsManager.getSettings().setHostName(host);
        SettingsManager.save();
        String toastTextSavedSettings = getString(R.string.saveSettingsToast);
        Toast.makeText(this, toastTextSavedSettings, Toast.LENGTH_SHORT).show();
    }

    private void transactionFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.account_frame_layout, fragment,"accountSettingsFragment");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }


}