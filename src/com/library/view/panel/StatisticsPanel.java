package com.library.view.panel;

import com.library.service.StatisticsService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class StatisticsPanel extends JPanel {
    private JComboBox<String> cmbStatType;
    private JButton btnStatistics;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea txtSummary;

    private StatisticsService statisticsService = new StatisticsService();

    public StatisticsPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 统计类型选择
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("统计类型："));
        cmbStatType = new JComboBox<>(new String[]{
                "图书类型统计", "读者类型统计", "月度借阅统计", "热门图书排行"
        });
        topPanel.add(cmbStatType);

        btnStatistics = new JButton("统计");
        topPanel.add(btnStatistics);

        add(topPanel, BorderLayout.NORTH);

        // 中间分割面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        // 表格
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(0, 300));
        splitPane.setTopComponent(tableScrollPane);

        // 统计摘要
        txtSummary = new JTextArea(5, 0);
        txtSummary.setEditable(false);
        JScrollPane summaryScrollPane = new JScrollPane(txtSummary);
        summaryScrollPane.setBorder(BorderFactory.createTitledBorder("统计摘要"));
        splitPane.setBottomComponent(summaryScrollPane);

        add(splitPane, BorderLayout.CENTER);

        // 事件监听
        btnStatistics.addActionListener(e -> doStatistics());
    }

    private void doStatistics() {
        int statType = cmbStatType.getSelectedIndex();

        switch (statType) {
            case 0: // 图书类型统计
                statisticsBookType();
                break;
            case 1: // 读者类型统计
                statisticsReaderType();
                break;
            case 2: // 月度借阅统计
                statisticsMonthly();
                break;
            case 3: // 热门图书排行
                statisticsTopBooks();
                break;
        }
    }

    private void statisticsBookType() {
        List<Map<String, Object>> data = statisticsService.getBookTypeStatistics();

        String[] columnNames = {"图书类型", "图书种类", "总库存"};
        tableModel.setDataVector(null, columnNames);

        int totalBooks = 0;
        int totalStock = 0;

        for (Map<String, Object> row : data) {
            Object[] rowData = {
                    row.get("TypeName"),
                    row.get("BookCount"),
                    row.get("TotalStock")
            };
            tableModel.addRow(rowData);

            totalBooks += (int) row.get("BookCount");
            totalStock += (int) row.get("TotalStock");
        }

        txtSummary.setText("统计摘要：\n" +
                "图书类型总数：" + data.size() + "\n" +
                "图书种类总数：" + totalBooks + "\n" +
                "图书总库存：" + totalStock);
    }

    private void statisticsReaderType() {
        List<Map<String, Object>> data = statisticsService.getReaderTypeStatistics();

        String[] columnNames = {"读者类型", "读者人数", "总借阅次数", "当前借阅数"};
        tableModel.setDataVector(null, columnNames);

        int totalReaders = 0;
        int totalBorrows = 0;

        for (Map<String, Object> row : data) {
            Object[] rowData = {
                    row.get("TypeName"),
                    row.get("ReaderCount"),
                    row.get("TotalBorrow"),
                    row.get("CurrentBorrow")
            };
            tableModel.addRow(rowData);

            totalReaders += (int) row.get("ReaderCount");
            totalBorrows += (int) row.get("TotalBorrow");
        }

        txtSummary.setText("统计摘要：\n" +
                "读者类型总数：" + data.size() + "\n" +
                "读者总人数：" + totalReaders + "\n" +
                "总借阅次数：" + totalBorrows);
    }

    private void statisticsMonthly() {
        List<Map<String, Object>> data = statisticsService.getMonthlyBorrowStatistics();

        String[] columnNames = {"年份", "月份", "借阅数量"};
        tableModel.setDataVector(null, columnNames);

        int totalBorrows = 0;

        for (Map<String, Object> row : data) {
            Object[] rowData = {
                    row.get("Year"),
                    row.get("Month"),
                    row.get("BorrowCount")
            };
            tableModel.addRow(rowData);

            totalBorrows += (int) row.get("BorrowCount");
        }

        txtSummary.setText("统计摘要：\n" +
                "统计月份数：" + data.size() + "\n" +
                "总借阅数量：" + totalBorrows + "\n" +
                "月均借阅量：" + (data.size() > 0 ? totalBorrows / data.size() : 0));
    }

    private void statisticsTopBooks() {
        List<Map<String, Object>> data = statisticsService.getTopBooks(10);

        String[] columnNames = {"排名", "图书ID", "书名", "作者", "借阅次数"};
        tableModel.setDataVector(null, columnNames);

        int rank = 1;
        for (Map<String, Object> row : data) {
            Object[] rowData = {
                    rank++,
                    row.get("BookID"),
                    row.get("Title"),
                    row.get("Author"),
                    row.get("BorrowTimes")
            };
            tableModel.addRow(rowData);
        }

        txtSummary.setText("统计摘要：\n" +
                "显示前10名最受欢迎的图书\n" +
                "这些图书的借阅次数反映了读者的阅读偏好");
    }
}