package com.qtimes.wonly.utils;

import android.os.FileObserver;

import com.qtimes.utils.android.PluLog;

import androidx.annotation.Nullable;

/**
 * Author: JackHou
 * Date: 2019/10/11.
 * 文件监听
 */
public class FileListenerUtils extends FileObserver {

    public FileListenerUtils(String path, int mask) {
        super(path, mask);
    }

    @Override
    public void onEvent(int event, @Nullable String path) {

        switch (event) {
            case FileObserver.MODIFY:
                PluLog.i("File has modify");
                break;

            case FileObserver.CREATE:
                PluLog.i("File has created");
                break;

            case FileObserver.DELETE:
                PluLog.i("File has deleted");
                break;
            default:
                break;
        }
    }
}
