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
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class GetFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        TextView contentView = (TextView) view.findViewById(R.id.getResponse);
        Button getButton = (Button) view.findViewById(R.id.buttonGet);
        String host = SettingsManager.getSettings().getHostName();
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentView.setText(getString(R.string.toastLoading));
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            TextView getTextView = (TextView) view.findViewById(R.id.getRequest);
                            String nameCity = getTextView.getText().toString();
                            String query = RequestEncoder.getRequest(nameCity);
                            RequestToServer get = new RequestToServer(Constants.ENDPOINT_GET_CITY, RequestType.GET);
                            get.addParam("city", query);
                            get.addLangParam();
                            String content = get.send().getData().toString();
                            contentView.post(new Runnable() {
                                public void run() {
                                    contentView.setText(content);
                                }
                            });
                        } catch (IOException ex) {
                            contentView.post(new Runnable() {
                                public void run() {
                                    contentView.setText("Ошибка: " + ex.getMessage() + ex.toString());
                                    Toast.makeText(view.getContext(), getString(R.string.error) + " : ", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}