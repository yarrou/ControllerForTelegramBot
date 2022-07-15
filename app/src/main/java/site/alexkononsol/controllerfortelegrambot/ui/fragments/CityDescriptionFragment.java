package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import site.alexkononsol.controllerfortelegrambot.AppHelperService;
import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.entity.City;

public class CityDescriptionFragment extends Fragment {

    public  interface Listener {
        void actionChangeCity(City city);
        void actionDeleteCity(String name);
    }

    private Listener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (Listener) context;
    }

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "jsonFromCity";

    private City city;

    public CityDescriptionFragment() {
        // Required empty public constructor
    }

    public static CityDescriptionFragment newInstance(String jsonFromCity) {
        CityDescriptionFragment fragment = new CityDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, jsonFromCity);
        fragment.setArguments(args);
        return fragment;
    }

    public static CityDescriptionFragment newInstance(City city) {
        Gson gson = new Gson();
        return newInstance(gson.toJson(city));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            city = gson.fromJson(getArguments().getString(ARG_PARAM1), City.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_description, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView cityNameView = requireView().findViewById(R.id.description_fragment_city_name);
        TextView cityCreatedDate = requireView().findViewById(R.id.description_fragment_city_created);
        TextView cityModifyDate = requireView().findViewById(R.id.description_fragment_city_modify);
        TextView cityDescription = requireView().findViewById(R.id.description_city_fragment_description);
        ImageView cityImage = requireView().findViewById(R.id.image_description);
        ImageButton cityChangeImageButton = requireView().findViewById(R.id.changeThisCityButton);
        ImageButton deleteCity = requireView().findViewById(R.id.fastDeleteCityButton);

        deleteCity.setOnClickListener(v -> listener.actionDeleteCity(city.getName()));
        cityChangeImageButton.setOnClickListener(v -> listener.actionChangeCity(city));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            Bitmap bmp = null;
            try {
                URL url = new URL(city.getPicture());
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                AppHelperService.startActionLogError(getContext(), e.getMessage());
                e.printStackTrace();
            }
            Bitmap finalBmp = bmp;
            handler.post(() -> {
                //UI Thread work here
                cityNameView.setText(city.getName());
                cityDescription.setText(city.getText());
                String cityCreatedText = getString(R.string.description_fragment_city_created_text);
                if (city.getDateCreated() == null)
                    cityCreatedText = String.format(cityCreatedText, getString(R.string.is_unknown));
                else
                    cityCreatedText = String.format(cityCreatedText, city.getDateCreated().toString());
                cityCreatedDate.setText(cityCreatedText);
                String cityModifyText = getString(R.string.description_fragment_city_modify_text);
                if (city.getDateLastModification() == null)
                    cityModifyText = String.format(cityModifyText, getString(R.string.is_unknown));
                else
                    cityModifyText = String.format(cityModifyText, city.getDateLastModification().toString());
                cityModifyDate.setText(cityModifyText);
                cityImage.setImageBitmap(finalBmp);

            });
        });
    }
}