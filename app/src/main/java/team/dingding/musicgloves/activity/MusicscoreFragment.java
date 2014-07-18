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

    private MusicScore ms=null;
    private EditText et=null;
    private TextView tv=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_musicscore, container, false);
        tv=(TextView)rootView.findViewById(R.id.textMsMakeorsave);

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


        return rootView;
    }

    public static MainActivity.PlaceholderFragment newInstance(int sectionNumber) {
        MusicscoreFragment fragment = new MusicscoreFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private void ivMsPlayOnClick(View v){
        final String[] filesname=MusicScore.listMsFile(v.getContext().getFilesDir());
        new AlertDialog.Builder(v.getContext())
                .setTitle("请选择")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(filesname, 0,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                playMusicScore(filesname[which]);
                            }
                        }
                )
                .setNegativeButton("取消", null)
                .show();

    }

    private void ivMsChooseOnClick(View v){
        Random r=new Random();
        ms.append(Math.abs(r.nextInt()%8)+1);

    }

    private void ivMsRemoveOnClick(View v){
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
                            }
                        }
                )
                .setNegativeButton("取消", null)
                .show();
    }

    private void ivMsMakeOnClick(View v) {
        if (tv.getText().toString().equals("制作乐谱")) {
            ms = new MusicScore("钢琴", "D大调");
            tv.setText("保存乐谱");
        } else {
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
                                }

                            } else {
                                Toast.makeText(et.getContext(), "保存失败", Toast.LENGTH_SHORT).show();
                            }
                            tv.setText("制作乐谱");

                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

    private void playMusicScore(String fileName){
        MusicScore.fromFile(getActivity().getBaseContext(),fileName).play(getMainActivity().getMusicControl());
    }






}
