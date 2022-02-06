package site.alexkononsol.controllerfortelegrambot;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ChoosingActionFragment extends Fragment {
    private View view;

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
        view = getView();
        //button get
        Button getButton = view.findViewById(R.id.buttonGet);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.actionChoose("get");
            }
        });

        //button search
        Button search = view.findViewById(R.id.buttonSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.actionChoose("search");
            }
        });

        //button post
        Button post = view.findViewById(R.id.buttonPost);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.actionChoose("post");
            }
        });

        //button put
        Button put = view.findViewById(R.id.buttonPut);
        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.actionChoose("put");
            }
        });

        //button del
        Button del = view.findViewById(R.id.buttonDel);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.actionChoose("del");
            }
        });
    }
}