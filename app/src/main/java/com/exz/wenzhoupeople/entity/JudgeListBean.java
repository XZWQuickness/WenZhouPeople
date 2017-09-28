package com.exz.wenzhoupeople.entity;

import java.util.List;

/**
 * Created by pc on 2017/9/8.
 */

public class JudgeListBean {
    /**
     * header : 评论者头像
     * name : 评论者昵称
     * content : 评论内容
     * images : ["http:123.png"]
     * date : 评论日期
     */

    private String header;
    private String name;
    private String content;
    private String date;
    private String star;
    private List<String> images;

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
