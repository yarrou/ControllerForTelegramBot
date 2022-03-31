package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import site.alexkononsol.controllerfortelegrambot.R;

public class HelpSettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}