package team.dingding.musicgloves.music.impl;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Toast;

import java.util.HashMap;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.music.intf.IPlayMusic;

/**
 * Created by guoyanchang on 2014/7/4.
 */

public class MusicControlImpl implements IPlayMusic {
    SoundPool soundPool;
    int loadcount=0;
    int currplay[];
    boolean loadsign=false;
    AudioManager am;
    Context mContext;
    HashMap<Integer, Integer> soundMap;
    //载入音源
    public MusicControlImpl(Context context){
        mContext=context;
        currplay=new int[8];
        for(int i=0;i<8;i++)
            currplay[i]=0;
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM,10);
        soundMap = new HashMap<Integer, Integer>();
        am=(AudioManager) context.getSystemService(context.AUDIO_SERVICE);
    }
    public boolean load(){
        loadsign=false;
        loadcount=0;
        final ProgressDialog dialog=ProgressDialog.show(mContext,
                "loading music","wait...",true);

//        soundMap.put(1, soundPool.load(mContext, R.raw.doo, 1));
//        soundMap.put(2, soundPool.load(mContext, R.raw.re, 1));
//        soundMap.put(3, soundPool.load(mContext, R.raw.mi, 1));
//        soundMap.put(4, soundPool.load(mContext, R.raw.fa, 1));
//        soundMap.put(5, soundPool.load(mContext, R.raw.so, 1));
//        soundMap.put(6, soundPool.load(mContext, R.raw.la, 1));
//        soundMap.put(7, soundPool.load(mContext, R.raw.xi, 1));
//        soundMap.put(8, soundPool.load(mContext, R.raw.hdo, 1));

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
                loadcount = loadcount + 1;
                if (loadcount == 8) {
                    dialog.dismiss();
                    loadsign=true;
                    }
            }
        });
        return loadsign;
    }
    //播放某个音乐
    public void play(int music){
        currplay[music]=soundPool.play(soundMap.get(music),1,1,0,0,1);
    }
    //停止播放某个音乐
    public void stop(int music){
        soundPool.stop(music);
    }
    //停止播放所有音乐
    public void stopAll(){
    //本来打算清理掉所有声音资源，但是考虑到声音并不多，直接一个一个关闭就行了
        for(int i=0;i<8;i++)
            soundPool.stop(i);
    }
    //设置音量（可以先不写）
    public void setVolume(int value){

    }
    //获得当前音量值
    public int getVolume(){
        int current =am.getStreamVolume( AudioManager.STREAM_SYSTEM );
        return current;
    }
}
