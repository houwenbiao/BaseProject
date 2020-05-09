package com.qtimes.wonly.dagger.base;

import androidx.annotation.NonNull;

/**所有component的基类
 * Created by lt on 2016/5/24.
 */

/**
 *
 * @param <C> 需要返回的component
 * @param <T> 需要传入的component
 */
public interface BaseDagger<C,T> {
    C initComponent(@NonNull T component);

}
