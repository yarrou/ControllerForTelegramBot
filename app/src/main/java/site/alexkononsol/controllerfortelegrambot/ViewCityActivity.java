package site.alexkononsol.controllerfortelegrambot;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ServerResponse;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestToServer;
import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.ui.fragments.CityDescriptionFragment;
import site.alexkononsol.controllerfortelegrambot.ui.fragments.ErrorFragment;
import site.alexkononsol.controllerfortelegrambot.ui.settings.SettingActivity;
import site.alexkononsol.controllerfortelegrambot.utils.DeviceTypeHelper;

public class ViewCityActivity extends AppCompatActivity implements CityDescriptionFragment.Listener {
    private String cityName;
    private final int CHANGE_CITY_CODE = 1;
    private final int DELETE_CITY_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_city);

        if (!DeviceTypeHelper.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        cityName = bundle.getString("city");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        AtomicReference<ServerResponse> response = new AtomicReference<>();
        executor.execute(() -> {
            RetrofitRequestToServer requestToServer = new RetrofitRequestToServer();
            response.set(requestToServer.getCity(cityName));
            handler.post(() -> {
                //UI Thread work here
                if (response.get().getCode() == 200) {
                    Gson gson = new Gson();
                    City city = (City) response.get().getData();
                    transactionFragment(CityDescriptionFragment.newInstance(gson.toJson(city)));
                } else
                    transactionFragment(ErrorFragment.newInstance(response.get().getData().toString()));
            });
        });
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

    private void transactionFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.view_city_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void actionChangeCity(City city) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("action", CHANGE_CITY_CODE);
        bundle.putSerializable("city", city);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    public void actionDeleteCity(String cityName) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("action", DELETE_CITY_CODE);
        bundle.putString("cityName", cityName);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}