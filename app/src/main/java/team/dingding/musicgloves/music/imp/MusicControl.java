package team.dingding.musicgloves.music.imp;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.SeekBar;

import java.util.HashMap;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.music.intf.IPlayMusic;

/**
 * Created by guoyanchang on 2014/7/4.
 */


public class MusicControl implements IPlayMusic{
    SoundPool soundPool;
    SoundPool soundPool2;
    SoundPool soundPool3;
    SoundPool soundPool4;

    int loadcount=0;
    int currplay[];
    String currInstrument=null;
    int currscale;
    boolean loadsign=false;
    AudioManager am;
    Context mContext;
    HashMap<Integer, Integer> soundMap;
    //载入音源
    public MusicControl(Context context){
        mContext=context;
        currplay=new int[21];
        for(int i=0;i<10;i++)
            currplay[i]=0;
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,10);
        soundPool2 = new SoundPool(10, AudioManager.STREAM_MUSIC,10);

        soundPool3 = new SoundPool(10, AudioManager.STREAM_MUSIC,10);
        soundPool4 = new SoundPool(10, AudioManager.STREAM_MUSIC,10);


        soundMap = new HashMap<Integer, Integer>();
        am=(AudioManager) context.getSystemService(context.AUDIO_SERVICE);
    }


    @Override
    public boolean load(String name, int scale){
            load(name,scale,null);
        return true;
    }


    //播放某个音乐

    public boolean loadall_piano(final Runnable after)
    {
        loadsign=false;
        loadcount=0;
        currInstrument="Piano2";
        currscale=1;
        soundMap.put(1, soundPool.load(mContext, R.raw.sound_piano_01, 1));
        soundMap.put(2, soundPool.load(mContext, R.raw.sound_piano_02, 1));
        soundMap.put(3, soundPool.load(mContext, R.raw.sound_piano_03, 1));
        soundMap.put(4, soundPool.load(mContext, R.raw.sound_piano_04, 1));
        soundMap.put(5, soundPool.load(mContext, R.raw.sound_piano_05, 1));
        soundMap.put(6, soundPool2.load(mContext, R.raw.sound_piano_06, 1));
        soundMap.put(7, soundPool2.load(mContext, R.raw.sound_piano_07, 1));

        soundMap.put(8, soundPool2.load(mContext, R.raw.sound_piano_11, 1));
        soundMap.put(9, soundPool2.load(mContext, R.raw.sound_piano_12, 1));
        soundMap.put(10, soundPool2.load(mContext, R.raw.sound_piano_13, 1));
        soundMap.put(11, soundPool3.load(mContext, R.raw.sound_piano_14, 1));
        soundMap.put(12, soundPool3.load(mContext, R.raw.sound_piano_15, 1));
        soundMap.put(13, soundPool3.load(mContext, R.raw.sound_piano_16, 1));
        soundMap.put(14, soundPool3.load(mContext, R.raw.sound_piano_17, 1));

        soundMap.put(15, soundPool3.load(mContext, R.raw.sound_piano_21, 1));
        soundMap.put(16, soundPool4.load(mContext, R.raw.sound_piano_22, 1));
        soundMap.put(17, soundPool4.load(mContext, R.raw.sound_piano_23, 1));
        soundMap.put(18, soundPool4.load(mContext, R.raw.sound_piano_24, 1));
        soundMap.put(19, soundPool4.load(mContext, R.raw.sound_piano_25, 1));
        soundMap.put(20, soundPool4.load(mContext, R.raw.sound_piano_26, 1));
        soundMap.put(21, soundPool4.load(mContext, R.raw.sound_piano_27, 1));


        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
                loadcount = loadcount + 1;
                if (loadcount==21)
                    if (after!=null) after.run();
                    loadsign=true;
            }
        });

        soundPool2.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
                loadcount = loadcount + 1;
                if (loadcount==21)
                    if (after!=null) after.run();
                loadsign=true;
            }
        });


        soundPool3.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
                loadcount = loadcount + 1;
                if (loadcount==21)
                    if (after!=null) after.run();
                loadsign=true;
            }
        });


        soundPool4.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
                loadcount = loadcount + 1;
                if (loadcount==21)
                    if (after!=null) after.run();
                loadsign=true;
            }
        });


        return true;
    }

    @Override
    public boolean load(String name, int scale, final Runnable after){
        loadsign=false;
        loadcount=0;
        currInstrument=name;
        currscale=scale;

        if(name.equals("Magic")) {

            soundMap.put(1, soundPool.load(mContext, R.raw.cdoo, 1));
            soundMap.put(2, soundPool.load(mContext, R.raw.cfa, 1));
            soundMap.put(3, soundPool.load(mContext, R.raw.chdo, 1));
            soundMap.put(4, soundPool.load(mContext, R.raw.cla, 1));
            soundMap.put(5, soundPool2.load(mContext, R.raw.cmi, 1));
            soundMap.put(6, soundPool2.load(mContext, R.raw.cre, 1));
            soundMap.put(7, soundPool2.load(mContext, R.raw.cso, 1));
            soundMap.put(8, soundPool2.load(mContext, R.raw.cxi, 1));
        }
        else if(name.equals("Piano")) {
            if(scale==0) {
                soundMap.put(1, soundPool.load(mContext, R.raw.sound_piano_01, 1));
                soundMap.put(2, soundPool.load(mContext, R.raw.sound_piano_02, 1));
                soundMap.put(3, soundPool.load(mContext, R.raw.sound_piano_03, 1));
                soundMap.put(4, soundPool.load(mContext, R.raw.sound_piano_04, 1));
                soundMap.put(5, soundPool.load(mContext, R.raw.sound_piano_05, 1));
                soundMap.put(6, soundPool2.load(mContext, R.raw.sound_piano_06, 1));
                soundMap.put(7, soundPool2.load(mContext, R.raw.sound_piano_07, 1));
                soundMap.put(8, soundPool2.load(mContext, R.raw.sound_piano_08, 1));
                soundMap.put(9, soundPool2.load(mContext, R.raw.sound_piano_09, 1));
                soundMap.put(10, soundPool2.load(mContext, R.raw.sound_piano_010, 1));
            }
            else if(scale==1) {
                soundMap.put(1, soundPool.load(mContext, R.raw.sound_piano_11, 1));
                soundMap.put(2, soundPool.load(mContext, R.raw.sound_piano_12, 1));
                soundMap.put(3, soundPool.load(mContext, R.raw.sound_piano_13, 1));
                soundMap.put(4, soundPool.load(mContext, R.raw.sound_piano_14, 1));
                soundMap.put(5, soundPool.load(mContext, R.raw.sound_piano_15, 1));
                soundMap.put(6, soundPool2.load(mContext, R.raw.sound_piano_16, 1));
                soundMap.put(7, soundPool2.load(mContext, R.raw.sound_piano_17, 1));
                soundMap.put(8, soundPool2.load(mContext, R.raw.sound_piano_18, 1));
                soundMap.put(9, soundPool2.load(mContext, R.raw.sound_piano_19, 1));
                soundMap.put(10, soundPool2.load(mContext, R.raw.sound_piano_110, 1));

            }
            if(scale==2) {
                soundMap.put(1, soundPool.load(mContext, R.raw.sound_piano_21, 1));
                soundMap.put(2, soundPool.load(mContext, R.raw.sound_piano_22, 1));
                soundMap.put(3, soundPool.load(mContext, R.raw.sound_piano_23, 1));
                soundMap.put(4, soundPool.load(mContext, R.raw.sound_piano_24, 1));
                soundMap.put(5, soundPool.load(mContext, R.raw.sound_piano_25, 1));
                soundMap.put(6, soundPool2.load(mContext, R.raw.sound_piano_26, 1));
                soundMap.put(7, soundPool2.load(mContext, R.raw.sound_piano_27, 1));
                soundMap.put(8, soundPool2.load(mContext, R.raw.sound_piano_28, 1));
                soundMap.put(9, soundPool2.load(mContext, R.raw.sound_piano_29, 1));
                soundMap.put(10, soundPool2.load(mContext, R.raw.sound_piano_210, 1));
            }
        }
        else if(name.equals("Drum")) {
            soundMap.put(1, soundPool.load(mContext, R.raw.sound_drum_1, 1));
            soundMap.put(2, soundPool.load(mContext, R.raw.sound_drum_2, 1));
            soundMap.put(3, soundPool.load(mContext, R.raw.sound_drum_3, 14));
            soundMap.put(4, soundPool.load(mContext, R.raw.sound_drum_4, 1));
            soundMap.put(5, soundPool2.load(mContext, R.raw.sound_drum_5, 1));
            soundMap.put(6, soundPool2.load(mContext, R.raw.sound_drum_6, 1));
            soundMap.put(7, soundPool2.load(mContext, R.raw.sound_drum_7, 1));
            soundMap.put(8, soundPool2.load(mContext, R.raw.sound_drum_8, 1));
        }
        else if(name.equals("Guitar")) {
            if (scale == 0) {
                soundMap.put(1, soundPool.load(mContext, R.raw.guitar0_1, 1));
                soundMap.put(2, soundPool.load(mContext, R.raw.guitar0_2, 1));
                soundMap.put(3, soundPool.load(mContext, R.raw.guitar0_3, 1));
                soundMap.put(4, soundPool.load(mContext, R.raw.guitar0_4, 1));
                soundMap.put(5, soundPool2.load(mContext, R.raw.guitar0_5, 1));
                soundMap.put(6, soundPool2.load(mContext, R.raw.guitar0_6, 1));
                soundMap.put(7, soundPool2.load(mContext, R.raw.guitar0_7, 1));
                soundMap.put(8, soundPool2.load(mContext, R.raw.guitar0_8, 1));
            } else if (scale == 1) {

                soundMap.put(1, soundPool.load(mContext, R.raw.guitar1_1, 1));
                soundMap.put(2, soundPool.load(mContext, R.raw.guitar1_2, 1));
                soundMap.put(3, soundPool.load(mContext, R.raw.guitar1_3, 1));
                soundMap.put(4, soundPool.load(mContext, R.raw.guitar1_4, 1));
                soundMap.put(5, soundPool2.load(mContext, R.raw.guitar1_5, 1));
                soundMap.put(6, soundPool2.load(mContext, R.raw.guitar1_6, 1));
                soundMap.put(7, soundPool2.load(mContext, R.raw.guitar1_7, 1));
                soundMap.put(8, soundPool2.load(mContext, R.raw.guitar1_8, 1));
            } else if (scale == 2) {
                soundMap.put(1, soundPool.load(mContext, R.raw.guitar2_1, 1));
                soundMap.put(2, soundPool.load(mContext, R.raw.guitar2_2, 1));
                soundMap.put(3, soundPool.load(mContext, R.raw.guitar2_3, 1));
                soundMap.put(4, soundPool.load(mContext, R.raw.guitar2_4, 1));
                soundMap.put(5, soundPool2.load(mContext, R.raw.guitar2_5, 1));
                soundMap.put(6, soundPool2.load(mContext, R.raw.guitar2_6, 1));
                soundMap.put(7, soundPool2.load(mContext, R.raw.guitar2_7, 1));
                soundMap.put(8, soundPool2.load(mContext, R.raw.guitar2_8, 1));
            }
        }
        else if(name.equals("Piano2"))
        {
            loadall_piano(after);
            return true;
        }

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
                loadcount = loadcount + 1;
                if(currInstrument.equals("Piano"))
                {
                    if (loadcount == 10) {
                        if (after!=null) after.run();
                        loadsign=true;
                    }
                }
                else {
                    if (loadcount == 8) {
                        if (after != null) after.run();
                        loadsign = true;
                    }
                }
            }
        });
        soundPool2.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
                loadcount = loadcount + 1;
                if(currInstrument.equals("Piano"))
                {
                    if (loadcount == 10) {
                        if (after!=null) after.run();
                        loadsign=true;
                    }
                }
                else {
                    if (loadcount == 8) {
                        if (after != null) after.run();
                        loadsign = true;
                    }
                }
            }
        });

        return true;
    }




    @Override
    public void play(int music,int scale){
        if(currInstrument.equals("Piano2"))
        {
            int now=music+scale*7;
            if (now <= 5)
                currplay[now - 1] = soundPool.play(soundMap.get(now), 1, 1, 0, 0, 1);
            else if(now<=10)
                currplay[now - 1] = soundPool2.play(soundMap.get(now), 1, 1, 0, 0, 1);
            else if(now<=15)
                currplay[now - 1] = soundPool3.play(soundMap.get(now), 1, 1, 0, 0, 1);
            else if(now<=21)
                currplay[now - 1] = soundPool4.play(soundMap.get(now), 1, 1, 0, 0, 1);

        }
    }



    @Override
    public void play(int music){
        if(currInstrument.equals("Guitar"))
            stopAll();
        if(currInstrument.equals("Piano"))
        {
            if (music <= 5)
                currplay[music - 1] = soundPool.play(soundMap.get(music), 1, 1, 0, 0, 1);
            else
                currplay[music - 1] = soundPool2.play(soundMap.get(music), 1, 1, 0, 0, 1);
        }
        else
        {
            if (music <= 4)
                currplay[music - 1] = soundPool.play(soundMap.get(music), 1, 1, 0, 0, 1);
            else if (music <= 8)
                currplay[music - 1] = soundPool2.play(soundMap.get(music), 1, 1, 0, 0, 1);
        }
    }
    //停止播放某个音乐
    @Override
    public void stop(int music){
        if(currInstrument.equals("Piano")){
            if(music<=5)
                soundPool.stop(currplay[music-1]);
            else
                soundPool2.stop(currplay[music-1]);
        }
        else {
            if (music <= 4)
                soundPool.stop(currplay[music - 1]);
            else
                soundPool2.stop(currplay[music - 1]);
        }
    }
    //停止播放所有音乐
    @Override
    public void stopAll(){
    //本来打算清理掉所有声音资源，但是考虑到声音并不多，直接一个一个关闭就行了
        for(int i=1;i<=10;i++)
            stop(i);
    }
    //设置音量
    @Override
    public void setVolume(int value){
        am.setStreamVolume(AudioManager.STREAM_MUSIC, value, 0);
    }

    //获得当前音量值
    @Override
    public int getVolume(){
        int current =am.getStreamVolume( AudioManager.STREAM_MUSIC);
        return current;
    }

    //把音乐音量调节和Seekbar联系起来
    @Override
    public void volumeMatch(SeekBar seek){
        int mVolume = getVolume(); //获取当前音乐音量
        seek.setMax(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)); //SEEKBAR设置为音量的最大阶数
        seek.setProgress(mVolume); //设置seekbar为当前音量进度

    }
    @Override
    public String getInstrument(){
        return currInstrument;
    }
     @Override
     public int getScale(){
         return currscale;
     }

    @Override
    public void setScale(int scale)
    {
        currscale=scale;
    }





}
