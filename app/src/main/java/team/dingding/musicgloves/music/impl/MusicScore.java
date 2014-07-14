package team.dingding.musicgloves.music.impl;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Timer;


import team.dingding.musicgloves.music.intf.IPlayMusic;
import team.dingding.musicgloves.utils.ReschedulableTimerTask;
import team.dingding.musicgloves.utils.StopWatch;

/**
 * Created by Elega on 2014/7/10.
 */
public class MusicScore {
    //构造一个乐谱
    // Scale:音阶

    private StopWatch sp=new StopWatch();

    private Vector<Note> note;
    private String mScale;
    private String mInstrument;
    private boolean run=false;
    private long nowTime=0;
    private int nowPos=0;
    private TimerTask task=null;

    public MusicScore(String instrument,String scale){
        mScale=scale;
        mInstrument=instrument;
        note=new Vector<Note>();
    }

    public void begin(){
        sp.start();
        run=true;
    }
    public void end(){
        sp.stop();
    }
    public void append(int sound){
        if (!run){
            sp.start();
            run=true;
        }
        note.addElement(new Note(sp.getTime(),sound));
    }
    public Boolean save(Context context,String filename){
        String buf="";
        buf+=mInstrument+"\n";
        buf+=mScale+"\n";
        try {
            FileOutputStream ofs = context.openFileOutput(filename, Context.MODE_PRIVATE);
            for (int i=0;i<note.size();++i){
                Note t=note.get(i);
                buf+=t.time+" "+t.note+"\n";
            }
            ofs.write(buf.getBytes());
            ofs.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static MusicScore fromFile(Context context,String filename) {
        try {
            FileInputStream ifs = context.openFileInput(filename);
            byte[] buf=new byte[1024];
            String all="";
            int length;
            while ((length=ifs.read(buf))!=-1){
                all+=new String(buf,0,length);
            }
            ifs.close();
            String[] line=all.split("\n");
            if (line.length<=2) return null;
            else{
                String instrument=line[0];
                String scale=line[1];
                MusicScore result=new MusicScore(instrument,scale);
                for (int i=2;i<line.length;++i){
                    Note t= new Note(line[i]);
                    result.note.addElement(t);
                }
                return result;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getNoteCount(){
        return note.size();
    }

    public Note getNote(int location){
        return note.get(location);
    }


    //调试用
    public static void printAll(Context context,String filename){
        try {
            FileInputStream ifs = context.openFileInput(filename);
            byte[] buf=new byte[1024];
            String all="";
            int length;
            while ((length=ifs.read(buf))!=-1){
                all+=new String(buf,0,length);
            }
            ifs.close();
            String[] line=all.split("\n");
            for (int i=0;i<line.length;++i){
                Log.v("233",line[i]);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(final IPlayMusic pm){
        nowTime=0;
        nowPos=0;
        task=new TimerTask() {
            @Override
            public void run() {
                nowTime+=100;
                while(nowPos<note.size() && note.get(nowPos).time<nowTime){
                    pm.play(note.get(nowPos).note);
                    nowPos++;
                }
                if (nowPos>=note.size())
                    this.cancel();

            }
        };
        Timer t=new Timer();
        t.scheduleAtFixedRate(task,50,50);
    }

    public void stop(){
        if (task!=null)
            task.cancel();
    }



}
