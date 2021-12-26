package site.alexkononsol.controllerfortelegrambot;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import site.alexkononsol.controllerfortelegrambot.entity.Settings;
import site.alexkononsol.controllerfortelegrambot.utils.FileUtils;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class BackupActivity extends AppCompatActivity {

    private Settings newSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView textView = (TextView)findViewById(R.id.textBackup);

        Intent intent = getIntent();
        String action = intent.getAction();

        if (action.compareTo(Intent.ACTION_VIEW) == 0) {
            String scheme = intent.getScheme();
            ContentResolver resolver = getContentResolver();

            if (scheme.compareTo(ContentResolver.SCHEME_CONTENT) == 0) {
                Uri uri = intent.getData();

                try {
                    newSettings = Settings.getSettingsFromString(FileUtils.convertStreamToString(getContentResolver().openInputStream(uri)));
                    TextView host = (TextView)findViewById(R.id.restoreSettingsHost);
                    host.setText(newSettings.getHostName());
                    TextView sizeText = (TextView)findViewById(R.id.restoreSettingsSizeText);
                    String size = null;
                    switch (newSettings.getTextSize()){
                        case ("small"):
                            size = getString(R.string.textSizeSmall);
                            break;
                        case ("normal"):
                            size = getString(R.string.textSizeNormal);
                            break;
                        case ("large"):
                            size = getString(R.string.textSizeLarge);
                            break;
                    }
                    sizeText.setText(size);
                    TextView showHelp = (TextView) findViewById(R.id.restoreSettingsAutoViewHelp);
                    if(newSettings.isViewHelpOnStart()){
                        showHelp.setText(getString(R.string.yes));
                    }else showHelp.setText(getString(R.string.no));

                } catch (Exception e) {
                    Toast.makeText(getBaseContext(),getString(R.string.error) +  e.getMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }
        }
    }

    public void onCancelRestoreSettings(View view){
        finish();
    }

    public void onConfirmRestoreSettings(View view){
        SettingsManager.restoreSettings(newSettings);
        Toast.makeText(getBaseContext(),getString(R.string.successfullyRestoreSettings),
                Toast.LENGTH_SHORT).show();

    }


}