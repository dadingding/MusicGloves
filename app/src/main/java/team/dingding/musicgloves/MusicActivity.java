package team.dingding.musicgloves;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.music.impl.MusicControlImpl;
import team.dingding.musicgloves.music.intf.IPlayMusic;
import team.dingding.musicgloves.network.intf.IServerCallBack;
import team.dingding.musicgloves.protocol.imp.WifiProtocolController;
import team.dingding.musicgloves.protocol.intf.IProtocolCallBack;

public class MusicActivity extends Activity {

    private final Handler msgHandler = new Handler(){
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(),msg.getData().getString("prompt") , Toast.LENGTH_SHORT).show();
        }
    };
    MusicControlImpl sound;
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
        setContentView(R.layout.activity_music);
        sound=new MusicControlImpl(this);
        sound.load();
    }

    public void onStop(){
        super.onStop();
        sound.stopAll();

    }
    public void PauseMusic(View v){

    }

    public void StopMusic(View v){

    }


    public void BuildWifi(View v){
        Random a=new Random();
        Button b=(Button) v;
        sound.play(Integer.valueOf(b.getText().toString()));
        /*
        WifiProtocolController pc=new WifiProtocolController(this.getApplicationContext());
        pc.registerMusicEvent("doo",new IProtocolCallBack() {
            @Override
            public void execute(Long cid, String argument) {
                 soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
                childProcessToast(cid + " 事件"  + "doo" +" 参数" + argument);
            }
        });

        pc.registerMusicEvent("re",new IProtocolCallBack() {
            @Override
            public void execute(Long cid, String argument) {
                soundPool.play(soundMap.get(2), 1, 1, 0, 0, 1);
                childProcessToast(cid + " 事件"  + "re" +" 参数" + argument);


            }
        });


        pc.registerMusicEvent("mi",new IProtocolCallBack() {
            @Override
            public void execute(Long cid, String argument) {
                soundPool.play(soundMap.get(3), 1, 1, 0, 0, 1);
                childProcessToast(cid + " 事件"  + "mi" +" 参数" + argument);
            }
        });


        pc.registerNetworkEvent("Connected", new IServerCallBack() {
            @Override
            public void execute(long cid) {
                childProcessToast(cid + "连接成功");
            }
        });

        pc.registerNetworkEvent("Disconnected", new IServerCallBack() {
            @Override
            public void execute(long cid) {
                childProcessToast(cid + "连接断开");
            }
        });


        pc.startApaAndServer("Billy", "12345678", 5000, 8081);
        */
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.music, menu);
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
