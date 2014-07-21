package team.dingding.musicgloves.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.network.imp.ClientManager;

/**
 * Created by Elega on 2014/7/8.
 */
public class StateFragment extends MainActivity.PlaceholderFragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_state, container, false);
        return rootView;
    }

    public static MainActivity.PlaceholderFragment newInstance(int sectionNumber) {
        StateFragment fragment = new StateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


}
