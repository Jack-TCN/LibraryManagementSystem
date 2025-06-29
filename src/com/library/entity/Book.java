package com.library.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Book {
    private int bookID;
    private String isbn;
    private String title;
    private String author;
    private int publisherID;
    private int bookTypeID;
    private BigDecimal price;
    private int stock;
    private int totalStock;
    private Date inDate;

    // 关联属性
    private String publisherName;
    private String bookTypeName;

    // 无参构造函数
    public Book() {}

    // 带参构造函数
    public Book(String isbn, String title, String author, int publisherID,
                int bookTypeID, BigDecimal price, int stock, int totalStock) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisherID = publisherID;
        this.bookTypeID = bookTypeID;
        this.price = price;
        this.stock = stock;
        this.totalStock = totalStock;
    }

    // Getter和Setter方法
    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }

    public int getBookTypeID() {
        return bookTypeID;
    }

    public void setBookTypeID(int bookTypeID) {
        this.bookTypeID = bookTypeID;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(int totalStock) {
        this.totalStock = totalStock;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getBookTypeName() {
        return bookTypeName;
    }

    public void setBookTypeName(String bookTypeName) {
        this.bookTypeName = bookTypeName;
    }
}