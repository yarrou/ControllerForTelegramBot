package site.alexkononsol.controllerfortelegrambot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.RequestEncoder;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ServerResponse;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestToServer;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestType;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.ui.fragments.CityDescriptionFragment;
import site.alexkononsol.controllerfortelegrambot.ui.fragments.ErrorFragment;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

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
        String host = SettingsManager.getSettings().getHostName();
        getButton.setOnClickListener(v -> new Thread(() -> {
            try {
                TextView getTextView = (TextView) view.findViewById(R.id.getRequest);
                String nameCity = getTextView.getText().toString();
                String query = RequestEncoder.getRequest(nameCity);
                RequestToServer get = new RequestToServer(Constants.ENDPOINT_GET_CITY, RequestType.GET);
                get.addParam("city", query);
                get.addLangParam();
                ServerResponse response = get.send();
                if (response.getCode() == 200) {
                    transactionFragment(CityDescriptionFragment.newInstance(response.getData().toString()));
                } else
                    transactionFragment(ErrorFragment.newInstance(response.getData().toString()));
            } catch (IOException ex) {
                LogHelper.logError(GetFragment.this, ex.getMessage(), ex);
                transactionFragment(ErrorFragment.newInstance(getString(R.string.error) + " : " + ex.getMessage() + ex.toString()));
                Toast.makeText(view.getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
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