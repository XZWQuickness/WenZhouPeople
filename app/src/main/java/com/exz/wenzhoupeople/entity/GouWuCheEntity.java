package com.exz.wenzhoupeople.entity;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by pc on 2017/8/31.
 */

public class GouWuCheEntity {


    /**
     * isCoupon : 0
     * goodsInfo : [{"shopCarId":"1037","id":"3","imgUrl":"/themes/main/images/good/default_good_conver_thum_220.jpg","title":"礼品包","count":"1","price":"120","skuid":"0","goodsType":""}]
     */

    private String isCoupon;
    private List<GoodsInfoBean> goodsInfo;

    public String getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(String isCoupon) {
        this.isCoupon = isCoupon;
    }

    public List<GoodsInfoBean> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<GoodsInfoBean> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public static class GoodsInfoBean {
        /**
         * shopCarId : 1037
         * id : 3
         * imgUrl : /themes/main/images/good/default_good_conver_thum_220.jpg
         * title : 礼品包
         * count : 1
         * price : 120
         * skuid : 0
         * goodsType :
         */

        private String shopCarId;
        private String id;
        private String goodsId;
        private String goodsCount;
        private String imgUrl;
        private String title;
        private String count;
        private String price;
        private String skuid;
        private String goodsType;
        private boolean check;

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public String getShopCarId() {
            return shopCarId;
        }

        public void setShopCarId(String shopCarId) {
            this.shopCarId = shopCarId;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSkuid() {
            return skuid;
        }

        public void setSkuid(String skuid) {
            this.skuid = skuid;
        }

        public String getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(String goodsType) {
            this.goodsType = goodsType;
        }

        public String getGoodsId() {

            return TextUtils.isEmpty(goodsId)?id:goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsCount() {
            return   TextUtils.isEmpty(goodsCount)?count: goodsCount;
        }

        public void setGoodsCount(String goodsCount) {
            this.goodsCount = goodsCount;
        }
    }
}
