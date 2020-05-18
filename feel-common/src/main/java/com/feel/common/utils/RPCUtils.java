package com.feel.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @Author: zz
 * @Description:
 * @Date: 8:19 PM 4/15/19
 * @Modified By
 */
public class RPCUtils {

    public static final String DEF_JSONRPC              = "1.0";
    /** 导出私钥*/
    public static final String DUMPPRIVKEY              = "dumpprivkey";



    /** 查询钱包 BTC总余额 */
    public static final String GETBTCBALANCE            = "getbalance";

    /** 查询usdt地址*/
    public static final String OMNI_GETBALANCE          = "omni_getbalance";

    /** 发送全部usdt*/
    public static final String OMNI_FUNDED_SENDALL      = "omni_funded_sendall";

    /** 生成新地址*/
    public static final String GETNEWADDRESS            = "getnewaddress";
    /** 根据账户获取地址*/
    public static final String GETADDRESSBYACCOUNT      = "getaddressesbyaccount";

    /** 发送比特币*/
    public static final String SENDBTC                  = "sendfrom";

    public static final String SENDTOADDRESS            = "sendtoaddress";

    /** 查询历史*/
    public static final String OMNI_LISTTRANSACTIONS    = "omni_listtransactions";
    /** 导入私钥*/
    public static final String IMPORTPRIVKEY            = "importprivkey";
    public static int               DEF_ID              = 0;


    /** 发送usdt*/
    public static final String OMNI_SEND                = "omni_send";

    /** 发送usdt*/
    public static final String OMNI_FUNDED_SEND         = "omni_funded_send";
    private static Logger log = LoggerFactory.getLogger(RPCUtils.class);

    /** 查询账户余额*/
    public static final String LISTUNSPENT              = "listunspent";

    /** 查询账户资产列表*/
    public static final String LISTACCOUNTS             = "listaccounts";

    /** 查询区块信息*/
    public static final String GETINFO                  = "getblockchaininfo";

    /** 旷工率*/
    public static final String SETTXFEE                 = "settxfee";

    public static final String AITC_SEND = "sendtoaddress";

    public static final String AITC_LIST_TRANSACTION = "listtransactions";
    public static final String AITC_PING = "ping";




    /** jsonrpc  */
    private String jsonrpc;
    /** id*/
    private String id;
    /** method*/
    private String method;
    /** params*/
    private Object[] params;



    public RPCUtils() {
    }

    private RPCUtils(Builder builder) {
        setJsonrpc(builder.jsonrpc);
        setId(builder.id);
        setMethod(builder.method);
        setParams(builder.params);
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    /**
     * 生成rpc参数
     * @return
     */
    public HashMap<String, Object> getParam() {
        HashMap<String,Object> param = new HashMap<>();
//        param.put("jsonrpc",(StringUtils.isNull(jsonrpc) ? DEF_JSONRPC : jsonrpc));
        param.put("id",(StringUtils.isNull(id) ? ("" + (DEF_ID++)) : id));
        param.put("method", method);
//        param.put("params", StringUtils.isNull(params) ? "[]" : "[\"" + params + "\"]");
        param.put("params",  params );
        return param;
    }


    /**
     * 链接本地omni服务 RPC接口
     * @param method  方法
     * @param params  参数
     * @param host  方法
     * @param port  参数
     * @return
     */
    public static JSONObject connectRPC(String method , Object[] params, String host , String port) throws IOException {
        return connectRPC(method , params , host , port , null , null);
    }

    public static JSONObject connectRPC(String method , Object[] params , String host , String port , String rpcuser , String rpcpassword) throws IOException {
        RPCUtils rpc = new RPCUtils.Builder().method(method).params(params).build();
        HashMap<String,Object> param = rpc.getParam();
        HashMap<String,Object> header = rpc.getHeader(rpcuser,rpcpassword);
        String url = rpc.getUrl(host , port);
        log.info("connect to "+ url +"  & RPC method :" + method + "& params: " + Arrays.toString(params));
        HttpClientHelper client = new HttpClientHelper();
        return client.doPost(url,param,header);
    }


    /**
     * 生成url
     * @return
     */
    public String getUrl(String host , String port) {
        return "http://" + host + ":" + port;
    }


    /**
     * 生成头
     * @param rpcuser
     * @param rpcpassword
     * @return
     */
    public HashMap<String, Object> getHeader(String rpcuser , String rpcpassword) {

        HashMap<String,Object> header = new HashMap<>();
        header.put("Content-Type","application/json");

        if(rpcuser != null && rpcpassword != null) {
            String authString = rpcuser + ":" + rpcpassword;
            String authEncBytes = CryptoUtils.encryptBase64(authString.getBytes());
            String authStringEnc = new String(authEncBytes);
            header.put("Authorization","Basic " + authStringEnc);
        }
        return header;
    }




    public static final class Builder {
        private String jsonrpc;
        private String id;
        private String method;
        private Object[] params;

        public Builder() {
        }

        public Builder jsonrpc(String val) {
            jsonrpc = val;
            return this;
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder method(String val) {
            method = val;
            return this;
        }

        public Builder params(Object[] val) {
            params = val;
            return this;
        }

        public RPCUtils build() {
            return new RPCUtils(this);
        }
    }
}
