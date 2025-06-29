package com.library.entity;

import java.util.Date;

public class ReaderType {
    private int typeID;
    private String typeName;
    private int borrowDays;
    private int borrowCount;
    private Date createTime;

    // 无参构造函数
    public ReaderType() {}

    // 带参构造函数
    public ReaderType(String typeName, int borrowDays, int borrowCount) {
        this.typeName = typeName;
        this.borrowDays = borrowDays;
        this.borrowCount = borrowCount;
    }

    // Getter和Setter方法
    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
