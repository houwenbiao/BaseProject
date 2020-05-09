package com.qtimes.data.util;


import com.qtimes.data.bean.BaseResponse;
import com.qtimes.data.net.Api;
import com.qtimes.domain.bean.ResultException;
import com.qtimes.utils.android.PluLog;

import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by liutao on 2017/1/13.
 */

public class BaseMapper<T extends BaseResponse<R>, R> implements Func1<T, R> {
    private Action0 action;

    public BaseMapper(Action0 action) {
        this.action = action;
    }

    public BaseMapper() {
    }

    @Override
    public R call(T t) {

        PluLog.i("code:" + t.getCode() + ",data:" + t.getData());
        int code = Integer.valueOf(t.getCode());
        if (code != Api.Response.SUCCESS) {
            throw new ResultException(code, t.getMsg());
        }
        if (action != null) {
            action.call();
        }
        return t.getData();
    }
}
