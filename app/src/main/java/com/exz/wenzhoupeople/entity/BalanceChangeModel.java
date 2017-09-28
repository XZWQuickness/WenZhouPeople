package com.exz.wenzhoupeople.entity;

/**
 * Created by weicao on 2017/9/8.
 */

public class BalanceChangeModel {


        /**
         * title : 微信充值
         * date : 2017-10-01 15:35
         * money : 2000
         * isIncrease : 1
         */

        private String title;
        private String date;
        private String money;
        private String isIncrease;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getIsIncrease() {
            return isIncrease;
        }

        public void setIsIncrease(String isIncrease) {
            this.isIncrease = isIncrease;
        }
}
