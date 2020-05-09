package com.qtimes.wonly.delegate;

import android.content.Context;

import com.qtimes.data.constant.Key;
import com.qtimes.data.net.Api;
import com.qtimes.domain.cache.DataCache;
import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.wonly.R;
import com.qtimes.wonly.events.UploadEvent;
import com.qtimes.wonly.events.UploadStatusEvent;
import com.qtimes.wonly.utils.ToastUtil;
import com.qtimes.wonly.utils.TusUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.FileNotFoundException;

import javax.inject.Inject;


/**
 * Author: JackHou
 * Date: 2019/12/21.
 */
public class UploadDelegate implements BaseDelegate {

    public Context mContext;
    private DataCache dataCache;
    private int retryTimes = 0;//重试次数

    /**
     * Log文件上传进度
     */
    public enum UploadStatus {
        RESOLVED("保留"),
        COLLECTING("1/4 采集中"),//采集中
        COMPRESSING("2/4 压缩中"),//zip压缩中
        COMPRESS_FAILED("压缩失败"),
        PRE_UPLOAD("3/4 准备上传"),
        UPLOADING("4/4 上传中"),//上传中
        CANCELED("已取消"),//已取消
        SUCCESS("上传成功");//上传成功

        private String desc;

        UploadStatus(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    private UploadStatus mUploadStatus;

    @Inject
    public UploadDelegate(@ContextLevel(ContextLevel.APPLICATION) Context context,
                          DataCache dataCache) {
        mContext = context;
        this.dataCache = dataCache;
        mUploadStatus = UploadStatus.RESOLVED;
    }

    @Override
    public void register() {
        if (EventBus.getDefault().isRegistered(this)) {
            return;
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUploadImageEvent(UploadEvent event) {

    }

    /**
     * upload files
     */
    private void uploadFiles(String filePath) {
        if (!mUploadStatus.equals(UploadStatus.UPLOADING)) {
            try {
                TusUtil.getInstance().uploadBigFile(this, filePath, Api.URL.UPLOAD_BIG_FILE);
            } catch (FileNotFoundException e) {
                ToastUtil.tip(mContext, mContext.getString(R.string.file_not_exist));
            }
        } else {
            ToastUtil.tip(mContext, mContext.getString(R.string.uploading));
        }
    }

    /**
     * 重试上传image文件
     */
    public void retryUploadImages(String filePath) {
        retryTimes++;
        if (retryTimes < 5) {
            uploadFiles(filePath);
        } else {
            retryTimes = 0;
            String mDeviceName = dataCache.getStringSp(Key.Account.DEVICE_NAME, "");
            ToastUtil.tip(mContext, mContext.getString(R.string.upload_failed_5_times));
        }
    }

    /**
     * 更新log文件上传状态
     *
     * @param status status
     */
    public void updateUploadStatus(UploadStatus status) {
        this.mUploadStatus = status;
        EventBus.getDefault().postSticky(new UploadStatusEvent(status));
    }

}
