package com.feel.modules.app.entity.message;

import com.alibaba.fastjson.JSON;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 2:38 2019/1/17 0017
 * @Modified By
 */
public class WebSocketMessage extends BaseMessage{

    //用户id
    private String userid;
    //请求方法
    private String subject;
    //请求参数
    private String body;
    //返回参数
    private Object value;


    public WebSocketMessage() {
    }

    private WebSocketMessage(Builder builder) {
        setVers(builder.vers);
        setType(builder.type);
        setCtime(builder.ctime);
        setUserid(builder.userid);
        setSubject(builder.subject);
        setBody(builder.body);
        setValue(builder.value);
    }


    public String backUpMessage() {
        String data;
        try {
            Object obj =  ((Object[])value)[0];
            data = obj.toString();
        } catch (Exception e) {
            data = "{\"result\" : " + JSON.toJSONString(value) + "}" ;
        }


        String backMessage =
                "{\n" +
                "    \"comm\": [\n" +
                "        "+ type +",\n" +
                "        {\n" +
                "            \"subject\": "+ subject +",\n" +
                "            \"body\": {\n" +
                "             " + data +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"userid\": "+ userid +",\n" +
                "    \"ctime\": "+ ctime +",\n" +
                "    \"vers\": " + vers + "\n" +
                "}";


        return backMessage;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public static final class Builder {
        private String vers;
        private String type;
        private String ctime;
        private String userid;
        private String subject;
        private String body;
        private Object value;

        public Builder() {
        }

        public Builder vers(String val) {
            vers = val;
            return this;
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder ctime(String val) {
            ctime = val;
            return this;
        }

        public Builder userid(String val) {
            userid = val;
            return this;
        }

        public Builder subject(String val) {
            subject = val;
            return this;
        }

        public Builder body(String val) {
            body = val;
            return this;
        }

        public Builder value(Object val) {
            value = val;
            return this;
        }

        public WebSocketMessage build() {
            return new WebSocketMessage(this);
        }
    }
}
