package team.dingding.musicgloves.暂时未完成;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team.dingding.musicgloves.R;

/**
 * Created by Elega on 2014/7/8.
 */
public class FuncFragment extends MyActivity.PlaceholderFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_func, container, false);
        return rootView;
    }

    public static MyActivity.PlaceholderFragment newInstance(int sectionNumber) {
        FuncFragment fragment = new FuncFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


}
