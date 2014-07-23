package team.dingding.musicgloves.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.music.impl.MusicControl;


/**
 * Created by Elega on 2014/7/8.
 */
public class SettingFragment extends MainActivity.PlaceholderFragment {

    SeekBar seekBar;
    MusicControl sound;
    ImageView ivSettingSetAboutus;
    TextView textSettingSetVolume;
    TextView textSettingSetScale;
    TextView textSettingSetInstrument;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        loadSpinner(rootView);
        ((ImageView)rootView.findViewById(R.id.ivSettingSetInstrument)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivSettingSetInstrumentOnClick(view);
            }
        });

        ivSettingSetAboutus=((ImageView)rootView.findViewById(R.id.ivSettingSetAboutus));
        ivSettingSetAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivSettingSetAboutusOnClick(view);
            }
        });

        ((ImageView)rootView.findViewById(R.id.ivSettingSetScale)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivSettingSetScaleOnClick(view);
            }
        });

        textSettingSetInstrument=(TextView)rootView.findViewById(R.id.textSettingSetInstrument);
        textSettingSetScale=(TextView)rootView.findViewById(R.id.textSettingSetScale);
        textSettingSetVolume=(TextView)rootView.findViewById(R.id.textSettingSetVolume);
        updateText();

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
        seekBar=(SeekBar)v.findViewById(R.id.seekbarSettingVolume);
        sound.volumeMatch(seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sound.setVolume(i);
                updateText();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
    private void ivSettingSetInstrumentOnClick(final View v) {
        final String[] instruments=getResources().getStringArray(R.array.instruments);
        final String[] instrumentsName={"Magic","Piano","Drum","Guitar"};

        final int source=getMainActivity().getSource();

        new AlertDialog.Builder(v.getContext()).setTitle("选择乐器").setIcon(
                android.R.drawable.ic_dialog_info).setSingleChoiceItems(
                instruments, source,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (which != source) {
                            final ProgressDialog prompt = ProgressDialog.show(getMainActivity(),
                                    "正在载入音源", "请稍后...", true);
                            prompt.show();
                            if (instrumentsName[which].equals("Magic") || instrumentsName[which].equals("Drum")){
                                getMainActivity().setScale(0);
                            }
                            sound.load(instrumentsName[which], getMainActivity().getScale(),new Runnable() {
                                @Override
                                public void run() {
                                    prompt.dismiss();
                                }
                            });
                            getMainActivity().setSource(which);
                            updateText();
                        }
                    }
                }
        ).setNegativeButton("取消", null).show();
    }

    private void ivSettingSetScaleOnClick(final  View v){
        final String[] scale={"0","1","2"};
        final String[] instrumentsName={"Magic","Piano","Drum","Guitar"};

        final int nowsource=getMainActivity().getSource();
        final int nowscale=getMainActivity().getScale();
        if (instrumentsName[nowsource].equals("Magic") || instrumentsName[nowsource].equals("Drum")){
            Toast.makeText(v.getContext(),"当前模式下的乐器不支持调整音调", Toast.LENGTH_LONG).show();
            return;
        }
        new AlertDialog.Builder(v.getContext()).setTitle("选择音调").setIcon(
                android.R.drawable.ic_dialog_info).setSingleChoiceItems(
                scale, nowscale,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        Toast.makeText(v.getContext(), instruments[which], Toast.LENGTH_LONG).show();
                        if (which != nowscale) {
                            final ProgressDialog prompt = ProgressDialog.show(getMainActivity(),
                                    "正在载入音源", "请稍后...", true);
                            prompt.show();
                            sound.load(instrumentsName[nowsource], which,new Runnable() {
                                @Override
                                public void run() {
                                    prompt.dismiss();
                                }
                            });
                            getMainActivity().setScale(which);
                            updateText();

                        }
                    }
                }
        ).setNegativeButton("取消", null).show();
    }

    private void ivSettingSetAboutusOnClick(final View v){
        ivSettingSetAboutus.setImageDrawable(getResources().getDrawable(R.drawable.pic_members));

    }

    private void updateText(){
        final String[] instruments=getResources().getStringArray(R.array.instruments);
        textSettingSetInstrument.setText("乐器 "+instruments[getMainActivity().getSource()] );
        textSettingSetScale.setText("音调 "+getMainActivity().getScale() );
        textSettingSetVolume.setText("音量 " + seekBar.getProgress());

    }
}
