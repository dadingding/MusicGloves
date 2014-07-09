package team.dingding.musicgloves.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.network.imp.ClientManager;

/**
 * Created by Elega on 2014/7/8.
 */
public class StateFragment extends MainActivity.PlaceholderFragment {

    private ClientManager mCM;
    private TextView[] textState;
    private TextView[] textID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_state, container, false);


        textID=new TextView[2];
        textState=new TextView[2];
        textID[0]=(TextView)rootView.findViewById(R.id.textStateID0);
        textID[1]=(TextView)rootView.findViewById(R.id.textStateID1);
        textState[0]=(TextView)rootView.findViewById(R.id.textStateCntState0);
        textState[1]=(TextView)rootView.findViewById(R.id.textStateCntState1);

        mCM=this.getMainActivity().getClientManager();

        updateText();

        return rootView;
    }

    public void updateText(){
        Log.v("233", "23333");
        for (int i=0;i<2;++i){
            if (mCM.isConnected(i)) {
                textID[i].setText(""+ mCM.getCid(i));
                textState[i].setText("已连接");
            }
            else {
                textID[i].setText("无");
                textState[i].setText("未连接");
            }
        }
    }

    public static MainActivity.PlaceholderFragment newInstance(int sectionNumber) {
        StateFragment fragment = new StateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


}
