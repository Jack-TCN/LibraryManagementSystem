package com.library.dao;

import com.library.entity.BorrowRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class BorrowDao extends BaseDao {

    /**
     * 借书（调用存储过程）
     */
    public String borrowBook(int readerID, int bookID) {
        Connection conn = null;
        CallableStatement cstmt = null;
        String message = "";

        try {
            conn = getConnection();
            cstmt = conn.prepareCall("{call SP_BorrowBook(?, ?, ?, ?)}");
            cstmt.setInt(1, readerID);
            cstmt.setInt(2, bookID);
            cstmt.registerOutParameter(3, Types.INTEGER);
            cstmt.registerOutParameter(4, Types.NVARCHAR);

            cstmt.execute();

            int result = cstmt.getInt(3);
            message = cstmt.getString(4);

            if (result != 1) {
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "借书失败：" + e.getMessage();
        } finally {
            try {
                if (cstmt != null) cstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return message;
    }

    /**
     * 还书（调用存储过程）
     */
    public String returnBook(int recordID) {
        Connection conn = null;
        CallableStatement cstmt = null;
        String message = "";
        BigDecimal fine = BigDecimal.ZERO;

        try {
            conn = getConnection();
            cstmt = conn.prepareCall("{call SP_ReturnBook(?, ?, ?, ?)}");
            cstmt.setInt(1, recordID);
            cstmt.registerOutParameter(2, Types.INTEGER);
            cstmt.registerOutParameter(3, Types.NVARCHAR);
            cstmt.registerOutParameter(4, Types.DECIMAL);

            cstmt.execute();

            int result = cstmt.getInt(2);
            message = cstmt.getString(3);
            fine = cstmt.getBigDecimal(4);

            if (result == 1 && fine.compareTo(BigDecimal.ZERO) > 0) {
                message += "，罚款：" + fine + "元";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "还书失败：" + e.getMessage();
        } finally {
            try {
                if (cstmt != null) cstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return message;
    }

    /**
     * 查询所有借阅记录
     */
    public List<BorrowRecord> getAllBorrowRecords() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BorrowRecord> records = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT br.*, r.ReaderName, b.Title, b.Author, " +
                    "CASE WHEN br.ReturnDate IS NULL AND br.DueDate < GETDATE() " +
                    "THEN DATEDIFF(DAY, br.DueDate, GETDATE()) ELSE 0 END AS OverdueDays " +
                    "FROM BorrowRecord br " +
                    "JOIN Reader r ON br.ReaderID = r.ReaderID " +
                    "JOIN Book b ON br.BookID = b.BookID " +
                    "ORDER BY br.RecordID DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                records.add(extractBorrowRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return records;
    }

    /**
     * 查询读者的借阅记录
     */
    public List<BorrowRecord> getBorrowRecordsByReader(int readerID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BorrowRecord> records = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT br.*, r.ReaderName, b.Title, b.Author, " +
                    "CASE WHEN br.ReturnDate IS NULL AND br.DueDate < GETDATE() " +
                    "THEN DATEDIFF(DAY, br.DueDate, GETDATE()) ELSE 0 END AS OverdueDays " +
                    "FROM BorrowRecord br " +
                    "JOIN Reader r ON br.ReaderID = r.ReaderID " +
                    "JOIN Book b ON br.BookID = b.BookID " +
                    "WHERE br.ReaderID = ? " +
                    "ORDER BY br.RecordID DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, readerID);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                records.add(extractBorrowRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return records;
    }

    /**
     * 查询当前借阅（未还）记录
     */
    public List<BorrowRecord> getCurrentBorrowRecords() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BorrowRecord> records = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT br.*, r.ReaderName, b.Title, b.Author, " +
                    "CASE WHEN br.DueDate < GETDATE() " +
                    "THEN DATEDIFF(DAY, br.DueDate, GETDATE()) ELSE 0 END AS OverdueDays " +
                    "FROM BorrowRecord br " +
                    "JOIN Reader r ON br.ReaderID = r.ReaderID " +
                    "JOIN Book b ON br.BookID = b.BookID " +
                    "WHERE br.Status = '借阅中' " +
                    "ORDER BY br.DueDate";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                records.add(extractBorrowRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return records;
    }

    /**
     * 查询逾期未还记录
     */
    public List<BorrowRecord> getOverdueBorrowRecords() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BorrowRecord> records = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT * FROM V_OverdueBorrow ORDER BY OverdueDays DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BorrowRecord record = new BorrowRecord();
                record.setReaderID(rs.getInt("ReaderID"));
                record.setReaderName(rs.getString("ReaderName"));
                record.setBookID(rs.getInt("BookID"));
                record.setBookTitle(rs.getString("Title"));
                record.setBorrowDate(rs.getTimestamp("BorrowDate"));
                record.setDueDate(rs.getTimestamp("DueDate"));
                record.setOverdueDays(rs.getInt("OverdueDays"));
                record.setFine(rs.getBigDecimal("Fine"));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return records;
    }

    /**
     * 从ResultSet中提取BorrowRecord对象
     */
    private BorrowRecord extractBorrowRecord(ResultSet rs) throws SQLException {
        BorrowRecord record = new BorrowRecord();
        record.setRecordID(rs.getInt("RecordID"));
        record.setReaderID(rs.getInt("ReaderID"));
        record.setBookID(rs.getInt("BookID"));
        record.setBorrowDate(rs.getTimestamp("BorrowDate"));
        record.setDueDate(rs.getTimestamp("DueDate"));
        record.setReturnDate(rs.getTimestamp("ReturnDate"));
        record.setRenewCount(rs.getInt("RenewCount"));
        record.setStatus(rs.getString("Status"));
        record.setFine(rs.getBigDecimal("Fine"));

        // 设置关联属性
        try {
            record.setReaderName(rs.getString("ReaderName"));
            record.setBookTitle(rs.getString("Title"));
            record.setBookAuthor(rs.getString("Author"));
            record.setOverdueDays(rs.getInt("OverdueDays"));
        } catch (SQLException e) {
            // 如果没有这些列，忽略
        }

        return record;
    }
}
