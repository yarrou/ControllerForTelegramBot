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
        Button delButton = (Button) view.findViewById(R.id.buttonDel);
        String host = SettingsManager.getSettings().getHostName();
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView getTextView = (TextView) view.findViewById(R.id.delRequest);
                if (TextValidator.noEmptyValidation(getTextView)) {
                    contentView.setText(getString(R.string.toastLoading));
                    new Thread(new Runnable() {
                        public void run() {
                            try {

                                String cityName = getTextView.getText().toString();
                                String query = RequestEncoder.getRequest(cityName);
                                RequestToServer del = new RequestToServer(Constants.ENDPOINT_DEL_CITY, RequestType.DELETE);
                                del.addParam("city",query);
                                del.addLangParam();
                                del.addAuthHeader();
                                String content = del.send().getData().toString();
                                contentView.post(new Runnable() {
                                    public void run() {
                                        contentView.setText(content);
                                    }
                                });
                            } catch (IOException ex) {
                                LogHelper.logError(DelFragment.this,ex.getMessage(),ex);
                                contentView.post(new Runnable() {
                                    public void run() {
                                        contentView.setText(getString(R.string.error) + " : " + ex.getMessage() + ex.getLocalizedMessage());
                                        Toast.makeText(getContext(), getString(R.string.error) + " : ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }

        });
    }
}