package com.exz.wenzhoupeople.entity;

/**
 * Created by weicao on 2017/8/30.
 */

public class FootPrintModel {

        /**
         * id : 1
         * imgUrl : http:123.png
         * title : 商品名称
         * price : 17.8
         * date : 10月12号
         */

        private String id;
        private String imgUrl;
        private String title;
        private String price;
        private String date;

    public FootPrintModel(String id, String imgUrl, String title, String price, String date) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.title = title;
        this.price = price;
        this.date = date;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
}
