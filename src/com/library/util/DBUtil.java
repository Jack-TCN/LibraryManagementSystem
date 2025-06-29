// DBUtil.java - 数据库连接工具类（修正版）
// 保存到：src/com/library/util/DBUtil.java

package com.library.util;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

public class DBUtil {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    // 静态代码块，加载数据库配置
    static {
        try {
            Properties prop = new Properties();
            InputStream is = DBUtil.class.getClassLoader()
                    .getResourceAsStream("db.properties");

            if (is == null) {
                // 如果配置文件不存在，使用默认配置
                System.out.println("配置文件未找到，使用默认配置");
                driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                url = "jdbc:sqlserver://localhost:1433;DatabaseName=LibraryDB;encrypt=false;trustServerCertificate=true";
                username = "root";
                password = "584249";
            } else {
                prop.load(is);
                driver = prop.getProperty("db.driver");
                url = prop.getProperty("db.url");
                username = prop.getProperty("db.username");
                password = prop.getProperty("db.password");
                is.close();
            }

            // 加载驱动
            Class.forName(driver);
            System.out.println("数据库驱动加载成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据库配置加载失败：" + e.getMessage());
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("数据库连接成功");
            return conn;
        } catch (SQLException e) {
            System.out.println("数据库连接失败：" + e.getMessage());
            throw e;
        }
    }

    /**
     * 关闭数据库资源
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试数据库连接
     */
    public static void testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("✅ 数据库连接测试成功！");
                System.out.println("数据库URL: " + url);
                System.out.println("用户名: " + username);
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("❌ 数据库连接测试失败！");
            e.printStackTrace();
        }
    }

    /**
     * 主方法，用于测试连接
     */
    public static void main(String[] args) {
        testConnection();
    }
}