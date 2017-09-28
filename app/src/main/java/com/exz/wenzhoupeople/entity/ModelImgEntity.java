package com.exz.wenzhoupeople.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pc on 2017/9/6.
 */

public class ModelImgEntity {

    /**
     * fresh : http://123.png
     * current : http://123.png
     * dryCargo : http://123.png
     * special : http://123.png
     */

    private String fresh;
    private String current;
    private String dryCargo;
    private String special;
    /**
     * sign : http://123.png
     * package : http://123.png
     */

    private String sign;
    @SerializedName("package")
    private String packageX;

    public String getFresh() {
        return fresh;
    }

    public void setFresh(String fresh) {
        this.fresh = fresh;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getDryCargo() {
        return dryCargo;
    }

    public void setDryCargo(String dryCargo) {
        this.dryCargo = dryCargo;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }
}
