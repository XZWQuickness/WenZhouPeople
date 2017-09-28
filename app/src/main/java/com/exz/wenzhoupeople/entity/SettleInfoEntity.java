package com.exz.wenzhoupeople.entity;

import java.util.List;

/**
 * Created by pc on 2017/9/12.
 */

public class SettleInfoEntity {


    /**
     * address : {"id":"1","name":"姓名","phone":"电话号码","area":"所在地区（省市区）","detail":"详细地址"}
     * goodsInfo : [{"goodsId":"","skuid":"","price":""}]
     * coupon : [{"id":"1","money":"5","limitMoney":"99","limitDate":"2016.02.25"}]
     * postMoney : 10
     */

    private TakeGoodsAddressEntity address;
    private String postMoney;
    private List<GoodsInfoBean> goodsInfo;
    private List<CouponEntity> coupon;

    public TakeGoodsAddressEntity getAddress() {
        return address;
    }

    public void setAddress(TakeGoodsAddressEntity address) {
        this.address = address;
    }

    public String getPostMoney() {
        return postMoney;
    }

    public void setPostMoney(String postMoney) {
        this.postMoney = postMoney;
    }

    public List<GoodsInfoBean> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<GoodsInfoBean> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public List<CouponEntity> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<CouponEntity> coupon) {
        this.coupon = coupon;
    }


    public static class GoodsInfoBean {
        /**
         * goodsId :
         * skuid :
         * price :
         */

        private String goodsId;
        private String skuid;
        private String price;

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getSkuid() {
            return skuid;
        }

        public void setSkuid(String skuid) {
            this.skuid = skuid;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

}
