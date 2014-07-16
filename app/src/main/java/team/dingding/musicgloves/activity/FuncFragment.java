package team.dingding.musicgloves.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.music.impl.MusicControlImpl;
import team.dingding.musicgloves.music.intf.IPlayMusic;
import team.dingding.musicgloves.network.imp.ClientManager;
import team.dingding.musicgloves.network.intf.IServerCallBack;
import team.dingding.musicgloves.protocol.intf.IProtocolController;
import team.dingding.musicgloves.protocol.intf.IStartWifiCallBack;

/**
 * Created by Elega on 2014/7/8.
 */
public class FuncFragment extends MainActivity.PlaceholderFragment {

    private IProtocolController mPC;
    private IPlayMusic sound;
    private ClientManager mCM;
    private TextView[] textState;
    private ImageView[] ivDevice;



//    private ClientManager mCM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_func, container, false);


        ((ImageView)rootView.findViewById(R.id.btnFuncLoadMusic)).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){btnFuncLoadMusicOnClick(v);}
        });


        ((ImageView)rootView.findViewById(R.id.btnFuncSetWifi)).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){btnFuncSetWifiOnClick(v);}
        });

        ((ImageView)rootView.findViewById(R.id.btnFuncQuit)).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){btnFuncQuitOnClick(v);}
        });


        textState=new TextView[2];
        ivDevice=new ImageView[2];
        textState[0]=(TextView)rootView.findViewById(R.id.textStateCntState0);
        textState[1]=(TextView)rootView.findViewById(R.id.textStateCntState1);
        ivDevice[0]=(ImageView)rootView.findViewById(R.id.ivStateDevice0);
        ivDevice[1]=(ImageView)rootView.findViewById(R.id.ivStateDevice1);

        mCM=this.getMainActivity().getClientManager();

        updateText();


        return rootView;
    }


    public void updateText(){
        Log.v("233", "23333");
        for (int i=0;i<2;++i){
            if (mCM.isConnected(i)) {
                textState[i].setText("已连接");
                ivDevice[i].setImageDrawable(getResources().getDrawable(R.drawable.pic_glove_cnt) );
            }
            else {
                textState[i].setText("未连接");
                ivDevice[i].setImageDrawable(getResources().getDrawable(R.drawable.pic_glove_dis) );

            }
        }
    }

    public static MainActivity.PlaceholderFragment newInstance(int sectionNumber) {
        FuncFragment fragment = new FuncFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }



    public void btnFuncLoadMusicOnClick(View v){
        sound=getMainActivity().getMusicControl();
        if (sound!=null) {
            if (sound.load(1)) {
                Toast.makeText(this.getActivity().getApplicationContext(),
                        "音乐载入成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getActivity().getApplicationContext(),
                        "音乐载入失败", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void btnFuncSetWifiOnClick(View v){
        final ProgressDialog dialog=ProgressDialog.show(v.getContext(),
                "正在开启Wifi热点并架设服务器","请稍后...",true);
        dialog.show();
        mPC=((MainActivity)this.getActivity()).getProtocolController();
        mPC.startApaAndServer("Billy","12345678",5000,8081,new IStartWifiCallBack() {
            @Override
            public void execute() {
                dialog.dismiss();
            }
        },new IStartWifiCallBack() {
            @Override
            public void execute() {
                dialog.dismiss();
            }
        });
    }


    public void btnFuncQuitOnClick(View v){
        SharedPreferences.Editor editor = getMainActivity().getSp().edit();
        //修改数据
        editor.putInt("Source_Key", getMainActivity().getSource());
        editor.putInt("Scale_Key", getMainActivity().getScale());
        editor.commit();
        System.exit(0);
    }
}
