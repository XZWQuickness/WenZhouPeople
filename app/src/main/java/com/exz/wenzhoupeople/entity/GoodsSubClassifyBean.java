package com.exz.wenzhoupeople.entity;

public class GoodsSubClassifyBean {
    /**
     * goodsSubClassifyName : 黑色
     * goodsSubClassifyId : 1
     * goodsSubClassifyUrl :
     * goodsSubClassifyNextId : 1,2,3,4
     * goodsSubState : 1 on  2 off 3 pass
     */

    private String goodsSubClassifyName;
    private String goodsSubClassifyId;
    private String goodsSubClassifyUrl;
    private String goodsSubClassifyNextId;
    private String goodsSubState = "2";


    public String getGoodsSubState() {
        return goodsSubState;
    }

    public void setGoodsSubState(String goodsSubState) {
        this.goodsSubState = goodsSubState;
    }

    public String getGoodsSubClassifyName() {
        return goodsSubClassifyName;
    }

    public void setGoodsSubClassifyName(String goodsSubClassifyName) {
        this.goodsSubClassifyName = goodsSubClassifyName;
    }

    public String getGoodsSubClassifyId() {
        return goodsSubClassifyId;
    }

    public void setGoodsSubClassifyId(String goodsSubClassifyId) {
        this.goodsSubClassifyId = goodsSubClassifyId;
    }

    public String getGoodsSubClassifyUrl() {
        return goodsSubClassifyUrl;
    }

    public void setGoodsSubClassifyUrl(String goodsSubClassifyUrl) {
        this.goodsSubClassifyUrl = goodsSubClassifyUrl;
    }

    public String getGoodsSubClassifyNextId() {
        return goodsSubClassifyNextId;
    }

    public void setGoodsSubClassifyNextId(String goodsSubClassifyNextId) {
        this.goodsSubClassifyNextId = goodsSubClassifyNextId;
    }
}