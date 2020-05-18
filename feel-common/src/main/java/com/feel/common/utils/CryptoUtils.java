package com.feel.common.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.Base64;

/**
 * @Author: zz
 * @Description:
 * @Date: 1:01 AM 4/16/19
 * @Modified By
 */
public class CryptoUtils {


    /**
     * BASE64Encoder 加密
     * @param data 要加密的数据
     * @return 加密后的字符串
     */
    public static String encryptBase64(byte[] data) {
        Base64.Encoder encoder = Base64.getEncoder();
        String encode = encoder.encodeToString(data).replace("\r\n","");
        return encode;
    }

    /**
     * BASE64Decoder 解密
     * @param data 要解密的字符串
     * @return 解密后的byte[]
     * @throws Exception
     */
    public static byte[] decryptBase64(String data)  {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] buffer = decoder.decode(data.replace("\r\n",""));
//        BASE64Decoder decoder = new BASE64Decoder();
//        byte[] buffer = decoder.decodeBuffer(data);
        return buffer;
    }



    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptBASE64(String key) {
        byte[] bt;
        try {
            bt = (new BASE64Decoder()).decodeBuffer(key);
            return new String(bt);// 如果出现乱码可以改成： String(bt, "utf-8")或 gbk
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String key) {
        byte[] bt = key.getBytes();
        return (new BASE64Encoder()).encodeBuffer(bt);
    }
}
