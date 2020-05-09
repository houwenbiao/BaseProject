package com.qtimes.wonly.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.qtimes.utils.android.NumberUtils;
import com.qtimes.utils.android.PluLog;
import com.qtimes.wonly.R;
import com.qtimes.wonly.app.App;
import com.qtimes.wonly.delegate.UploadDelegate;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.tus.android.client.TusPreferencesURLStore;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;

/**
 * Author: JackHou
 * Date: 2020/1/7.
 */
public class TusUtil {

    private static final String TAG = "TusUtil";

    private static TusUtil mInstance;
    private UploadTask mUploadTask;

    private TusUtil() {

    }

    public static TusUtil getInstance() {
        if (mInstance == null) {
            mInstance = new TusUtil();
        }
        return mInstance;
    }


    public void uploadBigFile(UploadDelegate mUploadDelegate, String path, String url) throws FileNotFoundException {
        PluLog.i("uploadBigFile: " + path + ", " + url);
        TusClient client = new TusClient();
        try {
            client.setUploadCreationURL(new URL(url));
            Map<String, String> headers = new HashMap<>();
            headers.put("path", "BigFiles");
            client.setHeaders(headers);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Enable resumable uploads by storing the upload URL in SharedPreferences
        SharedPreferences mSP = mUploadDelegate.mContext.getSharedPreferences("tus", Context.MODE_PRIVATE);
        client.enableResuming(new TusPreferencesURLStore(mSP));

        File mFile = new File(path);
        if (!mFile.exists()) {
            PluLog.e("文件不存在，上传失败");
            throw new FileNotFoundException();
        }
        final TusUpload upload = new TusUpload(mFile);
        PluLog.i("Starting upload...");
        mUploadTask = new UploadTask(mUploadDelegate, client, upload, path);
        mUploadTask.execute();
    }

    private static class UploadTask extends AsyncTask<Void, Long, URL> {
        private UploadDelegate mUploadDelegate;
        private TusClient client;
        private TusUpload upload;
        private Exception exception;
        private String mFilePath;

        UploadTask(UploadDelegate mUploadDelegate, TusClient client, TusUpload upload, String filePath) {
            this.mUploadDelegate = mUploadDelegate;
            this.client = client;
            this.upload = upload;
            mFilePath = filePath;

        }

        @Override
        protected void onPreExecute() {
            PluLog.i("onPreExecute");
            mUploadDelegate.updateUploadStatus(UploadDelegate.UploadStatus.PRE_UPLOAD);
        }

        @Override
        protected void onPostExecute(URL uploadURL) {
            PluLog.i("onPostExecute");
            ToastUtil.tip(App.getInstance().getString(R.string.upload_success));
            mUploadDelegate.updateUploadStatus(UploadDelegate.UploadStatus.SUCCESS);
        }

        @Override
        protected void onCancelled() {
            PluLog.i("onCancelled");
            mUploadDelegate.updateUploadStatus(UploadDelegate.UploadStatus.CANCELED);
            if (exception != null) {
                mUploadDelegate.retryUploadImages(mFilePath);
            }
        }

        @Override
        protected void onProgressUpdate(Long... updates) {
            mUploadDelegate.updateUploadStatus(UploadDelegate.UploadStatus.UPLOADING);
            long uploadedBytes = updates[0];
            long totalBytes = updates[1];
            double progress = (double) uploadedBytes / totalBytes * 100;
            PluLog.i("upload progress: " + NumberUtils.DecimalFormat(progress) + "%");
        }

        @Override
        protected URL doInBackground(Void... params) {
            try {
                TusUploader uploader = client.createUpload(upload);
                long totalBytes = upload.getSize();
                long uploadedBytes = uploader.getOffset();

                // Upload file in 1MiB chunks
                uploader.setChunkSize(1024 * 1024);
                while (!isCancelled() && uploader.uploadChunk() > 0) {
                    uploadedBytes = uploader.getOffset();
                    publishProgress(uploadedBytes, totalBytes);
                }

                uploader.finish();
                return uploader.getUploadURL();

            } catch (Exception e) {
                exception = e;
                PluLog.e(TAG + exception.toString());
                cancel(true);
            }
            return null;
        }
    }
}
