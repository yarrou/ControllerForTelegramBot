package site.alexkononsol.controllerfortelegrambot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ServerResponse;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestToServer;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestType;
import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;
import site.alexkononsol.controllerfortelegrambot.utils.TextValidator;

public class PutFragment extends Fragment {

    private City city;
    private TextView cityNameView;
    private TextView cityDescriptionView;
    private static final String ARG_PARAM1 = "jsonFromCity";
    private Button putButton;
    private static final int GALLERY_REQUEST_CODE = 1;
    private ImageView cityImage;
    private String pathToImage;

    public static PutFragment newInstance(String jsonFromCity) {
        PutFragment fragment = new PutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, jsonFromCity);
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
            city = gson.fromJson(getArguments().getString(ARG_PARAM1), City.class);
            pathToImage = city.getPicture();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Gson gson = new Gson();
        String jsonFromCityDao = gson.toJson(city);
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
        TextView contentView = getView().findViewById(R.id.putResponse);
        putButton = getView().findViewById(R.id.buttonPut);
        cityImage = getView().findViewById(R.id.city_picture);
        cityNameView = getView().findViewById(R.id.putRequest);
        cityDescriptionView = getView().findViewById(R.id.putRequestDescription);
        if (city != null) {
            cityNameView.post(() -> cityNameView.setText(city.getName()));
            cityDescriptionView.post(() -> cityDescriptionView.setText(city.getText()));

        } else {
            pathToImage = "android.resource://" + getContext().getApplicationContext().getPackageName() + "/drawable/city_drawable";
            city = new City("", "", "");

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
                contentView.setText(getString(R.string.toastLoading));
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executor.execute(() -> {
                    //Background work here
                    RetrofitRequestToServer requestToServer = new RetrofitRequestToServer(getContext());
                    ServerResponse response = requestToServer.addOrChangeCity(imageInByte, new City(cityName, cityDescription, ""), RetrofitRequestType.PUT);
                    String content = response.getData().toString();
                    handler.post(() -> {
                        //UI Thread work here
                        contentView.setText(content);

                    });
                });
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String userLogin = SettingsManager.getSettings().getUserName();
        putButton.setEnabled(userLogin != null);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            Bitmap bmp = null;
            try {
                URL url = new URL(pathToImage);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException me) {
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(pathToImage));
                } catch (Exception e) {
                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.city_drawable);
                    LogHelper.logDebug(PutFragment.this, "use default city image");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Bitmap finalBmp = bmp;
            handler.post(() -> {
                if (finalBmp != null) cityImage.setImageBitmap(finalBmp);
            });
        });

    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
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
                    Uri selectedImage = data.getData();
                    pathToImage = selectedImage.toString();
                    LogHelper.logDebug(this, pathToImage);
                    break;
            }

    }

    public void setCity(City city) {
        this.city = city;
    }
}

