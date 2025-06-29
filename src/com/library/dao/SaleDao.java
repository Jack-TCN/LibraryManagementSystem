package com.library.dao;

import com.library.entity.SaleRecord;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDao extends BaseDao {

    public String sellBook(int readerID, int bookID, int quantity) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // check stock and price
            String sql = "SELECT Price, Stock FROM Book WHERE BookID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookID);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                return "图书不存在";
            }
            BigDecimal price = rs.getBigDecimal("Price");
            int stock = rs.getInt("Stock");
            rs.close();
            pstmt.close();

            if (stock < quantity) {
                return "图书库存不足";
            }

            BigDecimal total = price.multiply(new BigDecimal(quantity));
            sql = "INSERT INTO SaleRecord (ReaderID, BookID, Quantity, SaleDate, TotalPrice) " +
                    "VALUES (?, ?, ?, GETDATE(), ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, readerID);
            pstmt.setInt(2, bookID);
            pstmt.setInt(3, quantity);
            pstmt.setBigDecimal(4, total);
            pstmt.executeUpdate();
            pstmt.close();

            sql = "UPDATE Book SET Stock = Stock - ? WHERE BookID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, bookID);
            pstmt.executeUpdate();

            conn.commit();
            return "购书成功";
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return "购书失败:" + e.getMessage();
        } finally {
            closeAll(conn, pstmt, rs);
        }
    }

    public List<SaleRecord> getAllSales() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<SaleRecord> list = new ArrayList<>();
        try {
            conn = getConnection();
            String sql = "SELECT s.*, r.ReaderName, b.Title FROM SaleRecord s " +
                    "LEFT JOIN Reader r ON s.ReaderID = r.ReaderID " +
                    "LEFT JOIN Book b ON s.BookID = b.BookID " +
                    "ORDER BY s.SaleID DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SaleRecord record = new SaleRecord();
                record.setSaleID(rs.getInt("SaleID"));
                record.setReaderID(rs.getInt("ReaderID"));
                record.setBookID(rs.getInt("BookID"));
                record.setQuantity(rs.getInt("Quantity"));
                record.setSaleDate(rs.getTimestamp("SaleDate"));
                record.setTotalPrice(rs.getBigDecimal("TotalPrice"));
                record.setReaderName(rs.getString("ReaderName"));
                record.setBookTitle(rs.getString("Title"));
                list.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }
        return list;
    }
}
