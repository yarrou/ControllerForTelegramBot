package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
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
    private Timer timer;
    private final TextWatcher hostNameTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable arg0) {
            // user typed: start the timer
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // do your actual work here
                    checkHost();
                }
            }, 2000); // 2000ms delay before the timer executes the „run“ method from TimerTask
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // nothing to do here
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // user is typing: reset already started timer (if existing)
            if (timer != null) {
                timer.cancel();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_host_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        hostNameView = requireView().findViewById(R.id.hostName);

        nameBotView = requireView().findViewById(R.id.nameBot);
        String host = SettingsManager.getSettings().getHostName();
        if (host == null || host.equals("")) {
            hostNameView.setHint(Constants.DEFAULT_HOST_URL);
        } else hostNameView.setText(SettingsManager.getSettings().getHostName());
        hostNameView.addTextChangedListener(hostNameTextWatcher);
        Button testButton = requireView().findViewById(R.id.buttonSettingsGetNameBot);
        testButton.setOnClickListener(v -> checkHost());
    }

    private String getNameBot() {
        String serverUrl = hostNameView.getText().toString();
        if (serverUrl.equals("")) {
            serverUrl = hostNameView.getHint().toString();
        }
        RetrofitRequestToServer requestToServer = new RetrofitRequestToServer(getContext());
        ServerResponse response = requestToServer.stringRequest(serverUrl, RetrofitRequestType.NAME);
        if (response.getCode() == 200) {
            return response.getData().toString();
        } else {
            return getString(R.string.nameBotNotAnswer);
        }
    }

    private void checkHost() {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        checkHost();
    }
}