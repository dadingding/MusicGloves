package team.dingding.musicgloves.暂时未完成;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import team.dingding.musicgloves.R;


/**
 * Created by Elega on 2014/7/8.
 */
public class SettingFragment extends MyActivity.PlaceholderFragment {
    Spinner spInstrument;
    Spinner spScale;
    ArrayAdapter adInstrument;
    ArrayAdapter adScale;
    TextView volume;
    SeekBar seekBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        loadSpinner(rootView);
        return rootView;
    }

    public static MyActivity.PlaceholderFragment newInstance(int sectionNumber) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private void loadSpinner(View v){
        spInstrument=(Spinner)v.findViewById(R.id.spinnerSettingInstrument);
        adInstrument= ArrayAdapter.createFromResource(this.getActivity().getBaseContext(), R.array.instruments, android.R.layout.simple_spinner_item);
        adInstrument.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInstrument.setAdapter(adInstrument);
        spInstrument.setOnItemSelectedListener(null);
        spInstrument.setVisibility(View.VISIBLE);

        spScale=(Spinner)v.findViewById(R.id.spinnerSettingScales);
        adScale= ArrayAdapter.createFromResource(this.getActivity().getBaseContext(), R.array.scales, android.R.layout.simple_spinner_item);
        adScale.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spScale.setAdapter(adScale);
        spScale.setOnItemSelectedListener(null);
        spScale.setVisibility(View.VISIBLE);

        volume=(TextView)v.findViewById(R.id.textSettingVolume);
        seekBar=(SeekBar)v.findViewById(R.id.seekbarSettingVolume);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                volume.setText("音量：" + i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

}
