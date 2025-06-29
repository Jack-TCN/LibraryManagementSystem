package com.library.service;

import com.library.dao.StatisticsDao;
import java.util.List;
import java.util.Map;

public class StatisticsService {
    private StatisticsDao statisticsDao = new StatisticsDao();

    /**
     * 获取图书类型统计
     */
    public List<Map<String, Object>> getBookTypeStatistics() {
        return statisticsDao.getBookTypeStatistics();
    }

    /**
     * 获取读者类型统计
     */
    public List<Map<String, Object>> getReaderTypeStatistics() {
        return statisticsDao.getReaderTypeStatistics();
    }

    /**
     * 获取月度借阅统计
     */
    public List<Map<String, Object>> getMonthlyBorrowStatistics() {
        return statisticsDao.getMonthlyBorrowStatistics();
    }

    /**
     * 获取热门图书排行
     */
    public List<Map<String, Object>> getTopBooks(int topN) {
        if (topN <= 0) {
            topN = 10;
        }
        return statisticsDao.getTopBooks(topN);
    }

    /**
     * 获取综合统计信息
     */
    public Map<String, Object> getSummaryStatistics() {
        // 这里可以返回一些综合统计信息，如总图书数、总读者数、当前借阅数等
        // 为简化起见，此处暂不实现
        return null;
    }
}