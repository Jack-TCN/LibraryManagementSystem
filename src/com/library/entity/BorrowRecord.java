package com.library.entity;

import java.math.BigDecimal;
import java.util.Date;

public class BorrowRecord {
    private int recordID;
    private int readerID;
    private int bookID;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private int renewCount;
    private String status;
    private BigDecimal fine;

    // 关联属性
    private String readerName;
    private String bookTitle;
    private String bookAuthor;
    private int overdueDays;

    // 无参构造函数
    public BorrowRecord() {}

    // 带参构造函数
    public BorrowRecord(int readerID, int bookID, Date borrowDate, Date dueDate) {
        this.readerID = readerID;
        this.bookID = bookID;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = "借阅中";
        this.renewCount = 0;
        this.fine = BigDecimal.ZERO;
    }

    // Getter和Setter方法
    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
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

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public int getRenewCount() {
        return renewCount;
    }

    public void setRenewCount(int renewCount) {
        this.renewCount = renewCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getFine() {
        return fine;
    }

    public void setFine(BigDecimal fine) {
        this.fine = fine;
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

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public int getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(int overdueDays) {
        this.overdueDays = overdueDays;
    }
}
