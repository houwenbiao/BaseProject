package com.qtimes.pavilion.utils;

import android.util.Log;

import com.qtimes.pavilion.R;
import com.qtimes.pavilion.app.App;
import com.qtimes.utils.android.PluLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Author: JackHou
 * Date: 2018/3/5.
 */

public class ApkInstallUtil {

    /**
     * 提升读写权限
     *
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static void setPermission(String filePath) {
        String command = "chmod " + "777" + " " + filePath;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 静默安装
     *
     * @param apkPath apk路径
     */
    public static boolean installSilent(final String apkPath) {
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        try {
            process = new ProcessBuilder("pm", "install", "-i", App.getInstance().getString(R.string.package_name), "-r", apkPath).start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (Exception e) {
            PluLog.e(e);
        }
        finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {
                PluLog.e(e);
            }
            if (process != null) {
                process.destroy();
            }
        }
        Log.e("result", "" + errorMsg.toString());
        //如果含有“success”单词则认为安装成功
        return successMsg.toString().equalsIgnoreCase("success");
    }

    /**
     * root权限安装
     *
     * @param apkPath apk路径
     */
    public static void installRoot(final String apkPath) {
        Runtime localRT = Runtime.getRuntime();
        try {
            localRT.exec("setprop persist.service.adb.enable 1").waitFor();
            Process localProcess = localRT.exec("adb install -r " + apkPath);
            new StreamGobbler(localProcess, localProcess.getErrorStream(), "INFO").start();
            int result = localProcess.waitFor();
            PluLog.i("result:" + result);
            if (result == 0) {
                PluLog.i("silent install completed");
            } else {
                PluLog.i("silent install fail");
                Process lp = localRT.exec("chmod -R 777 " + apkPath);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class StreamGobbler extends Thread {
        InputStream is;
        String type;
        Process mP;

        StreamGobbler(Process p, InputStream is, String type) {
            this.is = is;
            this.type = type;
            mP = p;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
//                    debug.LogV( type + " > " + line);
                    if (line.contains("error")) {
                        mP.destroy();
                    }
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
