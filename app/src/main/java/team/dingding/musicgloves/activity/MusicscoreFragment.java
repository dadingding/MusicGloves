package team.dingding.musicgloves.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Random;
import java.util.Vector;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.music.impl.MusicScore;
import team.dingding.musicgloves.music.intf.IPlayMusic;
import team.dingding.musicgloves.protocol.intf.IProtocolController;

/**
 * Created by Elega on 2014/7/8.
 */
public class MusicscoreFragment extends MainActivity.PlaceholderFragment {

    private EditText et=null;
    private TextView tv=null;
    private TextView tvSupportMode =null;
    private TextView tvPlayMusic =null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_musicscore, container, false);
        tv=(TextView)rootView.findViewById(R.id.textMsMakeorsave);
        tvSupportMode=(TextView)rootView.findViewById(R.id.textMsChoose);
        tvPlayMusic=(TextView)rootView.findViewById(R.id.textMsPlay);


        ((ImageView)rootView.findViewById(R.id.ivMsPlay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivMsPlayOnClick(v);
            }
        });
        ((ImageView)rootView.findViewById(R.id.ivMsChoose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivMsChooseOnClick(v);
            }
        });
        ((ImageView)rootView.findViewById(R.id.ivMsRemove)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivMsRemoveOnClick(v);
            }
        });
        ((ImageView)rootView.findViewById(R.id.ivMsMake)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivMsMakeOnClick(v);
            }
        });

        updateText();
        return rootView;
    }

    public static MainActivity.PlaceholderFragment newInstance(int sectionNumber) {
        MusicscoreFragment fragment = new MusicscoreFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private void ivMsPlayOnClick(View v) {
        if (tvPlayMusic.getText().equals("播放乐谱")) {
            if (getMainActivity().msState == MainActivity.MusicScoreState.Support) {
                Toast.makeText(v.getContext(), "请先结束辅助演奏模式", Toast.LENGTH_LONG).show();
                return;
            }
            else if (getMainActivity().msState == MainActivity.MusicScoreState.Make) {
                Toast.makeText(v.getContext(), "请先结束乐谱制作", Toast.LENGTH_LONG).show();
                return;
            }
            final Context context = v.getContext();
            final String[] filesname = MusicScore.listMsFile(v.getContext().getFilesDir());
            new AlertDialog.Builder(v.getContext())
                    .setTitle("请选择")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setSingleChoiceItems(filesname, 0,
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    playMusicScore(filesname[which]);
                                    Toast.makeText(context, "开始播放", Toast.LENGTH_SHORT).show();
                                    tvPlayMusic.setText("停止播放");
                                }
                            }
                    )
                    .setNegativeButton("取消", null)
                    .show();
        }
        else{
            getMainActivity().getMusicScore().stop();
            getMainActivity().setMusicScore(null);
            getMainActivity().msState= MainActivity.MusicScoreState.Idle;
            tvPlayMusic.setText("播放乐谱");
        }
    }

    private void ivMsChooseOnClick(View v){


        if (tvSupportMode.getText().equals("选择辅助模式乐谱")) {
            if(getMainActivity().msState== MainActivity.MusicScoreState.Play){
                Toast.makeText(v.getContext(), "请等待当前乐谱播放完成", Toast.LENGTH_LONG).show();
                return;
            }
            else if(getMainActivity().msState== MainActivity.MusicScoreState.Make){
                Toast.makeText(v.getContext(), "请先结束乐谱制作", Toast.LENGTH_LONG).show();
                return;
            }


            final Context context = v.getContext();
            final String[] filesname = MusicScore.listMsFile(v.getContext().getFilesDir());
            new AlertDialog.Builder(v.getContext())
                    .setTitle("请选择")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setSingleChoiceItems(filesname, 0,
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getMainActivity().msState= MainActivity.MusicScoreState.Support;
                                    getMainActivity().setMusicScore(MusicScore.fromFile(context, filesname[which]));
//                                playMusicScore(filesname[which]);
                                    Toast.makeText(context, "选择成功", Toast.LENGTH_SHORT).show();
                                    tvSupportMode.setText("停止辅助模式");

                                }
                            }
                    )
                    .setNegativeButton("取消", null)
                    .show();
        }
        else{
            getMainActivity().msState= MainActivity.MusicScoreState.Idle;
            getMainActivity().setMusicScore(null);
            tvSupportMode.setText("选择辅助模式乐谱");
        }
//        Random r=new Random();
//        ms.append(Math.abs(r.nextInt()%8)+1);

    }

    private void ivMsRemoveOnClick(View v){
        final Context context=v.getContext();
        final String rootname=v.getContext().getFilesDir().getAbsolutePath()+"/";
        final String[] filesname=MusicScore.listMsFile(v.getContext().getFilesDir());
        new AlertDialog.Builder(v.getContext())
                .setTitle("请选择")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(filesname, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                File f=new File( rootname + filesname[which]);
                                f.delete();
                                Toast.makeText(context, "成功删除", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                .setNegativeButton("取消", null)
                .show();

    }

    private void ivMsMakeOnClick(View v) {

        if (tv.getText().toString().equals("制作乐谱")) {
            if (getMainActivity().msState== MainActivity.MusicScoreState.Support){
                Toast.makeText(v.getContext(), "请先结束辅助演奏模式", Toast.LENGTH_LONG).show();
                return;
            }
            else if(getMainActivity().msState== MainActivity.MusicScoreState.Play){
                Toast.makeText(v.getContext(), "请等待当前乐谱播放完成", Toast.LENGTH_LONG).show();
                return;
            }
            IPlayMusic pm=getMainActivity().getMusicControl();
            getMainActivity().setMusicScore(new MusicScore(pm.getInstrument(),pm.getScale()));
            getMainActivity().msState= MainActivity.MusicScoreState.Make;
            tv.setText("保存乐谱");
        } else {
            final MusicScore ms=getMainActivity().getMusicScore();
            et = new EditText(v.getContext());
            et.setSingleLine();
            new AlertDialog.Builder(v.getContext())
                    .setTitle("请输入")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (ms != null) {
                                if (ms.save(et.getContext(), et.getText().toString() + ".msc") == false) {
                                    Toast.makeText(et.getContext(), "保存失败", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(et.getContext(), "保存成功", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(et.getContext(), "保存失败", Toast.LENGTH_SHORT).show();
                            }
                            tv.setText("制作乐谱");
                            getMainActivity().setMusicScore(null);
                            getMainActivity().msState= MainActivity.MusicScoreState.Idle;
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

    private void playMusicScore(String fileName){
        getMainActivity().setMusicScore(MusicScore.fromFile(getActivity().getBaseContext(),fileName));
        getMainActivity().getMusicScore().play(getMainActivity().getMusicControl());
        getMainActivity().msState= MainActivity.MusicScoreState.Play;
    }

    public void updateText(){
        tv.setText("制作乐谱");
        tvPlayMusic.setText("播放乐谱");
        tvSupportMode.setText("选择辅助模式乐谱");
        switch (getMainActivity().msState){
            case Make:
                tv.setText("保存乐谱");
                break;
            case Play:
                tvPlayMusic.setText("停止演奏");
                break;
            case Support:
                tvSupportMode.setText("停止辅助模式");
                break;
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
/*
        MusicScore ms=getMainActivity().getMusicScore();
        if (ms!=null && getMainActivity().msState== MainActivity.MusicScoreState.Play ){
            ms.stop();
        }
    }
*/




}
