package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import static java.lang.String.format;

import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.File;

import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;


public class LoggingSettingsFragment extends Fragment {
    private CheckBox logging;
    private Button clearLogsButton;
    private TextView sizeLogView;

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
        sizeLogView = getView().findViewById(R.id.size_logs_view);
        TextView logFilePathView = getView().findViewById(R.id.logging_file_path_title);
        String logFilePath = format(getString(R.string.logging_file_path), LogHelper.getLogFilePath());
        logFilePathView.setText(logFilePath);
        logging.setText(getString(R.string.logging_checkbox_title));
        logging.setChecked(SettingsManager.getSettings().isLogging());
        logging.setOnClickListener(v -> new Thread(() -> SettingsManager.getSettings().setLogging(logging.isChecked())).start());
        clearLogsButton = getView().findViewById(R.id.logs_clear_button);
        clearLogsButton.setOnClickListener(view -> clearLogs());
    }

    @Override
    public void onResume() {
        super.onResume();

        viewSizeLogs();
        clearLogsButton.post(() -> clearLogsButton.setEnabled((new File(LogHelper.getLogFilePath())).exists()));
    }

    private void clearLogs() {
        File logs = new File(LogHelper.getLogFilePath());
        logs.delete();
        viewSizeLogs();
    }

    private void viewSizeLogs() {
        sizeLogView.post(() -> {
            String kilobytes = getString(R.string.size_logs);
            sizeLogView.setText(format(kilobytes, LogHelper.getSizeLogFile()));
        });
    }

}