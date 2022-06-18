package site.alexkononsol.controllerfortelegrambot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.RequestEncoder;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestToServer;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestType;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
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
                    try {

                        String cityName = getTextView.getText().toString();
                        String query = RequestEncoder.getRequest(cityName);
                        RequestToServer del = new RequestToServer(Constants.ENDPOINT_DEL_CITY, RequestType.DELETE);
                        del.addParam("city", query);
                        del.addLangParam();
                        del.addAuthHeader();
                        String content = del.send().getData().toString();
                        contentView.post(() -> contentView.setText(content));
                    } catch (IOException ex) {
                        LogHelper.logError(DelFragment.this, ex.getMessage(), ex);
                        contentView.post(() -> {
                            contentView.setText(getString(R.string.error) + " : " + ex.getMessage() + ex.getLocalizedMessage());
                            Toast.makeText(getContext(), getString(R.string.error) + " : ", Toast.LENGTH_SHORT).show();
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
        delButton.setEnabled(userLogin != null);
    }
}