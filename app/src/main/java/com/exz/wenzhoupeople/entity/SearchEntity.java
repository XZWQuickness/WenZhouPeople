package com.exz.wenzhoupeople.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by 史忠文
 * on 2017/5/5.
 */
@Entity
public class SearchEntity {
    private String searchContent;
    @Id
    private Date date;

    @Generated(hash = 397443051)
    public SearchEntity(String searchContent, Date date) {
        this.searchContent = searchContent;
        this.date = date;
    }

    @Generated(hash = 1021466028)
    public SearchEntity() {
    }

    public String getSearchContent() {
        return this.searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
