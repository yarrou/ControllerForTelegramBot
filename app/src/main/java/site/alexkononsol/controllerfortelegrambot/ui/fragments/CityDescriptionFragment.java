package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.dao.CityDao;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.utils.CityPictureService;

public class CityDescriptionFragment extends Fragment {

    public static interface Listener {
        void actionChangeCity(CityDao cityDao);
    }
    private Listener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listener) context;
    }

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "jsonFromCity";

    private CityDao cityDao;

    public CityDescriptionFragment() {
        // Required empty public constructor
    }

    public static CityDescriptionFragment newInstance(String jsonFromCityDao) {
        CityDescriptionFragment fragment = new CityDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, jsonFromCityDao);
        fragment.setArguments(args);
        return fragment;
    }
    public static CityDescriptionFragment newInstance(CityDao cityDao) {
        Gson gson = new Gson();
        return newInstance(gson.toJson(cityDao));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            cityDao = gson.fromJson(getArguments().getString(ARG_PARAM1), CityDao.class);
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
        ImageButton cityChangeImageButton = getView().findViewById(R.id.changeThisCityButton);

        cityChangeImageButton.setOnClickListener(v -> listener.actionChangeCity(cityDao));
        cityNameView.post(() -> cityNameView.setText(cityDao.getName()));
        cityDescription.post(() -> cityDescription.setText(cityDao.getText()));
        cityCreatedDate.post(() -> {
            String cityCreatedText = getString(R.string.description_fragment_city_created_text);
            if (cityDao.getDateCreated() == null)
                cityCreatedText = String.format(cityCreatedText, getString(R.string.is_unknown));
            else
                cityCreatedText = String.format(cityCreatedText, cityDao.getDateCreated().toString());
            cityCreatedDate.setText(cityCreatedText);
        });
        cityModifyDate.post(() -> {
            String cityModifyText = getString(R.string.description_fragment_city_modify_text);
            if (cityDao.getDateLastModification() == null)
                cityModifyText = String.format(cityModifyText, getString(R.string.is_unknown));
            else
                cityModifyText = String.format(cityModifyText, cityDao.getDateLastModification().toString());
            cityModifyDate.setText(cityModifyText);
        });
        cityImage.post(() -> {
            try {
                CityPictureService service = new CityPictureService(getContext());
                Drawable d = service.getDrawableFromCityDao(cityDao);
                cityImage.setImageDrawable(d);
                LogHelper.logDebug(CityDescriptionFragment.this, "the image of the city is displayed");
            } catch (Exception e) {
                LogHelper.logError(CityDescriptionFragment.this, e.getMessage(), e);
                e.printStackTrace();
            }
        });
    }
}