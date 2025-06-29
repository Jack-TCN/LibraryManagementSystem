package com.library.service;

import com.library.dao.ReaderDao;
import com.library.entity.Reader;
import java.util.List;

public class ReaderService {
    private ReaderDao readerDao = new ReaderDao();

    /**
     * 添加读者
     */
    public boolean addReader(Reader reader) {
        // 验证数据
        if (reader.getReaderName() == null || reader.getReaderName().trim().isEmpty()) {
            return false;
        }
        if (reader.getGender() == null ||
                (!reader.getGender().equals("男") && !reader.getGender().equals("女"))) {
            return false;
        }
        if (reader.getReaderTypeID() <= 0) {
            return false;
        }

        // 设置默认值
        if (reader.getPassword() == null || reader.getPassword().isEmpty()) {
            reader.setPassword("123456");
        }

        return readerDao.addReader(reader) > 0;
    }

    /**
     * 更新读者信息
     */
    public boolean updateReader(Reader reader) {
        // 验证数据
        if (reader.getReaderID() <= 0) {
            return false;
        }
        if (reader.getReaderName() == null || reader.getReaderName().trim().isEmpty()) {
            return false;
        }
        if (reader.getGender() == null ||
                (!reader.getGender().equals("男") && !reader.getGender().equals("女"))) {
            return false;
        }
        if (reader.getReaderTypeID() <= 0) {
            return false;
        }

        return readerDao.updateReader(reader) > 0;
    }

    /**
     * 删除读者
     */
    public boolean deleteReader(int readerID) {
        // 在数据库触发器中已经检查是否有未还图书
        return readerDao.deleteReader(readerID) > 0;
    }

    /**
     * 根据ID查询读者
     */
    public Reader getReaderById(int readerID) {
        return readerDao.getReaderById(readerID);
    }

    /**
     * 获取所有读者
     */
    public List<Reader> getAllReaders() {
        return readerDao.getAllReaders();
    }

    /**
     * 按姓名搜索读者
     */
    public List<Reader> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllReaders();
        }
        return readerDao.searchByName(name.trim());
    }

    /**
     * 读者登录
     */
    public Reader login(int readerID, String password) {
        if (readerID <= 0) {
            return null;
        }
        if (password == null || password.isEmpty()) {
            return null;
        }

        return readerDao.login(readerID, password);
    }
}
