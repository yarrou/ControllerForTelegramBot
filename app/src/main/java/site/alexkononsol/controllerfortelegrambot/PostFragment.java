package site.alexkononsol.controllerfortelegrambot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ServerResponse;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestToServer;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestType;
import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;
import site.alexkononsol.controllerfortelegrambot.utils.TextValidator;

public class PostFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 1;
    Button postButton;
    private ImageView cityImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        TextView contentView = (TextView) view.findViewById(R.id.postResponse);
        postButton = (Button) view.findViewById(R.id.buttonPost);
        cityImage = getView().findViewById(R.id.city_picture);
        cityImage.setOnClickListener(v -> pickFromGallery());
        postButton.setOnClickListener(v -> {
            TextView cityNameView = (TextView) view.findViewById(R.id.postRequest);
            TextView cityDescriptionView = (TextView) view.findViewById(R.id.postRequestDescription);
            //checking for non-emptiness
            if (TextValidator.noEmptyValidation(cityNameView)&&TextValidator.noEmptyValidation(cityDescriptionView)) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executor.execute(() -> {
                    //Background work here
                    String cityName = cityNameView.getText().toString();
                    String cityDescription = cityDescriptionView.getText().toString();
                    BitmapDrawable bitmapDrawable = ((BitmapDrawable) cityImage.getDrawable());
                    Bitmap bitmap = bitmapDrawable .getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] imageInByte = stream.toByteArray();
                    RetrofitRequestToServer requestToServer = new RetrofitRequestToServer(requireContext());
                    ServerResponse response = requestToServer.addOrChangeCity(imageInByte, new City(cityName, cityDescription, ""), RetrofitRequestType.POST);
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
        postButton.setEnabled(userLogin != null);
    }
    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    cityImage.setImageURI(selectedImage);
                    break;
            }
    }
}