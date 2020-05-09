package com.qtimes.wonly.delegate;

/**
 * 统一管理Delegate
 * Created by liutao on 2017/1/14.
 */

public class BaseDelegateManager implements BaseDelegate {
    private BaseDelegate delegates[];
    private boolean isRegister;

    public BaseDelegateManager(BaseDelegate... delegates) {
        this.delegates = delegates;
    }

    @Override
    public void register() {
        if (isRegister) {
            return;
        }
        if (delegates != null && delegates.length > 0) {
            for (BaseDelegate delegate : delegates) {
                if (delegate != null) {
                    delegate.register();
                }
            }
        }
        isRegister = true;
    }

    @Override
    public void unRegister() {
        if (delegates != null && delegates.length > 0) {
            for (BaseDelegate delegate : delegates) {
                if (delegate != null) {
                    delegate.unRegister();
                }
            }
        }
    }
}
