package team.dingding.musicgloves.music.intf;

import android.widget.SeekBar;

/**
 * Created by Elega on 2014/7/7.
 */
public interface IPlayMusic {
    //载入音源
    public boolean load(String name,int scale);
    public boolean load(String name,int scale,final Runnable after);
    //播放某个音乐
    public void play(int music,int scale);
    public void play(int music);
    //停止播放某个音乐
    public void stop(int music);
    //停止播放所有音乐
    public void stopAll();
    //设置音量（可以先不写）
    public void setVolume(int value);
    //获得当前音量值
    public int getVolume();
    //把音乐音量调节和Seekbar联系起来
    public void volumeMatch(SeekBar seek);
    //获得当前乐器
    public String getInstrument();
    //获得当前调值
    public  int getScale();
    //设置当前调值
    public void setScale(int scale);


}
