package team.dingding.musicgloves.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.music.imp.MusicScore;
import team.dingding.musicgloves.music.intf.IMusicScore;
import team.dingding.musicgloves.music.intf.IPlayMusic;

/**
 * Created by Elega on 2014/7/8.
 */

//[乐谱]页面中的内容
public class MusicscoreFragment extends MainActivity.PlaceholderFragment {

    private EditText et=null;
    private TextView tv=null;
    private TextView tvSupportMode =null;
    private TextView tvPlayMusic =null;
    private Handler handler;

    private ImageView ivMsPlay;
    private ImageView ivMsChoose;
    private ImageView ivMsMake;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_musicscore, container, false);
        tv=(TextView)rootView.findViewById(R.id.textMsMakeorsave);
        tvSupportMode=(TextView)rootView.findViewById(R.id.textMsChoose);
        tvPlayMusic=(TextView)rootView.findViewById(R.id.textMsPlay);

        ivMsPlay=((ImageView)rootView.findViewById(R.id.ivMsPlay));
        ivMsPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivMsPlayOnClick(v);
            }
        });


        ivMsChoose=((ImageView)rootView.findViewById(R.id.ivMsChoose));
        ivMsChoose.setOnClickListener(new View.OnClickListener() {
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


        ivMsMake=(ImageView)rootView.findViewById(R.id.ivMsMake);
        ivMsMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivMsMakeOnClick(v);
            }
        });

        handler=new Handler();
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

    //播放乐谱
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

                                    if (playMusicScore(filesname[which])) {
                                        Toast.makeText(context, "开始播放", Toast.LENGTH_SHORT).show();
                                        tvPlayMusic.setText("停止播放");
                                        ivMsPlay.setImageDrawable(getResources().getDrawable(R.drawable.pic_stopms));
                                    }
                                    else{
                                        Toast.makeText(getMainActivity(), "乐谱文件为空或文件不能识别", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    )
                    .setNegativeButton("取消", null)
                    .show();
        }
        else{
            stopPlayMusicScore();
        }
    }

    //选择乐谱进入辅助模式
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
                                    if (loadMusicScore(filesname[which])) {
                                        Toast.makeText(context, "选择成功", Toast.LENGTH_SHORT).show();
                                        tvSupportMode.setText("停止辅助模式");
                                        ivMsChoose.setImageDrawable(getResources().getDrawable(R.drawable.pic_stopms));
                                    }
                                    else{
                                        Toast.makeText(getMainActivity(), "乐谱文件为空或文件不能识别", Toast.LENGTH_SHORT).show();
                                    }

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
            ivMsChoose.setImageDrawable(getResources().getDrawable(R.drawable.pic_chosems));

        }
//        Random r=new Random();
//        ms.append(Math.abs(r.nextInt()%8)+1);

    }

    //删除乐谱
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

    //制作乐谱
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
            ivMsMake.setImageDrawable(getResources().getDrawable(R.drawable.pic_savems) );

        } else {
            final IMusicScore ms=getMainActivity().getMusicScore();
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
                                    if (et.getText().toString().equals("")) {
                                        Toast.makeText(et.getContext(), "保存失败，文件名不能为空", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(et.getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(et.getContext(), "保存失败", Toast.LENGTH_SHORT).show();
                            }
                            tv.setText("制作乐谱");
                            ivMsMake.setImageDrawable(getResources().getDrawable(R.drawable.pic_makems));
                            getMainActivity().setMusicScore(null);
                            getMainActivity().msState = MainActivity.MusicScoreState.Idle;
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

    //调用某乐谱执行播放
    private boolean playMusicScore(String fileName){
        IMusicScore ms=MusicScore.fromFile(getActivity().getBaseContext(), fileName);
        if (ms==null){
            return false;
        }
        final ProgressDialog prompt = ProgressDialog.show(getMainActivity(),
                "正在载入音源", "请稍后...", true);
        prompt.show();
        IPlayMusic mc=getMainActivity().getMusicControl();
        getMainActivity().setMusicScore(ms);
        ms.changeMusic(mc,new Runnable() {
            @Override
            public void run() {
                prompt.dismiss();
                getMainActivity().getMusicScore().play(getMainActivity().getMusicControl(),
                        new Runnable() {
                            @Override
                            public void run() {
                                stopPlayMusicScore();

                            }
                        });
                getMainActivity().msState= MainActivity.MusicScoreState.Play;

            }
        });
        return true;
    }

    //加载某乐谱
    private boolean loadMusicScore(String fileName){
        IMusicScore ms=MusicScore.fromFile(getActivity().getBaseContext(), fileName);
        if (ms==null){
            return false;
        }
        final ProgressDialog prompt = ProgressDialog.show(getMainActivity(),
                "正在载入音源", "请稍后...", true);
        prompt.show();
        IPlayMusic mc=getMainActivity().getMusicControl();
        getMainActivity().setMusicScore(ms);
        ms.changeMusic(mc,new Runnable() {
            @Override
            public void run() {
                prompt.dismiss();
                getMainActivity().msState= MainActivity.MusicScoreState.Support;
            }
        });
        return true;
    }


    //停止播放
    private void stopPlayMusicScore(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                getMainActivity().getMusicScore().stop();
                getMainActivity().setMusicScore(null);
                getMainActivity().msState= MainActivity.MusicScoreState.Idle;
                tvPlayMusic.setText("播放乐谱");
                ivMsPlay.setImageDrawable(getResources().getDrawable(R.drawable.pic_playms));

            }
        });
    }

    public void updateText(){
        tv.setText("制作乐谱");
        tvPlayMusic.setText("播放乐谱");
        tvSupportMode.setText("选择辅助模式乐谱");
        ivMsPlay.setImageDrawable(getResources().getDrawable(R.drawable.pic_playms));
        ivMsChoose.setImageDrawable(getResources().getDrawable(R.drawable.pic_chosems));
        ivMsMake.setImageDrawable(getResources().getDrawable(R.drawable.pic_makems) );

        switch (getMainActivity().msState){
            case Make:
                tv.setText("保存乐谱");
                ivMsMake.setImageDrawable(getResources().getDrawable(R.drawable.pic_savems) );
                break;
            case Play:
                tvPlayMusic.setText("停止演奏");
                ivMsPlay.setImageDrawable(getResources().getDrawable(R.drawable.pic_stopms) );
                break;
            case Support:
                tvSupportMode.setText("停止辅助模式");
                ivMsChoose.setImageDrawable(getResources().getDrawable(R.drawable.pic_stopms) );
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
