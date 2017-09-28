package com.exz.wenzhoupeople.entity;

/**
 * Created by weicao on 2017/8/31.
 */

public class EnshrineModel {


        /**
         * id : 1
         * imgUrl : http:123.png
         * title : 商品名称
         * price : 17.8
         * downPrice : 降价额度
         * state : 1:已降价、2:已失效
         */

        private String id;
        private String imgUrl;
        private String title;
        private String price;
        private String downPrice;
        private String state;

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDownPrice() {
            return downPrice;
        }

        public void setDownPrice(String downPrice) {
            this.downPrice = downPrice;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
}
