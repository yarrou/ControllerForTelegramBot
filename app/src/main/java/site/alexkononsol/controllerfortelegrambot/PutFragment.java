package site.alexkononsol.controllerfortelegrambot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestToServer;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestType;
import site.alexkononsol.controllerfortelegrambot.dao.CityDao;
import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.utils.CityPictureService;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;
import site.alexkononsol.controllerfortelegrambot.utils.TextValidator;

public class PutFragment extends Fragment {

    private static CityDao cityDao;
    private TextView cityNameView;
    private TextView cityDescriptionView;
    private static final String ARG_PARAM1 = "jsonFromCityDao";
    private Button putButton;
    private static final int GALLERY_REQUEST_CODE = 1;
    private ImageView cityImage;
    private CityPictureService service;

    public static PutFragment newInstance(String jsonFromCityDao) {
        PutFragment fragment = new PutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, jsonFromCityDao);
        fragment.setArguments(args);
        return fragment;
    }

    public PutFragment() {
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Gson gson = new Gson();
        String jsonFromCityDao = gson.toJson(cityDao);
        savedInstanceState.putString(ARG_PARAM1, jsonFromCityDao);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_put, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView contentView = (TextView) getView().findViewById(R.id.putResponse);
        service = new CityPictureService(getContext());
        putButton = (Button) getView().findViewById(R.id.buttonPut);
        cityImage = getView().findViewById(R.id.city_picture);
        cityNameView = (TextView) getView().findViewById(R.id.putRequest);
        cityDescriptionView = (TextView) getView().findViewById(R.id.putRequestDescription);
        if (cityDao != null) {
            LogHelper.logDebug(this, "image temp file path is : " + cityDao.getPictureFilePath());
            cityNameView.post(() -> {
                cityNameView.setText(cityDao.getName());
            });
            cityDescriptionView.post(() -> {
                cityDescriptionView.setText(cityDao.getText());
            });

        } else {
            City city = new City("", "", service.getPictureAlsStringFromDrawable(cityImage.getDrawable()));
            cityDao = new CityDao(city, getContext());
        }
        cityImage.setOnClickListener(v -> pickFromGallery());
        putButton.setOnClickListener(v -> {

            //checking for non-emptiness
            if (TextValidator.noEmptyValidation(cityNameView) && (TextValidator.noEmptyValidation(cityDescriptionView))) {
                String cityName = cityNameView.getText().toString();
                String cityDescription = cityDescriptionView.getText().toString();
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) cityImage.getDrawable());
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageInByte = stream.toByteArray();
                String cityPictureString = new String(Base64.encodeBase64(imageInByte));
                contentView.setText(getString(R.string.toastLoading));
                new Thread(() -> {
                    try {
                        RequestToServer put = new RequestToServer(Constants.ENDPOINT_POST_CITY, RequestType.PUT);
                        put.addAuthHeader();
                        put.addLangParam();
                        put.addJsonHeaders();
                        put.setBody(new City(cityName, cityDescription, cityPictureString));
                        String content = put.send().getData();
                        contentView.post(() -> contentView.setText(content));
                    } catch (Exception ex) {
                        LogHelper.logError(PutFragment.this, ex.getMessage(), ex);
                        contentView.post(() -> {
                            contentView.setText(getString(R.string.error) + ": " + ex.getMessage());
                            Toast.makeText(getView().getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                        });
                    }
                }).start();
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String userLogin = SettingsManager.getSettings().getUserName();
        putButton.setEnabled(userLogin != null);
        cityImage.post(() -> {
            try {
                Drawable d = service.getDrawableFromCityDao(cityDao);
                cityImage.setImageDrawable(d);
            } catch (Exception e) {
                LogHelper.logError(PutFragment.this, e.getMessage(), e);
                e.printStackTrace();
            }
        });
    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        //Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    cityImage.post(() -> {
                        Drawable tempDrawable = null;
                        try {
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImage);
                            tempDrawable = Drawable.createFromStream(inputStream, selectedImage.toString());
                        } catch (FileNotFoundException e) {
                            tempDrawable = getResources().getDrawable(R.drawable.city_drawable);
                        }
                        cityDao.setPictureFilePath(service.createPictureFile(tempDrawable));
                    });
                    break;
            }
    }

}