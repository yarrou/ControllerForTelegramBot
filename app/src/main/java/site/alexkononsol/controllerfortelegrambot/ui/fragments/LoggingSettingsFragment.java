package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import static java.lang.String.format;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import java.io.IOException;

import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;


public class LoggingSettingsFragment extends Fragment {
    private CheckBox logging;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logging_settings, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        logging = getView().findViewById(R.id.checkBox_logging);
        TextView logFilePathView = getView().findViewById(R.id.logging_file_path_title);
        try {
            String logFilePath = format(getString(R.string.logging_file_path), LogHelper.getLogFilePath(getContext()));
            logFilePathView.setText(logFilePath);
            logging.setText(getString(R.string.logging_checkbox_title));
            logging.setChecked(SettingsManager.getSettings().isLogging());
            logging.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        public void run() {
                           SettingsManager.getSettings().setLogging(logging.isChecked());
                        }
                    }).start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}