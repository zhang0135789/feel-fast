package com.feel.common.utils;

/**
 * @Author: zz
 * @Description: 脚本工具工具
 * @Date: 下午 1:39 2018/11/26 0026
 * @Modified By
 */
public class ShellScriptUtil {





    /**
     * forever启动/关闭 nodejs服务
     * @param method
     * @param logname
     * @param servicename
     * @return forever start -l hub.log -a  ~/edu/edu-hub/start.js
     */
    public static String forever(String method ,String logname,String servicename) {
        if("start".equals(method))
            return "forever "+method+" -l "+logname+".log"+" -a " + servicename;
        else if("stop".equals(method))
            return "forever "+method+" " +servicename;
        return null;
    }

    /**
     * cp
     * @param filename
     * @param filepath
     * @param topath
     * @return
     */
    public static String cp(String filename ,String filepath ,String topath) {
        return "cp " + filepath + filename + " " + topath + filename;
    }

    /**
     * mkreomotedir
     * @param path
     * @return
     */
    public static String mkdir(String path) {
        return "mkdir -p " + path;
    }

    /**
     * scp 免密登录方式
     * @param filename
     * @param filepath
     * @param remotepath
     * @param user
     * @param remoteip
     * @return
     */
    public static String scp(String filename,String filepath ,String remotepath ,String user ,String remoteip) {
        return "scp -r " + filepath + filename + " "+ user +"@"+ remoteip + ":" + remotepath;
    }


    /**
     * mkreomotedir 免密登录方式
     * @param remotepath
     * @param user
     * @param ip
     * @return
     */
    public static String mkreomotedir(String remotepath , String user ,String ip) {
        return "ssh "+user+"@"+ip+" mkdir -p "+remotepath;
    }


    /**
     * rm -rf
     * @param deletepath
     * @return
     */
    public static String delete(String deletepath) {

        return "rm -rf "+deletepath;
    }

    /**
     *
     * @param filepath
     * @return
     */
    public static String cd(String filepath) {
        return "cd "+filepath;
    }

    /**
     *
     * @param serviceName
     * @return
     */
    public static String sh(String serviceName) {
        return "sh " + serviceName;
    }
}
