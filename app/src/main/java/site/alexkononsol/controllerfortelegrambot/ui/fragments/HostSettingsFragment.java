package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ServerResponse;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestToServer;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestType;
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
        testButton.setOnClickListener(v -> {
            if (TextValidator.noEmptyValidation(hostNameView)) {  //checking for non-emptiness
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executor.execute(() -> {
                    //Background work here
                    String content = getNameBot();
                    handler.post(() -> {
                        //UI Thread work here
                        nameBotView.setText(content);
                    });
                });
            }
        });
    }

    private String getNameBot() {
        String serverUrl = hostNameView.getText().toString();
        if (serverUrl.equals("")) {
            serverUrl = hostNameView.getHint().toString();
        }
        RetrofitRequestToServer requestToServer = new RetrofitRequestToServer();
        ServerResponse response = requestToServer.stringRequest(serverUrl, RetrofitRequestType.NAME);
        if (response.getCode()==200) {
            return response.getData().toString();
        }
        else {
            return getString(R.string.nameBotNotAnswer);
        }
    }
}