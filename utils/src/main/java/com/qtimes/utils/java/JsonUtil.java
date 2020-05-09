package com.qtimes.utils.java;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**json的util
 * Created by liutao on 16-10-17.
 */

public class JsonUtil {


    public static JSONObject safePut(JSONObject object, String key, String value) {
        try {
            return object.put(key, value);
        } catch (JSONException exception) {
            return object;
        }
    }

    public static JSONObject safePut(JSONObject var0, String var1, int var2) {
        try {
            return var0.put(var1, var2);
        } catch (JSONException var4) {
            return var0;
        }
    }

    public static JSONObject safePut(JSONObject var0, String var1, long var2) {
        try {
            return var0.put(var1, var2);
        } catch (JSONException var5) {
            return var0;
        }
    }

    public static JSONObject safePut(JSONObject var0, String var1, boolean var2) {
        try {
            return var0.put(var1, var2);
        } catch (JSONException var4) {
            return var0;
        }
    }

    public static JSONObject safePut(JSONObject var0, String var1, double var2) {
        try {
            return var0.put(var1, var2);
        } catch (JSONException var5) {
            return var0;
        }
    }

    public static JSONObject safePut(JSONObject var0, String var1, JSONObject var2) {
        try {
            return var0.put(var1, var2);
        } catch (JSONException var4) {
            return var0;
        }
    }

    public static JSONObject safePut(JSONObject var0, String var1, JSONArray var2) {
        try {
            return var0.put(var1, var2);
        } catch (JSONException var4) {
            return var0;
        }
    }

    public static JSONArray safePut(JSONArray var0, Object var1) {
        return var0.put(var1);
    }
}
