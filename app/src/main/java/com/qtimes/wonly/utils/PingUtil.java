package com.qtimes.wonly.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Author: JackHou
 * Date: 2017/11/6.
 */

public class PingUtil {
    public static boolean ping(String host, int pingCount, StringBuffer stringBuffer) {
        String line = null;
        Process process = null;
        BufferedReader successReader = null;
//        String command = "ping -c " + pingCount + " -w 5 " + host;
        String command = "ping -c " + pingCount + " -w 10  " + host;
        boolean isSuccess = false;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process == null) {
                LogUtil.e("ping fail:process is null.");
                append(stringBuffer, "ping fail:process is null.");
                return false;
            }
            successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = successReader.readLine()) != null) {
                LogUtil.i(line);
                append(stringBuffer, line);
            }
            int status = process.waitFor();
            if (status == 0) {
                LogUtil.i("exec cmd success:" + command);
                append(stringBuffer, "exec cmd success:" + command);
                isSuccess = true;
            } else {
                LogUtil.e("exec cmd fail.");
                append(stringBuffer, "exec cmd fail.");
                isSuccess = false;
            }
            LogUtil.i("exec finished.");
            append(stringBuffer, "exec finished.");
        } catch (IOException e) {
            LogUtil.e(e);
        } catch (InterruptedException e) {
            LogUtil.e(e);
        } finally {
            LogUtil.i("ping exit.");
            if (process != null) {
                process.destroy();
            }
            if (successReader != null) {
                try {
                    successReader.close();
                } catch (IOException e) {
                    LogUtil.e(e);
                }
            }
        }
        LogUtil.i("ping isSuccess:" + isSuccess + "StringBuffer:" + stringBuffer.toString());
        return isSuccess;
    }

    private static void append(StringBuffer stringBuffer, String text) {
        if (stringBuffer != null) {
            stringBuffer.append(text + "\n");
        }
    }
}
