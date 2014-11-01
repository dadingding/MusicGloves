package team.dingding.musicgloves.music.intf;

import android.content.Context;

/**
 * Created by Elega on 2014/7/23.
 */
public interface IMusicScore {
    //开始乐谱计时
    void begin();

    //结束乐谱计时
    void end();

    //为乐谱追加音符
    //sound:音符
    //press:按下或弹起，按下为1，弹起为0
    void append(int sound, int press);

    //保存乐谱(要带后缀名)
    Boolean save(Context context, String filename);

    //播放乐谱，after:播放完成后要执行的内容
    void play(IPlayMusic pm, Runnable after);




        //播放下一个音符
    void playnext(IPlayMusic pm);



        //停止播放乐谱
    void stop();

    //判断是否已经播放完成
    boolean finished();

    //改变音源
    void changeMusic(IPlayMusic pm, Runnable after);
}
