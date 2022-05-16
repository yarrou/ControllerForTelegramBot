package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;

public class CityDescriptionFragment extends Fragment {


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
        TextView cityNameView = getView().findViewById(R.id.description_fragment_city_name);
        TextView cityCreatedDate = getView().findViewById(R.id.description_fragment_city_created);
        TextView cityModifyDate = getView().findViewById(R.id.description_fragment_city_modify);
        TextView cityDescription = getView().findViewById(R.id.description_city_fragment_description);
        ImageView cityImage = getView().findViewById(R.id.image_description);
        cityNameView.post(() -> cityNameView.setText(city.getName()));
        cityDescription.post(() -> cityDescription.setText(city.getText()));
        cityCreatedDate.post(() -> {
            String cityCreatedText = getString(R.string.description_fragment_city_created_text);
            if (city.getDateCreated() == null)
                cityCreatedText = String.format(cityCreatedText, getString(R.string.is_unknown));
            else
                cityCreatedText = String.format(cityCreatedText, city.getDateCreated().toString());
            cityCreatedDate.setText(cityCreatedText);
        });
        cityModifyDate.post(() -> {
            String cityModifyText = getString(R.string.description_fragment_city_modify_text);
            if (city.getDateLastModification() == null)
                cityModifyText = String.format(cityModifyText, getString(R.string.is_unknown));
            else
                cityModifyText = String.format(cityModifyText, city.getDateLastModification().toString());
            cityModifyDate.setText(cityModifyText);
        });
        cityImage.post(() -> {
            try {
                byte[] imageByteArray = Base64.decodeBase64(city.getPicture().getBytes());
                InputStream inputStream = new ByteArrayInputStream(imageByteArray);

                Drawable d = Drawable.createFromStream(inputStream, "src name");
                cityImage.setImageDrawable(d);
                LogHelper.logDebug(CityDescriptionFragment.this, "the image of the city is displayed");
            } catch (Exception e) {
                LogHelper.logError(CityDescriptionFragment.this, e.getMessage(), e);
                e.printStackTrace();
            }

        });
    }
}