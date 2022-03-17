package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import site.alexkononsol.controllerfortelegrambot.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ErrorFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ErrorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String description;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment ErrorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ErrorFragment newInstance(String param1) {
        ErrorFragment fragment = new ErrorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ErrorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            description = getArguments().getString(ARG_PARAM1);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView textView = getView().findViewById(R.id.error_fragment_text);
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(description);
            }
        });
    }
}