package team.dingding.musicgloves.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.oauth.BaiduOAuth;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.music.intf.IPlayMusic;
import team.dingding.musicgloves.network.imp.ClientManager;
import team.dingding.musicgloves.protocol.intf.IProtocolController;
import team.dingding.musicgloves.protocol.intf.IStartWifiCallBack;

/**
 * Created by Elega on 2014/7/8.
 */

//[功能]页面中的内容
public class FuncFragment extends MainActivity.PlaceholderFragment {

    private IProtocolController mPC;
    private IPlayMusic sound;
    private ClientManager mCM;
    private TextView[] textState;
    private ImageView[] ivDevice;
    private String mbOauth = null;



//    private ClientManager mCM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_func, container, false);


        ((ImageView)rootView.findViewById(R.id.btnFuncCloud)).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){btnFuncCloudOnClick(v);}
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



    //进入云空间
    public void btnFuncCloudOnClick(View v){
        BaiduOAuth oauthClient = new BaiduOAuth();
        final Context context=v.getContext();
        oauthClient.startOAuth(context, "L6g70tBRRIXLsY0Z3HwKqlRE", new String[]{"basic", "netdisk"}, new BaiduOAuth.OAuthListener() {
            @Override
            public void onException(String msg) {
                Toast.makeText(context, "Login failed " + msg, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onComplete(BaiduOAuth.BaiduOAuthResponse response) {
                if(null != response){
                    mbOauth = response.getAccessToken();
                    Intent i=new Intent("team.dingding.musicgloves.activity.BaiduCloudActivity");
                    i.putExtra("mbOauth",mbOauth);
                    startActivity(i);
                    Toast.makeText(context, "Token: " + mbOauth + "    User name:" + response.getUserName(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancel() {
                Toast.makeText(context, "Login cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //建立wifi热点
    public void btnFuncSetWifiOnClick(View v){
        final ProgressDialog dialog=ProgressDialog.show(v.getContext(),
                "正在开启Wifi热点并架设服务器","请稍后...",true);
        dialog.show();
        mPC=((MainActivity)this.getActivity()).getProtocolController();
        mPC.startApaAndServer("Bilguoly","12345678",5000,8081,new IStartWifiCallBack() {
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


    //退出程序
    public void btnFuncQuitOnClick(View v){

        SharedPreferences.Editor editor = getMainActivity().getSp().edit();
        //修改数据
        editor.putInt("Source_Key", getMainActivity().getSource());
        editor.putInt("Scale_Key", getMainActivity().getScale());
        editor.commit();
        System.exit(0);




    }
}
