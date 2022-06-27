package site.alexkononsol.controllerfortelegrambot;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ServerResponse;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestToServer;
import site.alexkononsol.controllerfortelegrambot.ui.fragments.ErrorFragment;


public class GetFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        Button getButton = (Button) view.findViewById(R.id.buttonGet);
        getButton.setOnClickListener(v -> new Thread(() -> {
            TextView getTextView = (TextView) view.findViewById(R.id.getRequest);
            String nameCity = getTextView.getText().toString();
            RetrofitRequestToServer requestToServer = new RetrofitRequestToServer();
            ServerResponse serverResponse = requestToServer.getCity(nameCity);
            if (serverResponse.getCode() == 200) {
                Intent intent = new Intent(view.getContext(), ViewCityActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("city", nameCity);
                intent.putExtras(bundle);
                startActivity(intent);
            } else
                transactionFragment(ErrorFragment.newInstance(serverResponse.getData().toString()));
        }).start());
    }

    private void transactionFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.get_result_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }
}