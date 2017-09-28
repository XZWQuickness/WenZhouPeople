package com.exz.wenzhoupeople.entity;

import java.util.List;

/**
 * Created by weicao on 2017/8/29.
 */

public class EvaluateModel {


    /**
     * id : 6
     * imgUrl : http://qxw1649650091.my3w.com/UploadFiles/goods/2017091416262482435298_thum_220.jpg
     * title : 八爪鱼干
     * images : [""]
     * content : %E5%85%AB%E6%8A%98%E6%8A%93
     * date : 2017-09-16 09:26:25
     * score : 2
     */

    private String id;
    private String imgUrl;
    private String title;
    private String content;
    private String date;
    private String score;
    private List<String> images;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
