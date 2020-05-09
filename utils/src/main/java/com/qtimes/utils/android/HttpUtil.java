package com.qtimes.utils.android;

/**
 * Author: JackHou
 * Date: 2018/3/21.
 */

public class HttpUtil {
    public interface HTTP {
        //一号服务器
        String API_HOST = "https://api.qtimes.cc";//一号
        //        String WONLY_HOST = "https://update.qtimes.cc";//三号
//        String WONLY_HOST = "http://101.37.252.18";//
//        String WONLY_HOST = "http://192.168.3.4";
        int API_PORT = 8080;

        String UPLOAD_HOST = "http://101.37.252.18";//3号
        int UPLOAD_PORT = 8091;

        String UPLOAD_BIG_FILE_PATH = "/group1/big/upload/";
    }

    public interface HTTPS {
        String HOST_REGEX = "*.qtimes.cc";//域名校验
    }

    public static String urlJoin(String host, int port, String path) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(host);
        buffer.append(":");
        buffer.append(port);
        buffer.append(path);
        String url = buffer.toString();
        return url;
    }

    public static String urlJoin(String host, int port) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(host);
        buffer.append(":");
        buffer.append(port);
        String url = buffer.toString();
        return url;
    }
}
