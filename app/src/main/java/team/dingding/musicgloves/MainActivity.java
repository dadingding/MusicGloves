package team.dingding.musicgloves;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import team.dingding.musicgloves.network.intf.IServerCallBack;
import team.dingding.musicgloves.protocol.imp.ProtocolController;
import team.dingding.musicgloves.protocol.intf.IProtocolCallBack;

public class MainActivity extends Activity {

    private SoundPool soundPool;


    private final Handler msgHandler = new Handler(){
        public void handleMessage(Message msg) {
        Toast.makeText(getApplicationContext(),msg.getData().getString("prompt") , Toast.LENGTH_SHORT).show();
            }
    };


    private void childProcessToast(String prompt){
        Message msg = msgHandler.obtainMessage();
        Bundle bd=new Bundle();
        bd.putString("prompt",prompt);
        msg.setData(bd);
        msgHandler.sendMessage(msg);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soundPool = new SoundPool(10,AudioManager.STREAM_MUSIC,10);
        soundPool.load(this,R.raw.time1,1);
    }
    public void PlayMusic(View v){
//        soundPool = new SoundPool(10,AudioManager.STREAM_MUSIC,10);
//        soundPool.load(this,R.raw.gg,1);
//        try {
//            Thread.sleep(100);// 给予初始化音乐文件足够时间
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        soundPool.play(1,1,1, 0, 0, 1);
    }

    public void StopPlay(View v){

    }

    public void ceshi(View v){
        ProtocolController pc=new ProtocolController(this.getApplicationContext());
        pc.registerMusicEvent("aaaa",new IProtocolCallBack() {
            @Override
            public void execute(Long cid, String argument) {
                childProcessToast(argument);
            }
        });

        pc.registerMusicEvent("bbbb",new IProtocolCallBack() {
            @Override
            public void execute(Long cid, String argument) {
                childProcessToast(argument);


            }
        });


        pc.registerMusicEvent("cccc",new IProtocolCallBack() {
            @Override
            public void execute(Long cid, String argument) {
                childProcessToast(argument);
            }
        });


        pc.registerNetworkEvent("Connected", new IServerCallBack() {
            @Override
            public void execute(long cid) {
                Toast.makeText(getApplicationContext(), "连接成功",
                        Toast.LENGTH_SHORT).show();
            }
        });

        pc.registerNetworkEvent("Disconnected", new IServerCallBack() {
            @Override
            public void execute(long cid) {
                Toast.makeText(getApplicationContext(), "连接断开",
                        Toast.LENGTH_SHORT).show();
            }
        });


        pc.startApaAndServer("Billy", "12345678", 5000, 8081);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        int azard2 = 0;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
