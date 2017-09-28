package com.exz.wenzhoupeople.entity;

/**
 * Created by pc on 2017/8/28.
 */

public class CouponEntity {

    /**
     * id : 1
     * money : 5
     * limitMoney : 99
     * limitDate : 2016.01.25 - 2016.02.25
     */

    private String id;
    private String money;
    private String limitMoney;
    private String limitDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(String limitMoney) {
        this.limitMoney = limitMoney;
    }

    public String getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(String limitDate) {
        this.limitDate = limitDate;
    }
}
