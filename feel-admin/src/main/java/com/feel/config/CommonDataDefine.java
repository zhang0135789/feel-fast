package com.feel.config;

import io.socket.client.Socket;
import org.java_websocket.client.WebSocketClient;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommonDataDefine {

    /**
     * 在wsServer 上保存ws的链接信息
     */
    public static Map<String, Session> wsMgUserMap = new ConcurrentHashMap<>();


    /**
     * client链接区块链  长连接
     */
    public static Map<String, WebSocketClient> websocketClientMap = new ConcurrentHashMap<>();
    /**
     * client链接区块链浏览器  长连接
     */
    public static Map<String, Socket> socketIOClientMap = new ConcurrentHashMap<>();
}
