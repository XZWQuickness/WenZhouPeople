package com.exz.wenzhoupeople.entity;

import java.io.Serializable;

/**
 * Created by pc on 2017/8/29.
 */

public class TakeGoodsAddressEntity implements Serializable {

    /**
     * id : id
     * name : 姓名
     * phone : 电话号码
     * area : 所在地区（省市区）
     * detail : 详细地址
     * type : 0
     * isDef : 0
     */

    private String id="";
    private String name="";
    private String phone="";
    private String area="";
    private String detail="";
    private String type="";
    private String isDef="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsDef() {
        return isDef;
    }

    public void setIsDef(String isDef) {
        this.isDef = isDef;
    }
}
