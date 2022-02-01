package site.alexkononsol.controllerfortelegrambot;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ChoosingActionFragment extends Fragment {
    private View view;

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
                onGetRequest();
            }
        });

        //button search
        Button search = view.findViewById(R.id.buttonSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch();
            }
        });

        //button post
        Button post = view.findViewById(R.id.buttonPost);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostRequest();
            }
        });

        //button put
        Button put = view.findViewById(R.id.buttonPut);
        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPutRequest();
            }
        });

        //button del
        Button del = view.findViewById(R.id.buttonDel);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelRequest();
            }
        });
    }
    public void onGetRequest(){
        Intent intent = new Intent(view.getContext(),GetActivity.class);
        startActivity(intent);
    }
    public void onPostRequest(){
        Intent intent = new Intent(view.getContext(),PostActivity.class);
        startActivity(intent);
    }
    public void onPutRequest(){
        Intent intent = new Intent(view.getContext(),PutActivity.class);
        startActivity(intent);
    }
    public void onDelRequest(){
        Intent intent = new Intent(view.getContext(), DelActivity.class);
        startActivity(intent);
    }
    public void onSearch(){
        Intent intent = new Intent(view.getContext(),SearchActivity.class);
        startActivity(intent);
    }
}