package com.feel.modules.app.entity.message;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 2:53 2019/1/17 0017
 * @Modified By
 */
public abstract class BaseMessage {
    //消息协议版本
    public String vers;
    //消息类型
    public String type;
    //time
    public String ctime;

    public String getVers() {
        return vers;
    }

    public void setVers(String vers) {
        this.vers = vers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }
}
