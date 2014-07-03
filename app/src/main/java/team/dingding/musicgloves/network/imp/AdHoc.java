package team.dingding.musicgloves.network.imp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import team.dingding.musicgloves.network.intf.INetworkTransmission;
import team.dingding.musicgloves.network.intf.IServerCallBack;

/**
 * 用于手机与单片机端的点对点网络传输
 */
public class AdHoc implements INetworkTransmission {
    private ServerSocket mServerSocket;
    private String data = "";


    private IServerCallBack eventConnected; //连接成功时的回调函数
    private IServerCallBack eventDisconnected; //连接断开时的回调函数
    private IServerCallBack eventGetMessage; //得到信息时的回调函数



    private Map<Long,Client> clientMap=new HashMap<Long, Client>();




    public ConnectingState connectingState = ConnectingState.unConnected;
    public String sb="";

    public AdHoc(){}

    //建立服务器
    @Override
    public void startServer(int port) {
        try {
            mServerSocket = new ServerSocket(port);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    serverLoop();
                }
            });
            thread.start();

        } catch (IOException e) {
            connectingState = ConnectingState.failed;
            e.printStackTrace();
        }
    }

    //关闭服务器
    @Override
    public void closeServer() {
        try {
            mServerSocket.close();

        } catch (IOException e) {
            connectingState = ConnectingState.failed;
            e.printStackTrace();
        }
    }



    //为客户端发送信息
    @Override
    public void sendMessage(long cid, String message) {
        Client client=clientMap.get(cid);
        try {
            client.writeBufLock.lock();
            client.writeBuf += message;
        }
        finally {
            client.writeBufLock.unlock();
        }
    }

    //得到某客户端的信息
    @Override
    public String getMessage(long cid) {
        String result;
        Client client=clientMap.get(cid);

        try {
            client.readBufLock.lock();
            result = client.readBuf;
            client.readBuf = "";
        }
        finally {
            client.readBufLock.unlock();
        }
        return result;

    }

    //得到某客户端的连接状态
    @Override
    public ConnectingState getConnectingState(long cid){
        Client client=clientMap.get(cid);
        if (client==null)
            return ConnectingState.unConnected;
        else
            return client.connectingState;
    }



    /**
     * 注册事件
     * @param eventName 事件名，例："Connected"、"Disconnected"
     * @param cb 回调函数
     * 成功返回true，失败返回false
     */
    @Override
    public boolean registerEvent(String eventName, IServerCallBack cb){
        Class aphoc=this.getClass();
        try{
            Method m= aphoc.getMethod("register"+eventName,IServerCallBack.class);
            m.invoke(this,cb);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }


    private void serverLoop()
    {
        try {
            while (true) {
                Socket socket = mServerSocket.accept();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tryConnecting();

                    }

                });
                Client client = new Client
                        (socket.getLocalAddress().toString(), socket.getPort(), thread.getId(),socket);
                clientMap.put(thread.getId(), client);
                thread.start();
                if (eventConnected!=null) eventConnected.execute(thread.getId());
            }
        }
        catch (IOException e) {
            connectingState = ConnectingState.failed;
            e.printStackTrace();
        }
    }

   private void tryConnecting(){
       DataInputStream in=null;
       DataOutputStream out=null;
       int count;
       long cid=Thread.currentThread().getId();

       Client client=clientMap.get(cid);
       try {
           Socket socket = client.socket;
           client.connectingState=ConnectingState.connected;
           //Log.v("233 ", String.valueOf(cid) + "Connect succeed");
           in = new DataInputStream(socket.getInputStream());
           out=new DataOutputStream(socket.getOutputStream());
           byte[] buf = new byte[1024];
           while (client.connectingState==ConnectingState.connected) {
               //读取数据
               count=in.available();

               if (count>0) {
                   try{
                   client.readBufLock.lock();

                       int len = in.read(buf);
                       if (len == -1) {
                           connectingState = ConnectingState.unConnected;
                       } else {
                           client.readBuf += new String(buf, 0, len);
                           if (eventGetMessage!=null) eventGetMessage.execute(cid);
                       }
                   }
                   finally {
                       client.readBufLock.unlock();
                   }
               }

               //发送数据
               try{
               client.writeBufLock.lock();
                   if (client.writeBuf != "") {
                       out.writeBytes(client.writeBuf);
                       client.writeBuf = "";
                   }
               }
               finally {
                   client.writeBufLock.unlock();
               }
               try{Thread.sleep(20);}catch (InterruptedException e){}

           }
           in.close();
           out.close();
           socket.close();
       } catch (IOException e) {
           connectingState = ConnectingState.failed;
           if (eventDisconnected!=null) eventDisconnected.execute(cid);

       }
       finally {
           clientMap.remove(cid);
       }
   }

    public void registerConnected(IServerCallBack cb){
        eventConnected=cb;
    }

    public void registerDisconnected(IServerCallBack cb){
        eventDisconnected=cb;
    }


    public void registerGetMessage (IServerCallBack cb){
        eventGetMessage =cb;
    }




    public class Client{
        public Client(String _ipAddr,int _port,long _cid,Socket _socket){
            ipAddr=_ipAddr;
            port=_port;
            cid=_cid;
            socket=_socket;
            connectingState=ConnectingState.unConnected;
            readBufLock=new ReentrantLock(true);
            writeBufLock=new ReentrantLock(true);
            readBuf="";
            writeBuf="";
        }
        public String ipAddr;
        public int port;
        public long cid;
        public Socket socket;
        public String readBuf;
        public String writeBuf;
        public ReentrantLock readBufLock;
        public ReentrantLock writeBufLock;
        public ConnectingState connectingState;

    }
}

