package site.alexkononsol.controllerfortelegrambot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ServerResponse;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestToServer;
import site.alexkononsol.controllerfortelegrambot.ui.fragments.CityListFragment;
import site.alexkononsol.controllerfortelegrambot.ui.fragments.ErrorFragment;

public class SearchFragment extends Fragment {

    List<String> citiesNamesList;
    String cityName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) {
            citiesNamesList = (ArrayList<String>) savedInstanceState.getSerializable("listCity");
            cityName = savedInstanceState.getString("cityName");
        }
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("listCity", (Serializable) citiesNamesList);
        savedInstanceState.putString("cityName", cityName);
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView searchTextView = (TextView) requireView().findViewById(R.id.searchHint);

        searchTextView.setText(cityName);

        ImageButton searchButton = (ImageButton) requireView().findViewById(R.id.localSearchButton);
        searchButton.setOnClickListener(v -> {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                //Background work here
                cityName = searchTextView.getText().toString();
                RetrofitRequestToServer requestToServer = new RetrofitRequestToServer(getContext());
                ServerResponse response = requestToServer.findCity(cityName);
                handler.post(() -> {
                    //UI Thread work here
                    if (response.getCode() == 200) {
                        citiesNamesList = response.getCitiesList();
                        transactionFragment(CityListFragment.newInstance(citiesNamesList));
                    } else if (response.getCode() == 404){
                        citiesNamesList = null;
                        transactionFragment(ErrorFragment.newInstance(getString(R.string.notFoundResult)));}
                    else{
                        citiesNamesList = null;
                        transactionFragment(ErrorFragment.newInstance(response.getData().toString()));}
                });
            });
        });
    }

    public void onResume(){
        super.onResume();
        if (citiesNamesList != null) transactionFragment(CityListFragment.newInstance(citiesNamesList));
    }

    private void transactionFragment(Fragment fragment) {
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.view_city_list_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}