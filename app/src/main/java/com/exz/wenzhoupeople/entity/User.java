package com.exz.wenzhoupeople.entity;

import cn.com.szw.lib.myframework.entities.AbsUser;

/**
 * Created by pc on 2017/7/6.
 */

public class User extends AbsUser{

    /**
     * userId : 0
     * headerUrl : http://123.png
     * name : 昵称
     * vipState : 0
     * vipDate :
     * verifyState : 0
     * verifyName :
     */

    private String userId;
    private String headerUrl;
    private String name;
    private String vipState;
    private String vipDate;
    private String verifyState;
    private String verifyType;
    private String verifyName;

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVipState(String vipState) {
        this.vipState = vipState;
    }

    public void setVipDate(String vipDate) {
        this.vipDate = vipDate;
    }

    public void setVerifyState(String verifyState) {
        this.verifyState = verifyState;
    }

    public void setVerifyName(String verifyName) {
        this.verifyName = verifyName;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public String getName() {
        return name;
    }

    public String getVipState() {
        return vipState;
    }

    public String getVipDate() {
        return vipDate;
    }

    public String getVerifyState() {
        return verifyState;
    }

    public String getVerifyName() {
        return verifyName;
    }
}
