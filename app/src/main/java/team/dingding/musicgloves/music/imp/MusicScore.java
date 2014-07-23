package team.dingding.musicgloves.music.imp;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Timer;


import team.dingding.musicgloves.music.intf.IMusicScore;
import team.dingding.musicgloves.music.intf.IPlayMusic;
import team.dingding.musicgloves.utils.StopWatch;

/**
 * Created by Elega on 2014/7/10.
 */
public class MusicScore implements team.dingding.musicgloves.music.intf.IMusicScore {

    private StopWatch sp=new StopWatch();

    private Vector<Note> note;
    private int mScale;
    private String mInstrument;
    private boolean run=false;
    private long nowTime=0;
    private int nowPos=0;
    private TimerTask task=null;

    //构造一个乐谱
    // Scale:调值
    public MusicScore(String instrument,int scale){
        mScale=scale;
        mInstrument=instrument;
        note=new Vector<Note>();
    }

    //开始乐谱计时
    @Override
    public void begin(){
        sp.start();
        run=true;
    }

    //结束乐谱计时
    @Override
    public void end(){
        sp.stop();
    }

    //为乐谱追加音符
    //sound:音符
    //press:按下或弹起，按下为1，弹起为0
    @Override
    public void append(int sound, int press){
        if (!run){
            sp.start();
            run=true;
        }
        note.addElement(new Note(sp.getTime(),sound,press));
    }

    //保存乐谱(要带后缀名)
    @Override
    public Boolean save(Context context, String filename){
        String buf="";
        buf+=mInstrument+"\n";
        buf+=mScale+"\n";
        try {
            FileOutputStream ofs = context.openFileOutput(filename, Context.MODE_PRIVATE);
            for (int i=0;i<note.size();++i){
                Note t=note.get(i);
                buf+=t.time+" "+t.note+" " +t.press+ "\n";
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

    //从文件中获得一个乐谱
    //若获得失败，返回null
    public static IMusicScore fromFile(Context context,String filename) {
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
                int scale=Integer.valueOf(line[1]);
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

    //获得总音符数
    public int getNoteCount(){
        return note.size();
    }

    //获得某个音符
    public Note getNote(int location){
        return note.get(location);
    }


    //调试用，打印某个乐谱文件
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



    //播放乐谱，after:播放完成后要执行的内容
    @Override
    public void play(final IPlayMusic pm, final Runnable after){
        nowTime=0;
        nowPos=0;
        pm.load(mInstrument, mScale, new Runnable() {
                    @Override
                    public void run() {
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                nowTime += 50;
                                while (nowPos < note.size() && note.get(nowPos).time < nowTime) {
                                    //Log.v("233",""+note.get(nowPos).press);
                                    if (note.get(nowPos).press==1) {
                                        pm.play(note.get(nowPos).note);
                                    }
                                    else if(mInstrument.equals("Piano")) {
                                        pm.stop(note.get(nowPos).note);
                                    }
                                    nowPos++;
                                }
                                if (nowPos >= note.size()) {
                                    this.cancel();
                                    after.run();
                                }

                            }
                        };
                        Timer t = new Timer();
                        t.scheduleAtFixedRate(task, 50, 50);
                    }
                }
        );
    }

    //停止播放乐谱
    @Override
    public void stop(){
        if (task!=null)
            task.cancel();
    }

    //判断是否已经播放完成
    @Override
    public boolean finished(){
        if (nowPos< note.size())
            return false;
        else
            return true;
    }

    //播放下一个音符
    @Override
    public void playnext(final IPlayMusic pm){
        while (!finished() &&  note.get(nowPos).press==0 )
            nowPos++;
        if (finished()) return;
        pm.play(note.get(nowPos).note);
        nowPos++;
    }


    //改变音源
    @Override
    public void changeMusic(final IPlayMusic pm, final Runnable after) {
        pm.load(mInstrument, mScale, new Runnable() {
            @Override
            public void run() {
                after.run();
            }
        });

    }

    //获得所有乐谱文件名
    public static String[] listMsFile(File fileRoot){

        File[] files=fileRoot.listFiles();
        String[] filesname=new String[files.length];
        Vector<String> result=new Vector<String>();
        for (int i=0;i<files.length;++i){
            String name=files[i].getName();
            if (name.length()>4 && name.substring(name.length()-4).equals(".msc")){
                result.addElement(name);
            }
        }

        return (String[])result.toArray(new String[0]);
    }



}
