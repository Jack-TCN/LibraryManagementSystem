package com.library.entity;

public class BookType {
    private int typeID;
    private String typeName;
    private String description;

    // 无参构造函数
    public BookType() {}

    // 带参构造函数
    public BookType(String typeName, String description) {
        this.typeName = typeName;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
