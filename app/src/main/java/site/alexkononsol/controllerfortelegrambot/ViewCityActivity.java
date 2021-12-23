package site.alexkononsol.controllerfortelegrambot;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.settings.SettingActivity;

public class ViewCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_city);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        City city = (City) bundle.getSerializable("city");
        TextView nameCity =(TextView) findViewById(R.id.infoCityName);
        nameCity.setText(city.getName());
        TextView descriptionCity = (TextView) findViewById(R.id.infoCityDescription);
        descriptionCity.setText(city.getText());
    }

    //Menu of Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}