package com.library.entity;

public class Admin {
    private int adminID;
    private String username;
    private String password;
    private String adminName;
    private int permissionLevel;

    // 无参构造函数
    public Admin() {}

    // 带参构造函数
    public Admin(String username, String password, String adminName, int permissionLevel) {
        this.username = username;
        this.password = password;
        this.adminName = adminName;
        this.permissionLevel = permissionLevel;
    }

    // Getter和Setter方法
    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
}