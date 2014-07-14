package team.dingding.musicgloves.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.music.impl.MusicControlImpl;
import team.dingding.musicgloves.music.intf.IPlayMusic;
import team.dingding.musicgloves.network.imp.ClientManager;
import team.dingding.musicgloves.network.intf.IServerCallBack;
import team.dingding.musicgloves.protocol.intf.IProtocolController;

/**
 * Created by Elega on 2014/7/8.
 */
public class FuncFragment extends MainActivity.PlaceholderFragment {

    private IProtocolController mPC;
    private IPlayMusic sound;
//    private ClientManager mCM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_func, container, false);


        ((ImageButton)rootView.findViewById(R.id.btnFuncLoadMusic)).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){btnFuncLoadMusicOnClick(v);}
        });


        ((ImageButton)rootView.findViewById(R.id.btnFuncSetWifi)).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){btnFuncSetWifiOnClick(v);}
        });

        ((ImageButton)rootView.findViewById(R.id.btnFuncQuit)).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){btnFuncQuitOnClick(v);}
        });

        return rootView;
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
        mPC=((MainActivity)this.getActivity()).getProtocolController();
        mPC.startApaAndServer("Billy","12345678",5000,8081);
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
