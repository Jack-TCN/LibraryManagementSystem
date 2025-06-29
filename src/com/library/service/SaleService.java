package com.library.service;

import com.library.dao.BookDao;
import com.library.dao.ReaderDao;
import com.library.dao.SaleDao;
import com.library.entity.Book;
import com.library.entity.Reader;
import com.library.entity.SaleRecord;

import java.util.List;

public class SaleService {
    private SaleDao saleDao = new SaleDao();
    private ReaderDao readerDao = new ReaderDao();
    private BookDao bookDao = new BookDao();

    public String sellBook(int readerID, int bookID, int quantity) {
        Reader reader = readerDao.getReaderById(readerID);
        if (reader == null) {
            return "客户不存在";
        }
        Book book = bookDao.getBookById(bookID);
        if (book == null) {
            return "图书不存在";
        }
        if (book.getStock() < quantity) {
            return "图书库存不足";
        }
        return saleDao.sellBook(readerID, bookID, quantity);
    }

    public List<SaleRecord> getAllSales() {
        return saleDao.getAllSales();
    }
}
