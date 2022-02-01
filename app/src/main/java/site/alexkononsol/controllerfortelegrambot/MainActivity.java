package site.alexkononsol.controllerfortelegrambot;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import site.alexkononsol.controllerfortelegrambot.ui.settings.SettingActivity;
import site.alexkononsol.controllerfortelegrambot.utils.DeviceTypeHelper;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class MainActivity extends AppCompatActivity {

    private ShareActionProvider shareActionProvider;
    private Button putButton;
    private Button postButton;
    private Button delButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!DeviceTypeHelper.isTablet(this)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String isSavedHost = SettingsManager.getSettings().getHostName();
        if(isSavedHost == null){
            String toastTextNotHost = getString(R.string.toastTextNotHost);
            Toast.makeText(this,toastTextNotHost , Toast.LENGTH_SHORT).show();
        }

        putButton = (Button) findViewById(R.id.buttonPut);
        postButton = (Button) findViewById(R.id.buttonPost);
        delButton = (Button) findViewById(R.id.buttonDel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userLogin = SettingsManager.getSettings().getUserName();
        //if the user is not logged in to the account, the buttons for adding, changing and deleting information in the database are not active
        if(userLogin==null){
            putButton.setEnabled(false);
            postButton.setEnabled(false);
            delButton.setEnabled(false);
        }

    }


    //Menu of Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.greeting)).append("\n").append(getString(R.string.helpText));
        String text = getString(R.string.helpText);
        setShareActionIntent(stringBuilder.toString());
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }



}