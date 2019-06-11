package com.feel.modules.app.socket.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feel.common.cache.JedisAPI;
import com.feel.common.utils.StringUtils;
import com.feel.config.CommonConfig;
import com.feel.config.CommonDataDefine;
import com.feel.config.JedisNameConstants;
import com.feel.modules.app.entity.message.WebSocketMessage;
import com.feel.modules.app.socket.socketio.SocketIoClient;
import io.socket.client.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;


/**
 * webSocket 服务端
 */
@ServerEndpoint(value = "/websocket")
@Component
public class WebSocketServer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    private WebSocketClient client;

    @OnOpen
    public void onOpen(Session session) {
        log.info("Websocket Open:" + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        log.info("Websocket Close:" + session.getAsyncRemote().toString());
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        log.info("from websocket客户端的信息：" + msg);
        String rtype   = null;
        String ruserid = null;
        String rctime  = null;
        String rvers   = null;

        JSONObject data = null;
        String subject = null;
        Object body = null;
        Map mapType = null;
        try {
            mapType = JSON.parseObject(msg, Map.class);
            for (Object key : mapType.keySet()) {
                if (key.toString().equals("comm")) {
                    rtype = ((JSONArray) mapType.get(key)).getString(0);
                    data = ((JSONArray) mapType.get(key)).getJSONObject(1);
                    subject = data.getString("subject");
                    body = data.get("body");
                    continue;
                }
                if (key.toString().equals("userid")) {
                    ruserid = (String) mapType.get(key);
                    continue;
                }
                if (key.toString().equals("ctime")) {
                    rctime = String.valueOf(mapType.get(key));
                    continue;
                }
                if (key.toString().equals("vers")) {
                    rvers = String.valueOf(mapType.get(key));
                    continue;
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return;
        }

        if (StringUtils.isNull(rtype)) {
            log.info("type==null");
            return;
        }
        if (StringUtils.isNull(rvers)) {
            log.info("vers==null");
            return;
        }
        //校验版本号
        if (!CommonConfig.getVers().equals(rvers)) {
            log.info("you version ["+ rvers +"] , service version is [" + CommonConfig.getVers() +"]");
            return;
        }

        //判断是否登录
        try {
            processLogin(ruserid, rtype, subject , rctime, rvers, session);
        } catch (Exception e) {
            session.getAsyncRemote().sendText("");
            log.error("登录失败~!" ,e);
            return;
        }


      /*  if (rtype.equals("logout")) {
            if (ruserid != null) {
                processLogout(ruserid, session);
            } else {
                log.info("userid ==null");
                return;
            }
        } else*/
        if (rtype.equals("ping")) {
            processPing(rctime, rtype, session);
        } else {
            if (ruserid != null) {
                //处理业务
                processWebSocketMessage(ruserid, rtype, subject, body ,rvers, session);
            } else {
                log.info("userid ==null");
                return;
            }


        }
    }





    @OnError
    public void onError(Session session, Throwable error){
        log.error("Websocket Error:" + error.toString());
    }

    /**
     * 处理客户端关闭
     *
     * @param session
     */
    private void processClose(Session session) {
        String userid = null;
        for (Map.Entry<String, Session> entry : CommonDataDefine.wsMgUserMap.entrySet()) {
            Session session_exist = entry.getValue();
            if (session_exist.equals(session)) {
                userid = entry.getKey();
                break;
            }
        }
        if (userid != null) {
            CommonDataDefine.wsMgUserMap.remove(userid);
        }
        JedisAPI.delete(JedisNameConstants.VAL_BIZPUSH_MGUSER_NODE + ":" + userid);
        log.info("UserID: " + userid + " WebSocket Session Closeed id : " + session.getId());
    }

    /**
     * ping 功能
     * <p>
     * {"comm":"ping","ctime":"34343"}
     */
    private void processPing(String ctime, String comm, Session session) {
        log.info("Ping : Session.id :   " + session.getId());
        String backMessage = "{\"comm\":\"" + comm + "\",\"ctime\":\"" + ctime + "\",\"value\":\"ok\"}";
        session.getAsyncRemote().sendText(backMessage);
    }


    /**
     * /处理websocke 业务
     * @param ruserid
     * @param type
     * @param subject
     * @param body
     * @param session
     */
    private void processWebSocketMessage(String ruserid, String type, String subject, Object body, String vers ,Session session) {

        switch(type) {
            case "explorer"      : //请求浏览器
                handleExplorer(ruserid ,type,subject ,body ,vers ,session);
                break;
            case "justsaying"   :

                break;
            case "request"      :

                break;
            case "response"     :

                break;
            default :
                break;
        }



    }

    /**
     *  处理浏览器 链接信息
     * @param ruserid
     * @param session
     * @param data
     */
    private void handleExplorer(String ruserid,String type, String subject ,Object data ,String vers , Session session) {
        Socket socket = SocketIoClient.getConnect();
        socket.emit(subject,data);
    }


    /**
     * 登录功能
     * <p>
     * {"comm":"login","userid":"1111-2222-3333-4444","token":"3434","userno":"200000","ctime":"34343"}
     */
    private void processLogin(String userid, String type, String subject, String ctime, String vers, Session session) throws Exception{
        if (StringUtils.isNull(userid)) {
            log.info("userid==null");
            return;
        }
        //创建与区块链间的通讯
        // 先删除
        CommonDataDefine.wsMgUserMap.remove(userid);

        // bizpush:val:mguser:node
//            JedisAPI.delete(JedisNameConstants.VAL_BIZPUSH_MGUSER_NODE + ":" + userid);

        // 后绑定
        CommonDataDefine.wsMgUserMap.put(userid, session);


        // bizpush:val:mguser:node:"userid"
//            JedisAPI.set(JedisNameConstants.VAL_BIZPUSH_MGUSER_NODE + ":" + userid, CommonConfig.NODENO);

        String backMessage = new WebSocketMessage.Builder()
                .userid(userid)
                .ctime(ctime)
                .subject("login")
                .type("login")
                .value("success")
                .vers(vers)
                .build()
                .backUpMessage();
        session.getAsyncRemote().sendText(backMessage);
        log.info("Login Process End: Userid : " + userid);

    }

    /**
     * 登出功能
     * <p>
     * {"comm":"logout","userid":"b8f4f107323a465a82d3dd76e0fc3705"}
     */
    private void processLogout(String userid, Session session) {
        if (StringUtils.isNull(userid)) {
            log.info("userid==null");
            return;
        }
        CommonDataDefine.wsMgUserMap.remove(userid);
        // bz:val:mguser:node + 用户id
        JedisAPI.delete(JedisNameConstants.VAL_BIZPUSH_MGUSER_NODE + ":" + userid);
        log.info("Loginout End Process End: Userid : " + userid);
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
