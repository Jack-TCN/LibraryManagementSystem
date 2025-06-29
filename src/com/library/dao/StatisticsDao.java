package com.library.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsDao extends BaseDao {

    /**
     * 统计各类图书数量
     */
    public List<Map<String, Object>> getBookTypeStatistics() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT bt.TypeName, COUNT(b.BookID) AS BookCount, " +
                    "ISNULL(SUM(b.TotalStock), 0) AS TotalStock " +
                    "FROM BookType bt " +
                    "LEFT JOIN Book b ON bt.TypeID = b.BookTypeID " +
                    "GROUP BY bt.TypeID, bt.TypeName " +
                    "ORDER BY BookCount DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("TypeName", rs.getString("TypeName"));
                map.put("BookCount", rs.getInt("BookCount"));
                map.put("TotalStock", rs.getInt("TotalStock"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return list;
    }

    /**
     * 统计各类读者借阅情况
     */
    public List<Map<String, Object>> getReaderTypeStatistics() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT rt.TypeName, COUNT(DISTINCT r.ReaderID) AS ReaderCount, " +
                    "COUNT(br.RecordID) AS TotalBorrow, " +
                    "COUNT(CASE WHEN br.Status = '借阅中' THEN 1 END) AS CurrentBorrow " +
                    "FROM ReaderType rt " +
                    "LEFT JOIN Reader r ON rt.TypeID = r.ReaderTypeID " +
                    "LEFT JOIN BorrowRecord br ON r.ReaderID = br.ReaderID " +
                    "GROUP BY rt.TypeID, rt.TypeName";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("TypeName", rs.getString("TypeName"));
                map.put("ReaderCount", rs.getInt("ReaderCount"));
                map.put("TotalBorrow", rs.getInt("TotalBorrow"));
                map.put("CurrentBorrow", rs.getInt("CurrentBorrow"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return list;
    }

    /**
     * 月度借阅统计
     */
    public List<Map<String, Object>> getMonthlyBorrowStatistics() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT YEAR(BorrowDate) AS Year, MONTH(BorrowDate) AS Month, " +
                    "COUNT(*) AS BorrowCount " +
                    "FROM BorrowRecord " +
                    "WHERE BorrowDate >= DATEADD(MONTH, -12, GETDATE()) " +
                    "GROUP BY YEAR(BorrowDate), MONTH(BorrowDate) " +
                    "ORDER BY Year DESC, Month DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("Year", rs.getInt("Year"));
                map.put("Month", rs.getInt("Month"));
                map.put("BorrowCount", rs.getInt("BorrowCount"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return list;
    }

    /**
     * 热门图书排行
     */
    public List<Map<String, Object>> getTopBooks(int topN) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT TOP " + topN + " b.BookID, b.Title, b.Author, " +
                    "COUNT(br.RecordID) AS BorrowTimes " +
                    "FROM Book b " +
                    "LEFT JOIN BorrowRecord br ON b.BookID = br.BookID " +
                    "GROUP BY b.BookID, b.Title, b.Author " +
                    "ORDER BY BorrowTimes DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("BookID", rs.getInt("BookID"));
                map.put("Title", rs.getString("Title"));
                map.put("Author", rs.getString("Author"));
                map.put("BorrowTimes", rs.getInt("BorrowTimes"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return list;
    }
}