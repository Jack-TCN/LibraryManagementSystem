package com.library.dao;

import com.library.entity.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao extends BaseDao {

    /**
     * 添加图书
     */
    public int addBook(Book book) {
        String sql = "INSERT INTO Book (ISBN, Title, Author, PublisherID, BookTypeID, " +
                "Price, Stock, TotalStock) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, book.getIsbn(), book.getTitle(), book.getAuthor(),
                book.getPublisherID(), book.getBookTypeID(), book.getPrice(),
                book.getStock(), book.getTotalStock());
    }

    /**
     * 更新图书信息
     */
    public int updateBook(Book book) {
        String sql = "UPDATE Book SET ISBN = ?, Title = ?, Author = ?, " +
                "PublisherID = ?, BookTypeID = ?, Price = ?, Stock = ?, " +
                "TotalStock = ? WHERE BookID = ?";
        return executeUpdate(sql, book.getIsbn(), book.getTitle(), book.getAuthor(),
                book.getPublisherID(), book.getBookTypeID(), book.getPrice(),
                book.getStock(), book.getTotalStock(), book.getBookID());
    }

    /**
     * 删除图书
     */
    public int deleteBook(int bookID) {
        // 先检查是否有借阅记录
        if (hasborrowRecords(bookID)) {
            return -1; // 有借阅记录，不能删除
        }
        String sql = "DELETE FROM Book WHERE BookID = ?";
        return executeUpdate(sql, bookID);
    }

    /**
     * 根据ID查询图书
     */
    public Book getBookById(int bookID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Book book = null;

        try {
            conn = getConnection();
            String sql = "SELECT b.*, p.PublisherName, bt.TypeName " +
                    "FROM Book b " +
                    "LEFT JOIN Publisher p ON b.PublisherID = p.PublisherID " +
                    "LEFT JOIN BookType bt ON b.BookTypeID = bt.TypeID " +
                    "WHERE b.BookID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookID);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                book = extractBook(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return book;
    }

    /**
     * 查询所有图书
     */
    public List<Book> getAllBooks() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT b.*, p.PublisherName, bt.TypeName " +
                    "FROM Book b " +
                    "LEFT JOIN Publisher p ON b.PublisherID = p.PublisherID " +
                    "LEFT JOIN BookType bt ON b.BookTypeID = bt.TypeID " +
                    "ORDER BY b.BookID";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                books.add(extractBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return books;
    }

    /**
     * 按书名模糊查询
     */
    public List<Book> searchByTitle(String title) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT b.*, p.PublisherName, bt.TypeName " +
                    "FROM Book b " +
                    "LEFT JOIN Publisher p ON b.PublisherID = p.PublisherID " +
                    "LEFT JOIN BookType bt ON b.BookTypeID = bt.TypeID " +
                    "WHERE b.Title LIKE ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + title + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                books.add(extractBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return books;
    }

    /**
     * 按作者模糊查询
     */
    public List<Book> searchByAuthor(String author) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT b.*, p.PublisherName, bt.TypeName " +
                    "FROM Book b " +
                    "LEFT JOIN Publisher p ON b.PublisherID = p.PublisherID " +
                    "LEFT JOIN BookType bt ON b.BookTypeID = bt.TypeID " +
                    "WHERE b.Author LIKE ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + author + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                books.add(extractBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return books;
    }

    /**
     * 检查图书是否有借阅记录
     */
    private boolean hasborrowRecords(int bookID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String sql = "SELECT COUNT(*) FROM BorrowRecord WHERE BookID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookID);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return false;
    }

    /**
     * 从ResultSet中提取Book对象
     */
    private Book extractBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookID(rs.getInt("BookID"));
        book.setIsbn(rs.getString("ISBN"));
        book.setTitle(rs.getString("Title"));
        book.setAuthor(rs.getString("Author"));
        book.setPublisherID(rs.getInt("PublisherID"));
        book.setBookTypeID(rs.getInt("BookTypeID"));
        book.setPrice(rs.getBigDecimal("Price"));
        book.setStock(rs.getInt("Stock"));
        book.setTotalStock(rs.getInt("TotalStock"));
        book.setInDate(rs.getTimestamp("InDate"));

        // 设置关联属性
        try {
            book.setPublisherName(rs.getString("PublisherName"));
            book.setBookTypeName(rs.getString("TypeName"));
        } catch (SQLException e) {
            // 如果没有这些列，忽略
        }

        return book;
    }
}
