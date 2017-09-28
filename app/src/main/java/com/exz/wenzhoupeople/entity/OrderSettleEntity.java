package com.exz.wenzhoupeople.entity;

import java.util.List;

/**
 * Created by pc on 2017/9/12.
 */

public class OrderSettleEntity  {

    /**
     * id :
     * goodsInfo : [{"shopCarId":"","goodsId":"","skuid":"","goodsCount":""}]
     * addressId :
     * couponId :
     * postMoney : 10
     * total : 200
     * msg : 用户留言
     * requestCheck :
     */

    private String id;
    private String addressId;
    private String couponId;
    private String postMoney;
    private String total;
    private String msg;
    private String requestCheck;
    private List<GouWuCheEntity.GoodsInfoBean> goodsInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getPostMoney() {
        return postMoney;
    }

    public void setPostMoney(String postMoney) {
        this.postMoney = postMoney;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRequestCheck() {
        return requestCheck;
    }

    public void setRequestCheck(String requestCheck) {
        this.requestCheck = requestCheck;
    }

    public List<GouWuCheEntity.GoodsInfoBean> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<GouWuCheEntity.GoodsInfoBean> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }



}
