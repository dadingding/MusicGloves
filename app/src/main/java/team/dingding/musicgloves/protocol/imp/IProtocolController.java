package team.dingding.musicgloves.protocol.imp;

import team.dingding.musicgloves.network.intf.IServerCallBack;
import team.dingding.musicgloves.protocol.intf.IProtocolCallBack;

/**
 * Created by Elega on 2014/7/4.
 */
public interface IProtocolController {
    //启动Wifi热点和服务器
    boolean startApaAndServer(String ssid, String password, int latency, int port);

    //注册事件:如播放声音、调节音量
    boolean registerMusicEvent(String eventName, IProtocolCallBack cb);

    //注册网络事件:如连接成功、断开连接
    boolean registerNetworkEvent(String eventName, IServerCallBack cb);
}
