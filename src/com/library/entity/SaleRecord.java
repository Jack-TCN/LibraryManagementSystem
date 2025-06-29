package com.library.entity;

import java.math.BigDecimal;
import java.util.Date;

public class SaleRecord {
    private int saleID;
    private int readerID;
    private int bookID;
    private int quantity;
    private Date saleDate;
    private BigDecimal totalPrice;

    // Related fields
    private String readerName;
    private String bookTitle;

    public SaleRecord() {}

    public SaleRecord(int readerID, int bookID, int quantity, BigDecimal totalPrice) {
        this.readerID = readerID;
        this.bookID = bookID;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public int getReaderID() {
        return readerID;
    }

    public void setReaderID(int readerID) {
        this.readerID = readerID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}
