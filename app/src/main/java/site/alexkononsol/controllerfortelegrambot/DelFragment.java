package site.alexkononsol.controllerfortelegrambot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ServerResponse;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestToServer;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestType;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;
import site.alexkononsol.controllerfortelegrambot.utils.TextValidator;

public class DelFragment extends Fragment {

    private Button delButton;
    private static String cityName;
    private static final String ARG_PARAM1 = "cityName";

    public static DelFragment newInstance(String cityName) {
        DelFragment fragment = new DelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    public DelFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityName = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_del, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        TextView contentView = (TextView) view.findViewById(R.id.delResponse);
        TextView getTextView = (TextView) view.findViewById(R.id.delRequest);
        if (cityName != null) {
            getTextView.post(() -> getTextView.setText(cityName));
        }
        delButton = (Button) view.findViewById(R.id.buttonDel);
        delButton.setOnClickListener(v -> {

            if (TextValidator.noEmptyValidation(getTextView)) {
                contentView.setText(getString(R.string.toastLoading));
                new Thread(() -> {
                    String cityName = getTextView.getText().toString();
                    RetrofitRequestToServer requestToServer = new RetrofitRequestToServer(getContext());
                    ServerResponse serverResponse = requestToServer.stringRequest(cityName, RetrofitRequestType.DELETE);
                    contentView.post(() -> contentView.setText(serverResponse.getData().toString()));
                }).start();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String userLogin = SettingsManager.getSettings().getUserName();
        delButton.setEnabled(userLogin != null);
    }
}