package team.dingding.musicgloves.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.music.impl.MusicControlImpl;


/**
 * Created by Elega on 2014/7/8.
 */
public class SettingFragment extends MainActivity.PlaceholderFragment {
    Spinner spInstrument;
    Spinner spScale;
    ArrayAdapter adInstrument;
    ArrayAdapter adScale;
    TextView volume;
    SeekBar seekBar;
    MusicControlImpl sound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        loadSpinner(rootView);
        return rootView;
    }

    public static MainActivity.PlaceholderFragment newInstance(int sectionNumber) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private void loadSpinner(View v){
        sound = getMainActivity().getMusicControl();
        spInstrument=(Spinner)v.findViewById(R.id.spinnerSettingInstrument);
        adInstrument= ArrayAdapter.createFromResource(this.getActivity().getBaseContext(), R.array.instruments, android.R.layout.simple_spinner_item);
        adInstrument.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInstrument.setAdapter(adInstrument);
        spInstrument.setSelection(getMainActivity().getSource());
        spInstrument.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                //Log.d("mark", "onItemSelected1() is invoked!");
                sound.load(arg2);
                getMainActivity().setSource(arg2);
                arg0.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spInstrument.setVisibility(View.VISIBLE);

        spScale=(Spinner)v.findViewById(R.id.spinnerSettingScales);
        adScale= ArrayAdapter.createFromResource(this.getActivity().getBaseContext(), R.array.scales, android.R.layout.simple_spinner_item);
        adScale.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spScale.setAdapter(adScale);
        spScale.setSelection(getMainActivity().getScale());
        spScale.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                getMainActivity().setScale(arg2);
                arg0.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spScale.setVisibility(View.VISIBLE);

        volume=(TextView)v.findViewById(R.id.textSettingVolume);
        seekBar=(SeekBar)v.findViewById(R.id.seekbarSettingVolume);
        sound.volumeMatch(seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                volume.setText("音量：" + i);
                sound.setVolume(i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

}
