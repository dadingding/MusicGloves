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
import android.widget.Toast;

import java.util.HashMap;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.network.intf.IServerCallBack;
import team.dingding.musicgloves.protocol.imp.WifiProtocolController;
import team.dingding.musicgloves.protocol.intf.IProtocolCallBack;

public class MusicActivity extends Activity {

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

    SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM,10);
    int loadmark=0;
    int currplay=0;
    HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        LoadMusic(1);
    }
    public void playMusic(View v){
        soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
        currplay=soundPool.play(soundMap.get(2), 1, 1, 0, 0, 1);
//        soundPool.play(soundMap.get(3), 1, 1, 0, 0, 1);
    }

    public void onStop(){
        super.onStop();
        if(currplay!=0)
            soundPool.stop(currplay);

    }
    public void PauseMusic(View v){

    }

    public void StopMusic(View v){

    }

    public void  LoadMusic(int instrument){
        final ProgressDialog dialog=ProgressDialog.show(this,
                "loading music","wait...",true);
        soundMap.put(1, soundPool.load(this, R.raw.life, 1));
        soundMap.put(2, soundPool.load(this, R.raw.sky, 1));
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
                loadmark = loadmark + 1;
                if (loadmark == 2) {
                    dialog.dismiss();
                    Toast.makeText(getBaseContext(), String.valueOf(loadmark)+"Load success", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void BuildWifi(View v){
        WifiProtocolController pc=new WifiProtocolController(this.getApplicationContext());
        pc.registerMusicEvent("do",new IProtocolCallBack() {
            @Override
            public void execute(Long cid, String argument) {
                 soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
                childProcessToast(cid + " 事件"  + "do" +" 参数" + argument);
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
