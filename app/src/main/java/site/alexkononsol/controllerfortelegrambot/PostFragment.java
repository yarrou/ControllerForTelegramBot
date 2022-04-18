package site.alexkononsol.controllerfortelegrambot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestToServer;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestType;
import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;
import site.alexkononsol.controllerfortelegrambot.utils.TextValidator;

public class PostFragment extends Fragment {

    Button postButton;

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
        String host = SettingsManager.getSettings().getHostName();

        postButton.setOnClickListener(v -> {
            TextView cityNameView = (TextView) view.findViewById(R.id.postRequest);
            TextView cityDescriptionView = (TextView) view.findViewById(R.id.postRequestDescription);
            //checking for non-emptiness
            if (TextValidator.noEmptyValidation(cityNameView)&&TextValidator.noEmptyValidation(cityDescriptionView)) {
                String cityName = cityNameView.getText().toString();
                String cityDescription = cityDescriptionView.getText().toString();
                contentView.setText(getString(R.string.toastLoading));
                new Thread(() -> {
                    try {
                        RequestToServer post = new RequestToServer(Constants.ENDPOINT_POST_CITY, RequestType.POST);
                        post.addAuthHeader();
                        post.addLangParam();
                        post.addJsonHeaders();
                        post.setBody(new City(cityName,cityDescription));
                        String content = post.send().getData();
                        contentView.post(() -> contentView.setText(content));
                    } catch (Exception ex) {
                        LogHelper.logError(PostFragment.this,ex.getMessage(),ex);
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
        postButton.setEnabled(userLogin != null);
    }
}