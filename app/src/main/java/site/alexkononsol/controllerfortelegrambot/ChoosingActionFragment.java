package site.alexkononsol.controllerfortelegrambot;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class ChoosingActionFragment extends Fragment {
    private Button postButton;
    private Button putButton;
    private Button delButton;

    static interface Listener {
        void actionChoose(String action);
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
        return inflater.inflate(R.layout.fragment_choosing_action, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //button get
        Button getButton = getView().findViewById(R.id.buttonGet);
        getButton.setOnClickListener(v -> listener.actionChoose("get"));

        //button search
        Button search = getView().findViewById(R.id.buttonSearch);
        search.setOnClickListener(v -> listener.actionChoose("search"));

        //button post
        postButton = getView().findViewById(R.id.buttonPost);
        postButton.setOnClickListener(v -> listener.actionChoose("post"));

        //button put
        putButton = getView().findViewById(R.id.buttonPut);
        putButton.setOnClickListener(v -> listener.actionChoose("put"));

        //button del
        delButton = getView().findViewById(R.id.buttonDel);
        delButton.setOnClickListener(v -> listener.actionChoose("del"));
    }
    @Override
    public void onResume() {
        super.onResume();
        String userLogin = SettingsManager.getSettings().getUserName();
        //if the user is not logged in to the account, the buttons for adding, changing and deleting information in the database are not active
        if(userLogin==null){
            putButton.setEnabled(false);
            postButton.setEnabled(false);
            delButton.setEnabled(false);
        }
    }
}