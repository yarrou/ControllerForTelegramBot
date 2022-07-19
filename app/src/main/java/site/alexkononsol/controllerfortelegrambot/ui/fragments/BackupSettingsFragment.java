package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Objects;

import site.alexkononsol.controllerfortelegrambot.AppHelperService;
import site.alexkononsol.controllerfortelegrambot.BackupActivity;
import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.utils.BackupHelper;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class BackupSettingsFragment extends Fragment {

    private EditText backupFileNameEditText;
    private String backupPath;//path to the created backup file
    private ImageButton saveBackupButton;
    private ImageButton loadBackupButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_backup_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        backupFileNameEditText = requireView().findViewById(R.id.backup_file_name_value);
        saveBackupButton = requireView().findViewById(R.id.save_backup_settings_button);
        saveBackupButton.setOnClickListener(view -> onSaveBackup());
        loadBackupButton = requireView().findViewById(R.id.show_fileChooser_button);
        loadBackupButton.setOnClickListener(view -> onShowFileChooser());
    }

    @Override
    public void onResume() {
        super.onResume();
        viewNameBackup();
    }

    public void onSaveBackup() {
        // write on SD card file data in the text box
        String nameFile = backupFileNameEditText.getText().toString();
        if ( nameFile.equals("")) {
            nameFile = backupFileNameEditText.getHint().toString();
        }
        try {
            backupPath = BackupHelper.createBackup(nameFile, getActivity());
            AppHelperService.startActionLogDebug(requireContext(), "backup created " + backupPath);
            Toast.makeText(requireActivity().getBaseContext(), getString(R.string.backupToastSuccessfully) + backupPath,
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            AppHelperService.startActionLogError(requireContext(), ExceptionUtils.getStackTrace(e));
            Toast.makeText(requireActivity().getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private static final int FILE_SELECT_CODE = 1;

    public void onShowFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, getString(R.string.toastSelectFile)),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), getString(R.string.toastNeedInstallFileManager),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(getActivity(), BackupActivity.class);
                    intent.setData(data.getData());
                    startActivity(intent);
                }
                break;
        }
    }

    private void viewNameBackup() {
        String savedBackupName = SettingsManager.getSettings().getBackupName();
        if (savedBackupName == null || savedBackupName.equals("")) {
            savedBackupName = SettingsManager.getSettings().getHostName().split("://")[1].split("/")[0];
        }
        backupFileNameEditText.setHint(savedBackupName);
    }
}