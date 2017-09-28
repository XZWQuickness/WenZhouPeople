package com.exz.wenzhoupeople.entity;

/**
 * Created by pc on 2017/8/23.
 */

public class SeafoodListEntity {

    /**
     * id : 1
     * imgUrl : http:123.png
     * title : 商品名称
     * subTitle : 副标题
     * price : 17.8
     * oldPrice : 30.4
     */

    private String id;
    private String imgUrl;
    private String title;
    private String subTitle;
    private String price;
    private String oldPrice;

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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }
}
