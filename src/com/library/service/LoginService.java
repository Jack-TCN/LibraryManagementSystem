package com.library.service;

import com.library.dao.AdminDao;
import com.library.dao.ReaderDao;
import com.library.entity.Admin;
import com.library.entity.Reader;

public class LoginService {
    private AdminDao adminDao = new AdminDao();
    private ReaderDao readerDao = new ReaderDao();

    /**
     * 管理员登录
     */
    public Admin adminLogin(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        if (password == null || password.isEmpty()) {
            return null;
        }

        return adminDao.login(username.trim(), password);
    }

    /**
     * 读者登录
     */
    public Reader readerLogin(int readerID, String password) {
        if (readerID <= 0) {
            return null;
        }
        if (password == null || password.isEmpty()) {
            return null;
        }

        return readerDao.login(readerID, password);
    }

    /**
     * 修改管理员密码
     */
    public boolean changeAdminPassword(int adminID, String oldPassword, String newPassword) {
        if (adminID <= 0) {
            return false;
        }
        if (oldPassword == null || oldPassword.isEmpty()) {
            return false;
        }
        if (newPassword == null || newPassword.length() < 6) {
            return false;
        }

        return adminDao.changePassword(adminID, oldPassword, newPassword) > 0;
    }
}