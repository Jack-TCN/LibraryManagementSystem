package com.library.entity;

public class Publisher {
    private int publisherID;
    private String publisherName;
    private String phone;
    private String address;

    // 无参构造函数
    public Publisher() {}

    // 带参构造函数
    public Publisher(String publisherName, String phone, String address) {
        this.publisherName = publisherName;
        this.phone = phone;
        this.address = address;
    }

    // Getter和Setter方法
    public int getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
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

    @Override
    public String toString() {
        return publisherName;
    }
}
