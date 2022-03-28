package site.alexkononsol.controllerfortelegrambot.ui.requestingPermission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.ui.fragments.PermissionButtonFragment;

public class RequestingPermissionsActivity extends AppCompatActivity implements PermissionButtonFragment.Listener {

    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requed_permissions);
    }

    @Override
    public void grantPermission() {
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
    }

    public void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LogHelper.logDebug(this,"Storage Permission Allow");
                finish();
                //Toast.makeText(SettingActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                LogHelper.logDebug(this,"Storage Permission Denied");
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}