package com.exz.wenzhoupeople.entity;

/**
 * Created by weicao on 2017/8/28.
 */

public class AddressModel {
    private String name;
    private String phone;
    private String address;
    private String stu;

    public AddressModel(String name, String phone, String address, String stu) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.stu = stu;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStu() {
        return stu;
    }

    public void setStu(String stu) {
        this.stu = stu;
    }
}
