package com.library.entity;

import java.util.Date;

public class Reader {
    private int readerID;
    private String readerName;
    private String gender;
    private int readerTypeID;
    private String phone;
    private String email;
    private Date regDate;
    private String status;
    private String password;

    // 关联属性
    private String readerTypeName;
    private int borrowDays;
    private int borrowCount;

    // 无参构造函数
    public Reader() {}

    // 带参构造函数
    public Reader(String readerName, String gender, int readerTypeID,
                  String phone, String email) {
        this.readerName = readerName;
        this.gender = gender;
        this.readerTypeID = readerTypeID;
        this.phone = phone;
        this.email = email;
        this.status = "正常";
        this.password = "123456";
    }

    // Getter和Setter方法
    public int getReaderID() {
        return readerID;
    }

    public void setReaderID(int readerID) {
        this.readerID = readerID;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getReaderTypeID() {
        return readerTypeID;
    }

    public void setReaderTypeID(int readerTypeID) {
        this.readerTypeID = readerTypeID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReaderTypeName() {
        return readerTypeName;
    }

    public void setReaderTypeName(String readerTypeName) {
        this.readerTypeName = readerTypeName;
    }

    public int getBorrowDays() {
        return borrowDays;
    }

    public void setBorrowDays(int borrowDays) {
        this.borrowDays = borrowDays;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }
}
