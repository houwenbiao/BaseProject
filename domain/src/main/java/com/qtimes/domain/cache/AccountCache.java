package com.qtimes.domain.cache;

import com.qtimes.domain.bean.AccountInfoBean;

/**
 * 用户信息
 * Created by liutao on 2017/1/3.
 */

 public interface AccountCache extends DeviceCache {
    AccountInfoBean getUserInfo();
}
