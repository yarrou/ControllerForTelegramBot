package site.alexkononsol.controllerfortelegrambot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestToServer;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestType;
import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;
import site.alexkononsol.controllerfortelegrambot.utils.TextValidator;

public class PutFragment extends Fragment {

    Button putButton;
    private static final int GALLERY_REQUEST_CODE = 1;
    private ImageView cityImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_put, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        TextView contentView = (TextView) view.findViewById(R.id.putResponse);
        putButton = (Button)view.findViewById(R.id.buttonPut);
        cityImage = getView().findViewById(R.id.city_picture);
        cityImage.setOnClickListener(v -> pickFromGallery());
        putButton.setOnClickListener(v -> {
            TextView cityNameView = (TextView) view.findViewById(R.id.putRequest);
            TextView cityDescriptionView = (TextView) view.findViewById(R.id.putRequestDescription);
            //checking for non-emptiness
            if(TextValidator.noEmptyValidation(cityNameView)&&(TextValidator.noEmptyValidation(cityDescriptionView))){
                String cityName = cityNameView.getText().toString();
                String cityDescription = cityDescriptionView.getText().toString();
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) cityImage.getDrawable());
                Bitmap bitmap = bitmapDrawable .getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageInByte = stream.toByteArray();
                String cityPictureString = new String(Base64.encodeBase64(imageInByte));
                contentView.setText(getString(R.string.toastLoading));
                new Thread(() -> {
                    try{
                        RequestToServer put = new RequestToServer(Constants.ENDPOINT_POST_CITY, RequestType.PUT);
                        put.addAuthHeader();
                        put.addLangParam();
                        put.addJsonHeaders();
                        put.setBody(new City(cityName,cityDescription,cityPictureString));
                        String content = put.send().getData();
                        contentView.post(() -> contentView.setText(content));
                    }
                    catch (Exception ex){
                        LogHelper.logError(PutFragment.this,ex.getMessage(),ex);
                        contentView.post(() -> {
                            contentView.setText(getString(R.string.error) + ": " + ex.getMessage());
                            Toast.makeText(view.getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
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
    }
    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        //Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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