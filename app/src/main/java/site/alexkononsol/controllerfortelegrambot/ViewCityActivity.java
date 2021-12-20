package site.alexkononsol.controllerfortelegrambot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import site.alexkononsol.controllerfortelegrambot.entity.City;

public class ViewCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_city);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        City city = (City) bundle.getSerializable("city");
        TextView nameCity =(TextView) findViewById(R.id.infoCityName);
        nameCity.setText(city.getName());
        TextView descriptionCity = (TextView) findViewById(R.id.infoCityDescription);
        descriptionCity.setText(city.getText());
    }
}