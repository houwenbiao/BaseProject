package com.qtimes.domain.bean;

/**
 * Author: JackHou
 * Date: 2020/2/10.
 */
public class AuthenticateRes {
    private AccessBean access;

    public AuthenticateRes(AccessBean access) {
        this.access = access;
    }

    public AccessBean getAccess() {
        return access;
    }

    public void setAccess(AccessBean access) {
        this.access = access;
    }
}
