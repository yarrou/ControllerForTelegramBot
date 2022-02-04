package site.alexkononsol.controllerfortelegrambot;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestToServer;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestType;
import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;


public class SearchFragment extends Fragment {

    List<City> citiesList;
    String cityName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(savedInstanceState!=null){
            citiesList = (ArrayList<City>) savedInstanceState.getSerializable("listCity");
            cityName = savedInstanceState.getString("cityName");
        }
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        ListView contentView = (ListView) view.findViewById(R.id.searchList);
        TextView searchInfo = (TextView) view.findViewById(R.id.searchInfo);
        TextView searchTextView = (TextView) view.findViewById(R.id.searchHint);

        searchTextView.setText(cityName);
        if(citiesList!=null){
            viewListCity(citiesList);
        }


        ImageButton searchButton = (ImageButton) view.findViewById(R.id.localSearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInfo.setText(getString(R.string.searchInfoLoadingText));
                searchInfo.setVisibility(View.VISIBLE);
                contentView.setVisibility(View.INVISIBLE);
                String request = searchTextView.getText().toString();
                cityName = request;


                new Thread(new Runnable() {
                    public void run() {
                        try {
                            RequestToServer search = new RequestToServer(Constants.ENDPOINT_SEARCH_CITY, RequestType.GET);
                            search.addLangParam();
                            search.addParam("city",request);

                            citiesList = search.send().getCitiesList();//ContentUrlProvider.getContentSearch(request);
                            viewListCity(citiesList);

                        } catch (Exception ex) {
                            contentView.post(new Runnable() {
                                public void run() {
                                    searchInfo.setText(getString(R.string.error) + " : " + ex.getMessage() + ex.getLocalizedMessage());
                                    searchInfo.setVisibility(View.VISIBLE);
                                    Toast.makeText(view.getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(view.getContext(), ViewCityActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("city", citiesList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
        contentView.setOnItemClickListener(itemClickListener);
    }

    private void viewListCity(List<City> content){
        View view = getView();
        ListView contentView = (ListView) view.findViewById(R.id.searchList);
        TextView searchInfo = (TextView) view.findViewById(R.id.searchInfo);
        contentView.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {
                if (content.size() > 0) {
                    searchInfo.setVisibility(View.INVISIBLE);
                    ArrayList<String> nameCities = (ArrayList<String>) content.stream().map(City::getName).collect(Collectors.toList());
                    ArrayAdapter<String> itemsAdapter =
                            new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, nameCities);
                    contentView.setAdapter(itemsAdapter);
                    contentView.setVisibility(View.VISIBLE);
                } else {
                    searchInfo.setText(getString(R.string.notFoundResult));
                }
            }
        });
    }
}