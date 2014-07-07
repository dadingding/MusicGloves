package team.dingding.musicgloves.music.intf;

/**
 * Created by Elega on 2014/7/7.
 */
public interface IPlayMusic {
    //载入音源
    boolean load();
    //播放某个音乐
    void play(int music);
    //停止播放某个音乐
    void stop(int music);
    //停止播放所有音乐
    void stopAll();
    //设置音量（可以先不写）
    void setVolume(int value);
    //获得当前音量值
    int getVolume();
}
