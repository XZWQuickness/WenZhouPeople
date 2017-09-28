package com.exz.wenzhoupeople.entity;

/**
 * Created by weicao on 2017/8/29.
 */

public class BanlanceChangeModel {

    private String content;
    private String data;
    private String price;

    public BanlanceChangeModel(String content, String data, String price) {
        this.content = content;
        this.data = data;
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
