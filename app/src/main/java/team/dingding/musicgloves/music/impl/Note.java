package team.dingding.musicgloves.music.impl;

/**
 * Created by Elega on 2014/7/10.
 */
public class Note{
    public Note(Long v1,Integer v2){
        time=v1;
        note=v2;
    }
    public Note(String s){
        String[] tmp=s.split(" ");
        if (tmp.length==2){
            time=Long.valueOf(tmp[0]);
            note=Integer.valueOf(tmp[1]);
        }
    }
    public Long time;
    public Integer note;
}