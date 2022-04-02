package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import static site.alexkononsol.controllerfortelegrambot.R.id.textSizeLargeRadio;
import static site.alexkononsol.controllerfortelegrambot.R.id.textSizeSmallRadio;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import site.alexkononsol.controllerfortelegrambot.HelpActivity;
import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class HelpSettingsFragment extends Fragment {
    private RadioGroup radioGroup; //responsible for the size of the help text
    private CheckBox helpStartCheckbox;//responsible for displaying help at startup
    private ImageButton helpViewButton;//responsible for displaying help in new activity

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        radioGroup = (RadioGroup) getView().findViewById(R.id.radioTextSize);
        radioGroup.setOnCheckedChangeListener((radioGroup, id) -> {
            String textSize = "normal";
            switch (id) {
                case textSizeLargeRadio:
                    textSize = "large";
                    break;
                case textSizeSmallRadio:
                    textSize = "small";
            }
            SettingsManager.getSettings().setTextSize(textSize);
        });

        helpStartCheckbox = (CheckBox) getView().findViewById(R.id.viewHelpOnStart);
        helpStartCheckbox.setOnClickListener(view -> helpOnStart());

        helpViewButton = getView().findViewById(R.id.help_view_button);
        helpViewButton.setOnClickListener(view -> onSettingsHelpInfo());
    }

    public void onResume() {
        super.onResume();
        String textSize = SettingsManager.getSettings().getTextSize();
        int idRadioTextSize = 1;
        switch (textSize) {
            case "small":
                idRadioTextSize = 0;
                break;
            case "large":
                idRadioTextSize = 2;
                break;
        }

        RadioButton savedCheckedRadioButton = (RadioButton) radioGroup.getChildAt(idRadioTextSize);
        savedCheckedRadioButton.setChecked(true);

        boolean viewHelpOnStart = SettingsManager.getSettings().isViewHelpOnStart();
        helpStartCheckbox.setChecked(viewHelpOnStart);
    }

    private void onSettingsHelpInfo() {
        Intent intent = new Intent(getActivity(), HelpActivity.class);
        startActivity(intent);
    }

    private void helpOnStart() {
        SettingsManager.getSettings().setViewHelpOnStart(helpStartCheckbox.isChecked());
    }
}