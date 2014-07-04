package team.dingding.musicgloves.protocol.imp;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import team.dingding.musicgloves.network.imp.AdHoc;
import team.dingding.musicgloves.network.imp.WifiApAdmin;
import team.dingding.musicgloves.network.intf.INetworkTransmission;
import team.dingding.musicgloves.network.intf.IServerCallBack;
import team.dingding.musicgloves.network.intf.IWifiAp;
import team.dingding.musicgloves.protocol.intf.IProtocolCallBack;

/**
 * Created by Elega on 2014/7/3.
 */
public class ProtocolController {
    Map<String,IProtocolCallBack> mEventMap=new HashMap<String,IProtocolCallBack>();
    Map<String,String> mInstrMap=new HashMap<String,String>();
    Map<Long,String> mBufMap=new HashMap<Long, String>();
    IWifiAp wifiAp;
    INetworkTransmission adhoc;
    int mMessageLength=10;
    int mSerialNumberDigits=2;
    int mInstructionDigits=4;
    int mArgumentDigits=4;

    public ProtocolController(Context context){
        wifiAp=new WifiApAdmin(context);
        adhoc=new AdHoc();
        adhoc.registerEvent("GetMessage",new IServerCallBack() {
                    @Override
                    public void execute(long cid) {
                        getMessage(cid);
                    }
                }
                );
        mInstrMap.put("aaaa","aaaa");
        mInstrMap.put("bbbb","bbbb");
        mInstrMap.put("cccc","cccc");
    }

    public boolean startApaAndServer(String ssid,String password,int latency,int port)
    {
        if (wifiAp.startWifiAp(ssid,password,latency)){
            adhoc.startServer(port);
        }
        else
            return false;
        return false;
    }


    public boolean registerMusicEvent(String eventName, IProtocolCallBack cb){
        mEventMap.put(eventName,cb);
        return true;
    }

     public boolean registerNetworkEvent(String eventName,IServerCallBack cb){
         return adhoc.registerEvent(eventName,cb);
     }

    private  void getMessage(Long cid){
        String s=adhoc.getMessage(cid);
        Log.v("aaaa", s);
        if (mBufMap.containsKey(cid)){
            mBufMap.put(cid,mBufMap.get(cid)+s);
        }
        else{
            mBufMap.put(cid,s);
        }
        handleMessage(cid);
    }

    private void handleMessage(Long cid){
        String buf=mBufMap.get(cid);
        while (buf!=null && buf.length()>=mMessageLength){
            String tmp=buf.substring(0,mMessageLength);
            buf=buf.substring(mMessageLength);
            mBufMap.put(cid,buf);
            Message message=parseMessage(cid,tmp);
            postMessage(cid,message);
        }
    }

    private Message parseMessage(Long cid,String message){
        Log.v("aaaa",""+message.length());
        if (message.length()==mMessageLength){
            String tmp=message.substring(0,mSerialNumberDigits);
            int sn=Integer.valueOf(tmp);
            message=message.substring(mSerialNumberDigits);
            String ins=message.substring(0,mInstructionDigits );
            message=message.substring(mInstructionDigits);
            String arg=message.substring(0,mArgumentDigits);

            return new Message(sn,ins,arg);
        }
        else
            return null;
    }

    private void postMessage(Long cid,Message message){
        Log.v("233","sn "+message.serialNumber+"inst "+message.instruction+"arg "+message.argument);
        String opr= mInstrMap.get(message.instruction);
        IProtocolCallBack cb=mEventMap.get(opr);
        if (cb!=null){
//            Looper.prepare();
            cb.execute(cid,message.argument);
//            Looper.loop();
        }
    }


    private class Message {
        public Message(int _serialNumber, String _instruction, String _argument) {
            serialNumber = _serialNumber;
            instruction = _instruction;
            argument = _argument;
        }

        public int serialNumber;
        public String instruction;
        public String argument;
    }

}
