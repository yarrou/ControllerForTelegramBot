package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.ui.login.LoginActivity;
import site.alexkononsol.controllerfortelegrambot.ui.settings.SettingActivity;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;


public class AccountSettingsFragment extends Fragment {
    private Button logoutButton;
    private TextView authInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_settings, container, false);
    }
    @Override
    public void onStart(){
        super.onStart();
        authInfo = (TextView) getView().findViewById(R.id.authSettingsStatus);
        logoutButton = (Button) getView().findViewById(R.id.logoutButton);
    }

    private void viewInfoAboutAccount() {
        if (SettingsManager.getSettings().getAuthToken() != null) {
            authInfo.setText(getString(R.string.authInfo) + SettingsManager.getSettings().getUserName());
        } else {
            authInfo.setText(getString(R.string.anonimous));
            logoutButton.setText(getString(R.string.sign_in_button_text));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        viewInfoAboutAccount();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                        if (SettingsManager.getSettings().getUserName() != null) {
                            SettingsManager.getSettings().setAuthToken(null);
                            SettingsManager.getSettings().setUserName(null);
                            SettingsManager.save();

                            authInfo.post(new Runnable() {
                                public void run() {
                                    authInfo.setText(getString(R.string.anonimous));
                                }
                            });
                            logoutButton.post(new Runnable() {
                                @Override
                                public void run() {
                                    logoutButton.setText(getString(R.string.sign_in_button_text));
                                }
                            });
                        } else {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                }).start();
            }
        });
    }
}