package com.liwei.wiki.req;

public class UserQueryReq extends PageReq {
    private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserQueryReq{");
        sb.append("loginName='").append(loginName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}