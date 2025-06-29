package com.library.service;

import com.library.dao.BorrowDao;
import com.library.dao.ReaderDao;
import com.library.dao.BookDao;
import com.library.entity.BorrowRecord;
import com.library.entity.Reader;
import com.library.entity.Book;
import java.util.List;

public class BorrowService {
    private BorrowDao borrowDao = new BorrowDao();
    private ReaderDao readerDao = new ReaderDao();
    private BookDao bookDao = new BookDao();

    /**
     * 借书
     */
    public String borrowBook(int readerID, int bookID) {
        // 验证读者
        Reader reader = readerDao.getReaderById(readerID);
        if (reader == null) {
            return "读者不存在";
        }
        if (!"正常".equals(reader.getStatus())) {
            return "读者状态异常，无法借书";
        }

        // 验证图书
        Book book = bookDao.getBookById(bookID);
        if (book == null) {
            return "图书不存在";
        }
        if (book.getStock() <= 0) {
            return "图书库存不足";
        }

        // 调用存储过程进行借书
        return borrowDao.borrowBook(readerID, bookID);
    }

    /**
     * 还书
     */
    public String returnBook(int recordID) {
        if (recordID <= 0) {
            return "借阅记录ID无效";
        }

        // 调用存储过程进行还书
        return borrowDao.returnBook(recordID);
    }

    /**
     * 获取所有借阅记录
     */
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowDao.getAllBorrowRecords();
    }

    /**
     * 获取读者的借阅记录
     */
    public List<BorrowRecord> getBorrowRecordsByReader(int readerID) {
        return borrowDao.getBorrowRecordsByReader(readerID);
    }

    /**
     * 获取当前借阅（未还）记录
     */
    public List<BorrowRecord> getCurrentBorrowRecords() {
        return borrowDao.getCurrentBorrowRecords();
    }

    /**
     * 获取逾期未还记录
     */
    public List<BorrowRecord> getOverdueBorrowRecords() {
        return borrowDao.getOverdueBorrowRecords();
    }

    /**
     * 检查读者是否可以借书
     */
    public boolean canBorrow(int readerID) {
        Reader reader = readerDao.getReaderById(readerID);
        if (reader == null || !"正常".equals(reader.getStatus())) {
            return false;
        }

        // 检查当前借阅数量
        List<BorrowRecord> currentBorrows = getBorrowRecordsByReader(readerID);
        int borrowingCount = 0;
        boolean hasOverdue = false;

        for (BorrowRecord record : currentBorrows) {
            if ("借阅中".equals(record.getStatus())) {
                borrowingCount++;
                if (record.getOverdueDays() > 0) {
                    hasOverdue = true;
                }
            }
        }

        // 有逾期或达到借阅上限
        if (hasOverdue || borrowingCount >= reader.getBorrowCount()) {
            return false;
        }

        return true;
    }
}
