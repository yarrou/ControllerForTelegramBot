package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.ViewCityActivity;


public class CityListFragment extends Fragment {

    ListView contentView;

    private static final String ARG_PARAM1 = "param1";

    private List<String> listCitiesNames;

    public CityListFragment() {

    }

    public static CityListFragment newInstance(List<String> list) {
        CityListFragment fragment = new CityListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, new Gson().toJson(list));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            Type listOfMyClassObject = new TypeToken<ArrayList<String>>() {
            }.getType();
            listCitiesNames = gson.fromJson(getArguments().getString(ARG_PARAM1), listOfMyClassObject);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        contentView = (ListView) requireView().findViewById(R.id.listOfFoundedCities);
        AdapterView.OnItemClickListener itemClickListener = (adapterView, view, position, l) -> {
            Intent intent = new Intent(view.getContext(), ViewCityActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("city", listCitiesNames.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        };
        contentView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        contentView.post(() -> {
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<>(requireView().getContext(), android.R.layout.simple_list_item_1, listCitiesNames);
            contentView.setAdapter(itemsAdapter);
        });
    }

}