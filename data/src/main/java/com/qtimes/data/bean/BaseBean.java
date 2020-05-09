package com.qtimes.data.bean;

/**暂用
 * Created by liutao on 2017/3/4.
 */

public class BaseBean<T> {
    private T duration_result;
    private T list_result;

    public void setT(T t) {
        this.duration_result = t;
    }

    public T getT() {
        return duration_result;
    }


    public void setList_result(T list_result) {
        this.list_result = list_result;
    }

    public T getList_result() {
        return list_result;
    }
}
