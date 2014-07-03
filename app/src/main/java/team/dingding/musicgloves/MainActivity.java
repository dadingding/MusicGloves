package team.dingding.musicgloves;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    private SoundPool soundPool;

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
