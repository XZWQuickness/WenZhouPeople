package com.exz.wenzhoupeople.entity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by pc on 2017/8/22.
 */

public class MsgListEntity {

    /**
     * id : 消息id
     * orderId : 订单id
     * title : 标题
     * img : http://123.png
     * subTitle : 内容
     */

    private String id;
    private String orderId;
    private String title;
    private String img;
    private String subTitle;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTitle() throws UnsupportedEncodingException {
        return URLDecoder.decode(title,"UTF-8");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSubTitle() throws UnsupportedEncodingException {
        return  URLDecoder.decode(subTitle,"UTF-8");
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
