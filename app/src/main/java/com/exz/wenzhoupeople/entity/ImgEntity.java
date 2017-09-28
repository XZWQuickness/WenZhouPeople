package com.exz.wenzhoupeople.entity;

/**
 * Created by pc on 2017/9/9.
 */

public class ImgEntity {
    String url;
    int  error;

    public ImgEntity(String url, int error) {
        this.url=url;
        this.error=error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}
