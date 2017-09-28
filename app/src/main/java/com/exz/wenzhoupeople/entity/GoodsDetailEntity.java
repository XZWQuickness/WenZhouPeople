package com.exz.wenzhoupeople.entity;

import java.util.List;

/**
 * Created by pc on 2017/9/7.
 */

public class GoodsDetailEntity {

    /**
     * id : 商品id
     * imgUrls : ["http:123.png"]
     * title : 商品名称
     * price : 17.8
     * oldPrice : 30.4
     * postMoney : 快递价格
     * saleCount : 销量
     * address : 所在地
     * isCoupon : 0:没有 1:有（是否有可领取的优惠券）
     * isCollected : 0:未收藏 1:已收藏
     * judgeCount : 全部评论数量
     * stock : 总库存
     * isDelete : 1
     * judgeList : [{"header":"评论者头像","name":"评论者昵称","content":"评论内容","images":["http:123.png"],"date":"评论日期"}]
     */

    private String id;
    private String title;
    private String price;
    private String oldPrice;
    private String postMoney;
    private String saleCount;
    private String address;
    private String isCoupon;
    private String isCollected;
    private String judgeCount;
    private String stock;
    private String isDelete;
    private List<String> imgUrls;
    private List<JudgeListBean> judgeList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getPostMoney() {
        return postMoney;
    }

    public void setPostMoney(String postMoney) {
        this.postMoney = postMoney;
    }

    public String getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(String saleCount) {
        this.saleCount = saleCount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(String isCoupon) {
        this.isCoupon = isCoupon;
    }

    public String getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(String isCollected) {
        this.isCollected = isCollected;
    }

    public String getJudgeCount() {
        return judgeCount;
    }

    public void setJudgeCount(String judgeCount) {
        this.judgeCount = judgeCount;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public List<JudgeListBean> getJudgeList() {
        return judgeList;
    }

    public void setJudgeList(List<JudgeListBean> judgeList) {
        this.judgeList = judgeList;
    }

}
