package site.alexkononsol.controllerfortelegrambot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ContentUrlProvider;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.RequestEncoder;
import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;

public class SearchActivity extends AppCompatActivity {
    Context context = this;
    List<City> content;
    String cityName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView contentView = (ListView) findViewById(R.id.searchList);
        TextView searchInfo = (TextView) findViewById(R.id.searchInfo);
        TextView searchTextView = (TextView) findViewById(R.id.searchHint);

        if(savedInstanceState!=null){
            content = (ArrayList<City>) savedInstanceState.getSerializable("listCity");
            cityName = savedInstanceState.getString("cityName");
            searchTextView.setText(cityName);
            viewListCity(content);
        }

        ImageButton searchButton = (ImageButton) findViewById(R.id.localSearchButton);
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
                            content = ContentUrlProvider.getContentSearch(request);
                            viewListCity(content);

                        } catch (IOException ex) {
                            contentView.post(new Runnable() {
                                public void run() {
                                    searchInfo.setText("Ошибка: " + ex.getMessage() + ex.getLocalizedMessage());
                                    searchInfo.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(SearchActivity.this, ViewCityActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("city", content.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
        contentView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        if(cityName != null){
            savedInstanceState.putString("cityName",cityName);
        }
        if (content != null) {
            savedInstanceState.putSerializable("listCity",(Serializable) content);
        }

    }

    private void viewListCity(List<City> content){
        ListView contentView = (ListView) findViewById(R.id.searchList);
        TextView searchInfo = (TextView) findViewById(R.id.searchInfo);
        contentView.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {
                if (content.size() > 0) {
                    searchInfo.setVisibility(View.INVISIBLE);
                    ArrayList<String> nameCities = (ArrayList<String>) content.stream().map(City::getName).collect(Collectors.toList());
                    ArrayAdapter<String> itemsAdapter =
                            new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, nameCities);
                    contentView.setAdapter(itemsAdapter);
                    contentView.setVisibility(View.VISIBLE);
                } else {
                    searchInfo.setText(getString(R.string.notFoundResult));
                }
            }
        });
    }

}