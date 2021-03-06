package team.dingding.musicgloves.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.music.imp.MusicControl;
import team.dingding.musicgloves.music.imp.MusicScore;
import team.dingding.musicgloves.music.intf.IMusicScore;
import team.dingding.musicgloves.network.imp.ClientManager;
import team.dingding.musicgloves.network.intf.IServerCallBack;
import team.dingding.musicgloves.protocol.imp.WifiProtocolController;
import team.dingding.musicgloves.protocol.intf.IProtocolCallBack;
import team.dingding.musicgloves.protocol.intf.IProtocolController;

import static android.os.SystemClock.sleep;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private MusicControl sound;
    private IMusicScore mMS;
    public MusicScoreState msState=MusicScoreState.Idle;

    boolean[] isMove=new boolean[10];//记录小球的运动状态


    public SharedPreferences getSp() {
        return sp;
    }


    SharedPreferences sp;//数据持久化
    public final String SourceKey = "Source_Key";
    public final String ScaleKey = "Scale_Key";

    int source=1;
    int scale=1;
    int nowfragment=-1;
    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public IMusicScore getMusicScore(){return mMS;}

    public void setMusicScore(IMusicScore value){mMS=value;}
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private IProtocolController mPC;
    private ClientManager mCM;
    private FuncFragment mFF;
    private MusicscoreFragment mMF;
    private SettingFragment mSF;
    private StateFragment mStF;

    public boolean supportMode=false;

    private final Handler msgHandler = new Handler(){
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), msg.getData().getString("prompt"), Toast.LENGTH_SHORT).show();
        }
    };

    private final Handler updateHandler= new Handler(){
        public void handleMessage(Message msg) {
            if (mFF!=null && nowfragment==0)
                mFF.updateText();
            if (mStF!=null && nowfragment==1)
                mStF.updateText();
            if (mMF!=null && nowfragment==2)
                mMF.updateText();
            if (mSF!=null && nowfragment==3)
                mSF.updateText();

        }
    };

    public void childProcessToast(String prompt){
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

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        for(int i=0;i<10;i++){
            //初始小球的运动状态
            isMove[i]=false;
        }

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mPC=new WifiProtocolController(this.getBaseContext());
        mPC.registerNetworkEvent("Connected", new IServerCallBack() {
            @Override
            public void execute(long cid) {
                connectSucceed(cid);
            }
        });
        mPC.registerNetworkEvent("Disconnected", new IServerCallBack() {
            @Override
            public void execute(long cid) {
                disconnect(cid);
            }
        });
        mPC.registerMusicEvent("playMusic",new IProtocolCallBack() {
            @Override
            public void execute(Long cid, String argument) {
                if (msState!=MusicScoreState.Support) {
                    int res = Integer.valueOf(argument);
                    for(int i=0;i<10;i++)
                        isMove[i]=false;
                    isMove[res-1]=true;
                    if (sound.getInstrument().equals("Piano2"))
                    {
                        if (res==10)
                            sound.setScale(0);
                        else if (res==9)
                            sound.setScale(1);
                        else if (res==8)
                            sound.setScale(2);
                        else
                            sound.play(res,sound.getScale());
                    }
                    else {
                        sound.play(res);
                    }
                    if (mMS != null) {
                        mMS.append(res, 1);
                    }
                }else if(mMS!=null){
                    mMS.playnext(sound);
                    if (mMS.finished()){
                       childProcessToast("弹奏结束，切换回普通模式");
                       updateHandler.sendMessage(new Message());
                       msState=MusicScoreState.Idle;
                       mMS=null;
                    }

                }
            }
        });
        mPC.registerMusicEvent("stopMusic",new IProtocolCallBack() {
            @Override
            public void execute(Long cid, String argument) {
                int res=Integer.valueOf(argument);
                if(getSource()==1){
                    sound.stopAll();
                }
                if (mMS!=null){
                    mMS.append(res,0);
                }



            }
        });
        sound=new MusicControl(this);
        mPC.registerMusicEvent("switchInstrument",new IProtocolCallBack() {
            @Override
            public void execute(Long cid, String argument) {
                int res=Integer.valueOf(argument);
                switch (res){
                    case 1:
                        setSource(0);
                        setScale(0);
                        sound.load("Magic",0);
                        childProcessToast("弹奏乐器切换至哲学魔幻电子音");
                        updateHandler.sendMessage(new Message());
                        break;
                    case 2:
                        setSource(1);
                        setScale(0);
                        sound.load("Piano", 0);
                        childProcessToast("弹奏乐器切换至钢琴");
                        updateHandler.sendMessage(new Message());

                        break;
                    case 3:
                        setSource(1);
                        setScale(1);
                        sound.load("Piano", 1);
                        childProcessToast("弹奏乐器切换至钢琴");
                        updateHandler.sendMessage(new Message());
                        break;
                    case 4:
                        setSource(1);
                        setScale(2);
                        sound.load("Piano",2);
                        childProcessToast("弹奏乐器切换至钢琴");
                        updateHandler.sendMessage(new Message());
                        break;
                    case 5:
                        setSource(2);
                        setScale(0);
                        sound.load("Drum",0);
                        childProcessToast("弹奏乐器切换至架子鼓");
                        updateHandler.sendMessage(new Message());
                        break;
                    case 6:
                        setSource(3);
                        setScale(0);
                        sound.load("Guitar",0);
                        childProcessToast("弹奏乐器切换至吉他");
                        updateHandler.sendMessage(new Message());

                        break;
                    case 7:
                        setSource(3);
                        setScale(1);
                        sound.load("Guitar", 1);
                        childProcessToast("弹奏乐器切换至吉他");
                        updateHandler.sendMessage(new Message());
                        break;
                    case 8:
                        setSource(3);
                        setScale(2);
                        sound.load("Guitar", 2);
                        childProcessToast("弹奏乐器切换至吉他");
                        updateHandler.sendMessage(new Message());
                        break;

                }

            }
        });
       // sound=new MusicControlImpl(this);
        mCM=new ClientManager();

        sp =getSharedPreferences("setting",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
        source = sp.getInt(SourceKey,-1);

        if (source == -1) {
            source=0;
        }
        scale = sp.getInt(ScaleKey, -1);
        if (scale == -1) {
            scale=0;
        }
        //加载音乐
        int source=getSource();
            String name=null;
            switch (source){
                case 0:
                    name="Magic";
                    break;
                case 1:
                    name="Piano";
                    break;
                case 2:
                    name="Piano2";
                    break;
                case 3:
                    name="Drum";
                    break;
                case 4:
                    name="Guitar";
                    break;
            }
            sound.load(name,getScale());


//        MusicScore.printAll(this,"q.msc");

    }
    @Override
    protected void onDestroy() {
        //获得SharedPreferences 的Editor对象
        SharedPreferences.Editor editor = sp.edit();
        //修改数据
        editor.putInt(SourceKey, source);
        editor.putInt(ScaleKey, scale);
        editor.commit();
        super.onDestroy();
    }
    @Override
    protected void onStop() {
        //获得SharedPreferences 的Editor对象
        SharedPreferences.Editor editor = sp.edit();
        //修改数据
        editor.putInt(SourceKey, source);
        editor.putInt(ScaleKey, scale);
        editor.commit();
//        mPC.stopServer();
//        mPC.stopWifi();
        super.onStop();
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        nowfragment=position;
        // update the activity_baiducloud content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch (position){
            case 0:
                mFF=(FuncFragment) FuncFragment.newInstance(position + 1);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, mFF)
                        .commit();
                break;
            case 1:
                mStF=(StateFragment)StateFragment.newInstance(position+1);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, mStF)
                        .commit();
                break;
            case 2:
                mMF=(MusicscoreFragment) MusicscoreFragment.newInstance(position + 1);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, mMF)
                        .commit();
                break;
            case 3:
                mSF=(SettingFragment)SettingFragment.newInstance(position + 1);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, mSF)
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle=getString(R.string.title_section3);
                break;
            case 4:
                mTitle=getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.my, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    public IProtocolController getProtocolController(){
        return mPC;
    }

    public MusicControl getMusicControl(){
        return sound;
    }

    public ClientManager getClientManager(){
        return mCM;
    }

    public boolean isMoveOfWhich(int index){return isMove[index];}
    public void setMoveOfWhich(int index){isMove[index]=false;}

    private void connectSucceed(long cid){

        mCM.addClient(cid);
        this.childProcessToast(cid + "连接成功");
        Log.v("233", cid + "连接成功");
        updateHandler.sendMessage(new Message());

    }
    private void disconnect(long cid){
        mCM.removeClient(cid);
        this.childProcessToast(cid + "连接断开");
        Log.v("233", cid + "连接断开");
        updateHandler.sendMessage(new Message());
    }












    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        protected static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        public MainActivity getMainActivity(){
            return (MainActivity) getActivity();
        }
    }

    public enum MusicScoreState{
        Play,
        Make,
        Support,
        Idle,
    }


}
