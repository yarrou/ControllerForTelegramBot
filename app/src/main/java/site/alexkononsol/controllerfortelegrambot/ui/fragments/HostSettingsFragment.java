package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestToServer;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;
import site.alexkononsol.controllerfortelegrambot.utils.TextValidator;

public class HostSettingsFragment extends Fragment {
    private EditText hostNameView;
    private TextView nameBotView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_host_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        hostNameView = getView().findViewById(R.id.hostName);
        nameBotView = getView().findViewById(R.id.nameBot);
        String host = SettingsManager.getSettings().getHostName();
        if (host == null || host.equals("")) {
            hostNameView.setHint(Constants.DEFAULT_HOST_URL);
        } else hostNameView.setText(SettingsManager.getSettings().getHostName());

        Button testButton = (Button) getView().findViewById(R.id.buttonSettingsGetNameBot);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TextView hostNameView = (TextView) findViewById(R.id.hostName);
                if (TextValidator.noEmptyValidation(hostNameView)) {  //checking for non-emptiness
                    new Thread(() -> {
                        String content = getNameBot();
                        nameBotView.post(() -> nameBotView.setText(content));
                    }).start();
                }
            }
        });
    }

    private String getNameBot() {
        String serverUrl = hostNameView.getText().toString();
        if (serverUrl.equals("")) {
            serverUrl = hostNameView.getHint().toString();
        }
        try {
            RequestToServer request = new RequestToServer(serverUrl, Constants.ENDPOINT_BOT_NAME);
            request.addLangParam();
            return request.send().getData();
        } catch (Exception ex) {
            LogHelper.logError(this, ex.getMessage(), ex);
            return getString(R.string.nameBotNotAnswer);
        }
    }
}