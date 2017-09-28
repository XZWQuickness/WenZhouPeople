package com.exz.wenzhoupeople.entity;

import io.realm.RealmObject;

public class BannersBean extends RealmObject {


    /**
     * mark : 0：店铺，1：商品，2：广告
     * id : 店铺、商品id
     * imgUrl : http://123.png
     * adUrl : 广告微页面(暂时为空,以后版本若有则传微页面地址)
     */

    private String mark;
    private String id;
    private String imgUrl;
    private String adUrl;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }
}