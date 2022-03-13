package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import site.alexkononsol.controllerfortelegrambot.R;


public class PermissionButtonFragment extends Fragment {

    public static interface Listener {
        void grantPermission();
    }

    private Listener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_permission_button, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Button grantPermissionButton = getView().findViewById(R.id.button_permission_request);
        grantPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.grantPermission();
            }
        });
    }

}