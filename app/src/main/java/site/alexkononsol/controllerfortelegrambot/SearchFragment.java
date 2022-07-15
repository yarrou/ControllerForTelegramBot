package site.alexkononsol.controllerfortelegrambot;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestToServer;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;


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

        ListView contentView = (ListView) getView().findViewById(R.id.searchList);
        TextView searchInfo = (TextView) getView().findViewById(R.id.searchInfo);
        TextView searchTextView = (TextView) getView().findViewById(R.id.searchHint);

        searchTextView.setText(cityName);
        if (citiesNamesList != null) {
            viewListCity(citiesNamesList);
        }

        ImageButton searchButton = (ImageButton) getView().findViewById(R.id.localSearchButton);
        searchButton.setOnClickListener(v -> {
            searchInfo.setText(getString(R.string.searchInfoLoadingText));
            searchInfo.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.INVISIBLE);
            cityName = searchTextView.getText().toString();

            new Thread(() -> {
                try {
                    RetrofitRequestToServer requestToServer = new RetrofitRequestToServer(getContext());
                    citiesNamesList = requestToServer.findCity(cityName).getCitiesList();
                    viewListCity(citiesNamesList);

                } catch (Exception ex) {
                    LogHelper.logError(SearchFragment.this, ex.getMessage(), ex);
                    contentView.post(() -> {
                        searchInfo.setText(getString(R.string.error) + " : " + ex.getMessage() + ex.getLocalizedMessage());
                        searchInfo.setVisibility(View.VISIBLE);
                        Toast.makeText(getView().getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        });

        AdapterView.OnItemClickListener itemClickListener = (adapterView, view, position, l) -> {
            Intent intent = new Intent(view.getContext(), ViewCityActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("city", citiesNamesList.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        };
        contentView.setOnItemClickListener(itemClickListener);
    }

    private void viewListCity(List<String> content) {
        View view = getView();
        ListView contentView = (ListView) view.findViewById(R.id.searchList);
        TextView searchInfo = (TextView) view.findViewById(R.id.searchInfo);
        contentView.post(() -> {
            if (content.size() > 0) {
                searchInfo.setVisibility(View.INVISIBLE);
                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, content);
                contentView.setAdapter(itemsAdapter);
                contentView.setVisibility(View.VISIBLE);
            } else {
                searchInfo.setText(getString(R.string.notFoundResult));
            }
        });
    }
}