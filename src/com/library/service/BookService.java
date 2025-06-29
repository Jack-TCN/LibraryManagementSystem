package com.library.service;

import com.library.dao.BookDao;
import com.library.entity.Book;
import java.util.List;

public class BookService {
    private BookDao bookDao = new BookDao();

    /**
     * 添加图书
     */
    public boolean addBook(Book book) {
        // 验证数据
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            return false;
        }
        if (book.getPrice() == null || book.getPrice().doubleValue() < 0) {
            return false;
        }
        if (book.getStock() < 0 || book.getTotalStock() < book.getStock()) {
            return false;
        }

        return bookDao.addBook(book) > 0;
    }

    /**
     * 更新图书信息
     */
    public boolean updateBook(Book book) {
        // 验证数据
        if (book.getBookID() <= 0) {
            return false;
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            return false;
        }
        if (book.getPrice() == null || book.getPrice().doubleValue() < 0) {
            return false;
        }
        if (book.getStock() < 0 || book.getTotalStock() < book.getStock()) {
            return false;
        }

        return bookDao.updateBook(book) > 0;
    }

    /**
     * 删除图书
     */
    public String deleteBook(int bookID) {
        int result = bookDao.deleteBook(bookID);
        if (result == -1) {
            return "该图书有借阅记录，无法删除";
        } else if (result > 0) {
            return "删除成功";
        } else {
            return "删除失败";
        }
    }

    /**
     * 根据ID查询图书
     */
    public Book getBookById(int bookID) {
        return bookDao.getBookById(bookID);
    }

    /**
     * 获取所有图书
     */
    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }

    /**
     * 按书名搜索
     */
    public List<Book> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookDao.searchByTitle(title.trim());
    }

    /**
     * 按作者搜索
     */
    public List<Book> searchByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookDao.searchByAuthor(author.trim());
    }

    /**
     * 综合搜索（书名或作者）
     */
    public List<Book> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }

        // 合并书名和作者的搜索结果
        List<Book> titleResults = bookDao.searchByTitle(keyword.trim());
        List<Book> authorResults = bookDao.searchByAuthor(keyword.trim());

        // 去重合并
        for (Book book : authorResults) {
            boolean exists = false;
            for (Book b : titleResults) {
                if (b.getBookID() == book.getBookID()) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                titleResults.add(book);
            }
        }

        return titleResults;
    }
}
