package com.exz.wenzhoupeople.entity;

/**
 * Created by pc on 2017/9/8.
 */

public class JudgeCountEntity {

    /**
     * total : 全部数量
     * nice : 好评数量
     * normal : 中评数量
     * poor : 差评数量
     * haveImg : 晒图数量
     */

    private String total;
    private String nice;
    private String normal;
    private String poor;
    private String haveImg;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNice() {
        return nice;
    }

    public void setNice(String nice) {
        this.nice = nice;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getPoor() {
        return poor;
    }

    public void setPoor(String poor) {
        this.poor = poor;
    }

    public String getHaveImg() {
        return haveImg;
    }

    public void setHaveImg(String haveImg) {
        this.haveImg = haveImg;
    }
}
