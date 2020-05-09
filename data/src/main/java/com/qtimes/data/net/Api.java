package com.qtimes.data.net;

import com.qtimes.utils.android.HttpUtil;

/**
 * Created by JackHou on 2016/12/12.
 */

public class Api {
    public interface URL {
        String API = HttpUtil.urlJoin(HttpUtil.HTTP.API_HOST, HttpUtil.HTTP.API_PORT);
        String UPLOAD_BIG_FILE = HttpUtil.urlJoin(HttpUtil.HTTP.UPLOAD_HOST, HttpUtil.HTTP.UPLOAD_PORT, HttpUtil.HTTP.UPLOAD_BIG_FILE_PATH);
        String DEVICE = HttpUtil.urlJoin(HttpUtil.HTTP.API_HOST, HttpUtil.HTTP.API_PORT);
    }

    public interface Response {
        /***** Response codes *****/
        int RESERVED = 0;
        int SUCCESS = 1;
        int NO_PARAMETER = 2;
        int INVALID_PARAMETER = 3;
        int HAS_EXISTED = 4;
        int DB_ERROR = 5;
        int NO_RECORD = 6;
        int EXECUTE_ERROR = 7;
        int TOKEN_INVALID = 8;
        int NO_PERMISSION = 9;
    }

    public interface Timeout {
        int CONNECT_TIMEOUT = 5_000;
    }
}
