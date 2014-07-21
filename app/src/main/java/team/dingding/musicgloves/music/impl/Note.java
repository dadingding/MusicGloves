package team.dingding.musicgloves.music.impl;

/**
 * Created by Elega on 2014/7/10.
 */
public class Note{
    public Note(Long v1,Integer v2,Integer v3){
        time=v1;
        note=v2;
        press=v3;
    }
    public Note(String s){
        String[] tmp=s.split(" ");
        if (tmp.length==3){
            time=Long.valueOf(tmp[0]);
            note=Integer.valueOf(tmp[1]);
            press=Integer.valueOf(tmp[2]);

        }
    }
    public Long time;
    public Integer note;
    public Integer press;
}