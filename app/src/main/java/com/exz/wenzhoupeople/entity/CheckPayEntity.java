package com.exz.wenzhoupeople.entity;

/**
 * Created by weicao on 2017/9/14.
 */

public class CheckPayEntity {


        /**
         * payState :
         * payMoney :
         * paySuccessDate :
         */

        private String payState;
        private String payMoney;
        private String paySuccessDate;

        public String getPayState() {
            return payState;
        }

        public void setPayState(String payState) {
            this.payState = payState;
        }

        public String getPayMoney() {
            return payMoney;
        }

        public void setPayMoney(String payMoney) {
            this.payMoney = payMoney;
        }

        public String getPaySuccessDate() {
            return paySuccessDate;
        }

        public void setPaySuccessDate(String paySuccessDate) {
            this.paySuccessDate = paySuccessDate;
        }
}
