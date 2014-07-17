package team.dingding.musicgloves.music.impl;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.HashMap;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.music.intf.IPlayMusic;

/**
 * Created by guoyanchang on 2014/7/4.
 */

public class MusicControlImpl implements IPlayMusic {
    SoundPool soundPool;
    SoundPool soundPool2;
    int loadcount=0;
    int currplay[];

    boolean loadsign=false;
    AudioManager am;
    Context mContext;
    HashMap<Integer, Integer> soundMap;
    //载入音源
    public MusicControlImpl(Context context){
        mContext=context;
        currplay=new int[9];
        for(int i=0;i<8;i++)
            currplay[i]=0;
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,10);
        soundPool2 = new SoundPool(10, AudioManager.STREAM_MUSIC,10);
        soundMap = new HashMap<Integer, Integer>();
        am=(AudioManager) context.getSystemService(context.AUDIO_SERVICE);
    }
    public boolean load(int kind){
        loadsign=false;
        loadcount=0;
        final ProgressDialog dialog=ProgressDialog.show(mContext,
                "loading music","wait...",true);
        switch (kind){

            case 0:
                soundMap.put(1, soundPool.load(mContext, R.raw.cdoo, 1));
                soundMap.put(2, soundPool.load(mContext, R.raw.cfa, 1));
                soundMap.put(3, soundPool.load(mContext, R.raw.chdo, 1));
                soundMap.put(4, soundPool.load(mContext, R.raw.cla, 1));
                soundMap.put(5, soundPool2.load(mContext, R.raw.cmi, 1));
                soundMap.put(6, soundPool2.load(mContext, R.raw.cre, 1));
                soundMap.put(7, soundPool2.load(mContext, R.raw.cso, 1));
                soundMap.put(8, soundPool2.load(mContext, R.raw.cxi, 1));
                break;
            case 1:
                soundMap.put(1, soundPool.load(mContext, R.raw.sound_piano_25, 1));
                soundMap.put(2, soundPool.load(mContext, R.raw.sound_piano_26, 1));
                soundMap.put(3, soundPool.load(mContext, R.raw.sound_piano_27, 1));
                soundMap.put(4, soundPool.load(mContext, R.raw.sound_piano_28, 1));
                soundMap.put(5, soundPool2.load(mContext, R.raw.sound_piano_29, 1));
                soundMap.put(6, soundPool2.load(mContext, R.raw.sound_piano_30, 1));
                soundMap.put(7, soundPool2.load(mContext, R.raw.sound_piano_31, 1));
                soundMap.put(8, soundPool2.load(mContext, R.raw.sound_piano_32, 1));
                break;
            case 2:
                soundMap.put(1, soundPool.load(mContext, R.raw.sound_drum_1, 1));
                soundMap.put(2, soundPool.load(mContext, R.raw.sound_drum_2, 1));
                soundMap.put(3, soundPool.load(mContext, R.raw.sound_drum_3, 1));
                soundMap.put(4, soundPool.load(mContext, R.raw.sound_drum_4, 1));
                soundMap.put(5, soundPool2.load(mContext, R.raw.sound_drum_5, 1));
                soundMap.put(6, soundPool2.load(mContext, R.raw.sound_drum_6, 1));
                soundMap.put(7, soundPool2.load(mContext, R.raw.sound_drum_7, 1));
                soundMap.put(8, soundPool2.load(mContext, R.raw.sound_drum_8, 1));
                break;
            case 3:
                soundMap.put(1, soundPool.load(mContext, R.raw.guitar1_1, 1));
                soundMap.put(2, soundPool.load(mContext, R.raw.guitar1_2, 1));
                soundMap.put(3, soundPool.load(mContext, R.raw.guitar1_3, 1));
                soundMap.put(4, soundPool.load(mContext, R.raw.guitar1_4, 1));
                soundMap.put(5, soundPool2.load(mContext, R.raw.guitar1_5, 1));
                soundMap.put(6, soundPool2.load(mContext, R.raw.guitar1_6, 1));
                soundMap.put(7, soundPool2.load(mContext, R.raw.guitar1_7, 1));
                soundMap.put(8, soundPool2.load(mContext, R.raw.guitar1_8, 1));


        }

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
        soundPool2.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
                loadcount = loadcount + 1;
                if (loadcount == 8) {
                    dialog.dismiss();
                    loadsign=true;
                }
            }
        });

        return true;
    }
    //播放某个音乐
    public void play(int music){
        if(music<=4)
            currplay[music-1]=soundPool.play(soundMap.get(music),1,1,0,0,1);
        else
            currplay[music-1]=soundPool2.play(soundMap.get(music),1,1,0,0,1);
    }
    //停止播放某个音乐
    public void stop(int music){
        if(music<=4)
            soundPool.stop(currplay[music-1]);
        else
            soundPool2.stop(currplay[music-1]);
    }
    //停止播放所有音乐
    public void stopAll(){
    //本来打算清理掉所有声音资源，但是考虑到声音并不多，直接一个一个关闭就行了
        for(int i=1;i<=8;i++)
            stop(i);
    }
    //设置音量
    public void setVolume(int value){
        am.setStreamVolume(AudioManager.STREAM_MUSIC, value, 0);
    }

    //获得当前音量值
    public int getVolume(){
        int current =am.getStreamVolume( AudioManager.STREAM_MUSIC);
        return current;
    }

    //把音乐音量调节和Seekbar联系起来
    public void volumeMatch(SeekBar seek){
        int mVolume = getVolume(); //获取当前音乐音量
        seek.setMax(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)); //SEEKBAR设置为音量的最大阶数
        seek.setProgress(mVolume); //设置seekbar为当前音量进度

    }


}
