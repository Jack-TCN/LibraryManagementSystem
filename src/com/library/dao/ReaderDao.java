package com.library.dao;

import com.library.entity.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderDao extends BaseDao {

    /**
     * 添加读者
     */
    public int addReader(Reader reader) {
        String sql = "INSERT INTO Reader (ReaderName, Gender, ReaderTypeID, " +
                "Phone, Email, Password) VALUES (?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, reader.getReaderName(), reader.getGender(),
                reader.getReaderTypeID(), reader.getPhone(), reader.getEmail(),
                reader.getPassword());
    }

    /**
     * 更新读者信息
     */
    public int updateReader(Reader reader) {
        String sql = "UPDATE Reader SET ReaderName = ?, Gender = ?, " +
                "ReaderTypeID = ?, Phone = ?, Email = ? WHERE ReaderID = ?";
        return executeUpdate(sql, reader.getReaderName(), reader.getGender(),
                reader.getReaderTypeID(), reader.getPhone(), reader.getEmail(),
                reader.getReaderID());
    }

    /**
     * 删除读者
     */
    public int deleteReader(int readerID) {
        String sql = "DELETE FROM Reader WHERE ReaderID = ?";
        return executeUpdate(sql, readerID);
    }

    /**
     * 根据ID查询读者
     */
    public Reader getReaderById(int readerID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Reader reader = null;

        try {
            conn = getConnection();
            String sql = "SELECT r.*, rt.TypeName, rt.BorrowDays, rt.BorrowCount " +
                    "FROM Reader r " +
                    "JOIN ReaderType rt ON r.ReaderTypeID = rt.TypeID " +
                    "WHERE r.ReaderID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, readerID);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                reader = extractReader(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return reader;
    }

    /**
     * 查询所有读者
     */
    public List<Reader> getAllReaders() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reader> readers = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT r.*, rt.TypeName, rt.BorrowDays, rt.BorrowCount " +
                    "FROM Reader r " +
                    "JOIN ReaderType rt ON r.ReaderTypeID = rt.TypeID " +
                    "ORDER BY r.ReaderID";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                readers.add(extractReader(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return readers;
    }

    /**
     * 按姓名模糊查询
     */
    public List<Reader> searchByName(String name) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reader> readers = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT r.*, rt.TypeName, rt.BorrowDays, rt.BorrowCount " +
                    "FROM Reader r " +
                    "JOIN ReaderType rt ON r.ReaderTypeID = rt.TypeID " +
                    "WHERE r.ReaderName LIKE ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + name + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                readers.add(extractReader(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return readers;
    }

    /**
     * 登录验证
     */
    public Reader login(int readerID, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Reader reader = null;

        try {
            conn = getConnection();
            String sql = "SELECT r.*, rt.TypeName, rt.BorrowDays, rt.BorrowCount " +
                    "FROM Reader r " +
                    "JOIN ReaderType rt ON r.ReaderTypeID = rt.TypeID " +
                    "WHERE r.ReaderID = ? AND r.Password = ? AND r.Status = '正常'";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, readerID);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                reader = extractReader(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return reader;
    }

    /**
     * 从ResultSet中提取Reader对象
     */
    private Reader extractReader(ResultSet rs) throws SQLException {
        Reader reader = new Reader();
        reader.setReaderID(rs.getInt("ReaderID"));
        reader.setReaderName(rs.getString("ReaderName"));
        reader.setGender(rs.getString("Gender"));
        reader.setReaderTypeID(rs.getInt("ReaderTypeID"));
        reader.setPhone(rs.getString("Phone"));
        reader.setEmail(rs.getString("Email"));
        reader.setRegDate(rs.getTimestamp("RegDate"));
        reader.setStatus(rs.getString("Status"));
        reader.setPassword(rs.getString("Password"));

        // 设置关联属性
        try {
            reader.setReaderTypeName(rs.getString("TypeName"));
            reader.setBorrowDays(rs.getInt("BorrowDays"));
            reader.setBorrowCount(rs.getInt("BorrowCount"));
        } catch (SQLException e) {
            // 如果没有这些列，忽略
        }

        return reader;
    }
}
