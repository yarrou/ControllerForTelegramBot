package site.alexkononsol.controllerfortelegrambot;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.ui.settings.SettingActivity;
import site.alexkononsol.controllerfortelegrambot.utils.DeviceTypeHelper;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class MainActivity extends AppCompatActivity implements ChoosingActionFragment.Listener {

    private final int CHANGE_CITY_CODE = 1;
    private final int DELETE_CITY_CODE = 2;

    private ShareActionProvider shareActionProvider;
    private TabLayout tabLayout;
    private ViewPager pager;
    private Bundle changeBundle;
    private View fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!DeviceTypeHelper.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String isSavedHost = SettingsManager.getSettings().getHostName();
        if (isSavedHost == null) {
            String toastTextNotHost = getString(R.string.toastTextNotHost);
            Toast.makeText(this, toastTextNotHost, Toast.LENGTH_SHORT).show();
        }

        pager = (ViewPager) findViewById(R.id.pager);
        if (pager != null) {
            SectionsPagerAdapter pagerAdapter =
                    new SectionsPagerAdapter(getSupportFragmentManager());
            pager.setAdapter(pagerAdapter);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(pager);
        }
        fragmentContainer = findViewById(R.id.fragment_container);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        changeBundle = intent.getExtras();
        if (changeBundle != null) {
            if (pager != null) {
                if (changeBundle.getInt("action") == CHANGE_CITY_CODE) changeTabSelect();
                else if (changeBundle.getInt("action") == DELETE_CITY_CODE) deleteTabSelect();
            } else if (fragmentContainer != null && changeBundle.getInt("action") == CHANGE_CITY_CODE) {
                City city = (City) changeBundle.getSerializable("city");
                PutFragment putFragment = (PutFragment) getSupportFragmentManager().findFragmentByTag("PUT_FRAGMENT");
                if (putFragment == null) {
                    Gson gson = new Gson();
                    putFragment = PutFragment.newInstance(gson.toJson(city));
                }
                changeBundle = null;
                transactionFragment(putFragment);
            } else if (fragmentContainer != null && changeBundle.getInt("action") == DELETE_CITY_CODE) {
                transactionFragment(DelFragment.newInstance(changeBundle.getString("cityName")));
            }
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


    @Override
    public void actionChoose(String action) {
        switch (action) {
            case ("get"):
                if (fragmentContainer != null) transactionFragment(new GetFragment());
                else startActivity(new Intent(this, GetActivity.class));
                break;
            case ("search"):
                if (fragmentContainer != null) transactionFragment(new SearchFragment());
                else startActivity(new Intent(this, SearchActivity.class));
                break;
            case ("post"):
                if (fragmentContainer != null) transactionFragment(new PostFragment());
                else startActivity(new Intent(this, PostActivity.class));
                break;
            case ("put"):
                if (fragmentContainer != null) {
                    transactionFragment(new PutFragment());
                } else startActivity(new Intent(this, PutActivity.class));
                break;
            case ("del"):
                if (fragmentContainer != null) transactionFragment(new DelFragment());
                else startActivity(new Intent(this, PutActivity.class));
                break;
        }
    }

    private void transactionFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment, "PUT_FRAGMENT");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SearchFragment();
                case 1:
                    return new GetFragment();
                case 2:
                    return new PostFragment();
                case 3:
                    if (changeBundle != null && changeBundle.getInt("action") == CHANGE_CITY_CODE) {
                        City city = (City) changeBundle.getSerializable("city");
                        Gson gson = new Gson();
                        return PutFragment.newInstance(gson.toJson(city));
                    } else return new PutFragment();
                case 4:
                    if (changeBundle != null && changeBundle.getInt("action") == DELETE_CITY_CODE) {
                        return DelFragment.newInstance(changeBundle.getString("cityName"));
                    }
                    return new DelFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getText(R.string.search_tab);
                case 1:
                    return getResources().getText(R.string.get_tab);
                case 2:
                    return getResources().getText(R.string.post_tab);
                case 3:
                    return getResources().getText(R.string.put_tab);
                case 4:
                    return getResources().getText(R.string.del_tab);
            }
            return null;
        }
    }

    void changeTabSelect() {
        tabLayout.setScrollPosition(3, 0f, true);
        pager.setCurrentItem(3);
    }

    private void deleteTabSelect() {
        tabLayout.setScrollPosition(4, 0f, true);
        pager.setCurrentItem(4);
    }
}


