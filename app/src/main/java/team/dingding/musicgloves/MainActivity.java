package team.dingding.musicgloves;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import team.dingding.musicgloves.network.intf.IServerCallBack;
import team.dingding.musicgloves.protocol.imp.WifiProtocolController;
import team.dingding.musicgloves.protocol.intf.IProtocolCallBack;

public class MainActivity extends Activity {

//    private final Handler msgHandler = new Handler(){
//        public void handleMessage(Message msg) {
//        Toast.makeText(getApplicationContext(),msg.getData().getString("prompt") , Toast.LENGTH_SHORT).show();
//            }
//    };
//
//
//    private void childProcessToast(String prompt){
//        Message msg = msgHandler.obtainMessage();
//        Bundle bd=new Bundle();
//        bd.putString("prompt",prompt);
//        msg.setData(bd);
//        msgHandler.sendMessage(msg);
//    }

    SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void BeginPlay(View v){

        startActivity(new Intent("team.dingding.musicgloves.MusicActivity"));
    }
//    public void ceshi(View v){
//        WifiProtocolController pc=new WifiProtocolController(this.getApplicationContext());
//        pc.registerMusicEvent("playmusic",new IProtocolCallBack() {
//            @Override
//            public void execute(Long cid, String argument) {
////                soundPool.play(1,1,1, 0, 0, 1);
//                childProcessToast(cid + " 事件"  + "playmusic" +" 参数" + argument);
//            }
//        });
//
//        pc.registerMusicEvent("bbbb",new IProtocolCallBack() {
//            @Override
//            public void execute(Long cid, String argument) {
//                childProcessToast(cid + " 事件"  + "bbbb" +" 参数" + argument);
//
//
//            }
//        });
//
//
//        pc.registerMusicEvent("cccc",new IProtocolCallBack() {
//            @Override
//            public void execute(Long cid, String argument) {
//                childProcessToast(cid + " 事件"  + "cccc" +" 参数" + argument);
//            }
//        });
//
//
//        pc.registerNetworkEvent("Connected", new IServerCallBack() {
//            @Override
//            public void execute(long cid) {
//                childProcessToast(cid + "连接成功");
//            }
//        });
//
//        pc.registerNetworkEvent("Disconnected", new IServerCallBack() {
//            @Override
//            public void execute(long cid) {
//                childProcessToast(cid + "连接断开");
//            }
//        });
//
//
//        pc.startApaAndServer("Billy", "12345678", 5000, 8081);
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
