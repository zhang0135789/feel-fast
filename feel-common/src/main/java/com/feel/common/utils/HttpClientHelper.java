package com.feel.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * HTTP POST和GET处理工具类
 */
public class HttpClientHelper {

    private static final MediaType JSON = MediaType.parse("application/json");
    private static Logger log = LoggerFactory.getLogger(HttpClientHelper.class);
    private static Proxy proxy = null;

    private OkHttpClient httpClient = null;


    public HttpClientHelper() {
        this.httpClient = getHttpClient();
    }

    public HttpClientHelper(Proxy proxy) {
        this.proxy = proxy;
        this.httpClient = getHttpClient();
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url    发送请求的URL
     * @param params 请求参数
     * @param header 请求头
     * @return URL 所代表远程资源的响应结果
     */
//    public static String sendGet(String url, HashMap<String, Object> params, HashMap<String, String> header) {
//        String result = "";
//        BufferedReader in = null;
//        try {
//            /**组装参数**/
//            String param = parseParams(params);
//            String urlNameString = url + "?" + param;
//            URL realUrl = new URL(urlNameString);
//            /**打开和URL之间的连接**/
//            URLConnection connection = realUrl.openConnection();
//            /**设置通用的请求属性**/
//            if (header != null) {
//                header.forEach((key, value) -> {
//                    connection.setRequestProperty(key, value);
//                });
//            }
//            connection.setRequestProperty("accept", "*/*");
//            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            /**建立实际的连接**/
//            connection.connect();
//            /**定义 BufferedReader输入流来读取URL的响应**/
//            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//        } catch (Exception e) {
//            log.error("发送GET请求出现异常~!", e);
//        } finally {/**使用finally块来关闭输入流**/
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//        return result;
//    }

    /**
     * 将HashMap参数组装成字符串
     *
     * @param map
     * @return
     */
    private static String parseParams(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        if (map != null && map.size() != 0) {
            for (Entry<String, Object> e : map.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
            sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    private OkHttpClient getHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS);
        return proxy == null ? builder.proxy(proxy).build() : builder.build();
    }


    /**
     * get请求
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public JSONObject doGet(String url, Map<String,Object> params) throws IOException {
        return get(url, params, new TypeReference<JSONObject>() {} , null);
    }

    /**
     * post请求
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public JSONObject doPost(String url, Map<String,Object> params) throws IOException {
        JSONObject s = post(url, params, new TypeReference<JSONObject>() {} ,null);
        return s;
    }

    /**
     * post请求
     * @param url
     * @param params
     * @param headers 请求头
     * @return
     * @throws IOException
     */
    public JSONObject doPost(String url, Map<String,Object> params , Map<String,Object> headers) throws IOException {
        return post(url, params, new TypeReference<JSONObject>() {} ,headers);
    }

    // send a GET request.
    <T> T get(String url, Map<String, Object> params, TypeReference<T> ref , Map<String , Object> headers) throws IOException {
        if (params == null) {
            params = new HashMap<>();
        }
        return call("GET", url, null, params, ref ,headers);
    }

    // send a POST request.
    <T> T post(String url, Object object, TypeReference<T> ref , Map<String,Object> headers) throws IOException {
        return call("POST", url, object, new HashMap<String, Object>(), ref ,headers);
    }

    // call api by endpoint.
    <T> T call(String method, String url, Object object, Map<String, Object> params, TypeReference<T> ref , Map<String,Object> headers) throws IOException {
        Request.Builder builder = new Request.Builder();

        if (headers != null) {
            if (headers.containsKey(HttpHeaders.REFERER)) {
                builder.addHeader(HttpHeaders.REFERER, (String) headers.get(HttpHeaders.REFERER));
            }
            if (headers.containsKey(HttpHeaders.HOST)) {
                builder.addHeader(HttpHeaders.HOST, (String) headers.get(HttpHeaders.HOST));
            }
            if (headers.containsKey(HttpHeaders.USER_AGENT)) {
                builder.addHeader(HttpHeaders.USER_AGENT, (String) headers.get(HttpHeaders.USER_AGENT));
            }
            if (headers.containsKey(HttpHeaders.ACCEPT)) {
                builder.addHeader(HttpHeaders.ACCEPT, (String) headers.get(HttpHeaders.ACCEPT));
            }
            if (headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
                builder.addHeader(HttpHeaders.CONTENT_TYPE , (String) headers.get(HttpHeaders.CONTENT_TYPE));
            }
            if (headers.containsKey("Origin")) {
                builder.addHeader("Origin", (String) headers.get("Origin"));
            }
            if (headers.containsKey("Authorization")) {
                builder.addHeader("Authorization", (String) headers.get("Authorization"));
            }
        }
        if ("POST".equals(method)) {
            String json = JsonUtil.toJson(object);
            RequestBody body = RequestBody.create(JSON, json);
            builder = builder.url(url).post(body);
        } else {
            builder = builder.url(url + "?" + parseParams(params)).get();
        }
        Request request = builder.build();
        Response response = httpClient.newCall(request).execute();
        String s = response.body().string();
        if(StringUtils.isNull(s)) {
            return null;
        }else {
            return JsonUtil.fromJson(s, ref);
        }
    }


}//end
