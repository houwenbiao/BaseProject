package com.qtimes.domain.base;

/**
 * author: liutao
 * date: 2016/9/9.
 */
public interface UseCaseGroup<R extends  BaseReqParameter,C extends BaseCallback>{

    void  release();

    void execute(R req, C callback);

    void execute(R req);

    void setRspCallback(C callback);

}
