package com.library.dao;

import com.library.entity.Admin;
import java.sql.*;

public class AdminDao extends BaseDao {

    /**
     * 管理员登录
     */
    public Admin login(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Admin admin = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM Admin WHERE Username = ? AND Password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                admin = new Admin();
                admin.setAdminID(rs.getInt("AdminID"));
                admin.setUsername(rs.getString("Username"));
                admin.setPassword(rs.getString("Password"));
                admin.setAdminName(rs.getString("AdminName"));
                admin.setPermissionLevel(rs.getInt("PermissionLevel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, rs);
        }

        return admin;
    }

    /**
     * 修改密码
     */
    public int changePassword(int adminID, String oldPassword, String newPassword) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            // 先验证旧密码
            String checkSql = "SELECT COUNT(*) FROM Admin WHERE AdminID = ? AND Password = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setInt(1, adminID);
            pstmt.setString(2, oldPassword);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // 旧密码正确，更新密码
                String updateSql = "UPDATE Admin SET Password = ? WHERE AdminID = ?";
                return executeUpdate(updateSql, newPassword, adminID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, null);
        }

        return 0;
    }
}
