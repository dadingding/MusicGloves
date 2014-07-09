package team.dingding.musicgloves.music.intf;

/**
 * Created by Elega on 2014/7/7.
 */
public interface IPlayMusic {
    //载入音源
    public boolean load(int kind);
    //播放某个音乐
    public void play(int music);
    //停止播放某个音乐
    public void stop(int music);
    //停止播放所有音乐
    public void stopAll();
    //设置音量（可以先不写）
    public void setVolume(int value);
    //获得当前音量值
    public int getVolume();
}
