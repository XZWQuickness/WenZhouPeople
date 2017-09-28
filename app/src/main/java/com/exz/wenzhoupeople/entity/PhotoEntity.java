package com.exz.wenzhoupeople.entity;

/**
 * Created by pc on 2017/6/26.
 */

public class PhotoEntity {
    Object photoImg;
    String type;
    String deleteImg;

    public PhotoEntity(Object photoImg, String type) {
        this.photoImg=photoImg;
        this.type=type;
    }


    public Object getPhotoImg() {
        return photoImg;
    }

    public void setPhotoImg(Object photoImg) {
        this.photoImg = photoImg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeleteImg() {
        return deleteImg;
    }

    public void setDeleteImg(String deleteImg) {
        this.deleteImg = deleteImg;
    }
}
