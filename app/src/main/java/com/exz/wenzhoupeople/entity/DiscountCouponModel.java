package com.exz.wenzhoupeople.entity;

/**
 * Created by weicao on 2017/8/29.
 */

public class DiscountCouponModel {


        /**
         * id : 1
         * money : 5
         * limitMoney : 99
         * limitDate : 2016.01.25 - 2016.02.25
         * state : 0
         */

        private String id;
        private String money;
        private String limitMoney;
        private String limitDate;
        private String state;

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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
}
