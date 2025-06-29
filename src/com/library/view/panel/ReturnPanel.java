package com.library.view.panel;

import com.library.entity.BorrowRecord;
import com.library.service.BorrowService;
import com.library.util.Constants;
import com.library.util.DateUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReturnPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnReturn;
    private JButton btnRefresh;

    private BorrowService borrowService = new BorrowService();

    public ReturnPanel() {
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 表格
        String[] columnNames = {
                "记录ID", "读者姓名", "图书名称", "借阅日期",
                "应还日期", "状态", "逾期天数"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        btnReturn = new JButton("还书");
        btnRefresh = new JButton("刷新");

        buttonPanel.add(btnReturn);
        buttonPanel.add(btnRefresh);

        add(buttonPanel, BorderLayout.SOUTH);

        // 事件监听
        btnReturn.addActionListener(e -> returnBook());
        btnRefresh.addActionListener(e -> loadData());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<BorrowRecord> records = borrowService.getCurrentBorrowRecords();

        for (BorrowRecord record : records) {
            Object[] row = {
                    record.getRecordID(),
                    record.getReaderName(),
                    record.getBookTitle(),
                    DateUtil.dateToString(record.getBorrowDate()),
                    DateUtil.dateToString(record.getDueDate()),
                    record.getStatus(),
                    record.getOverdueDays() > 0 ? record.getOverdueDays() : ""
            };
            tableModel.addRow(row);
        }
    }

    private void returnBook() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要归还的记录！");
            return;
        }

        int recordID = (int) tableModel.getValueAt(selectedRow, 0);
        String bookTitle = (String) tableModel.getValueAt(selectedRow, 2);

        int option = JOptionPane.showConfirmDialog(this,
                "确定要归还《" + bookTitle + "》吗？", "确认归还",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            String result = borrowService.returnBook(recordID);
            JOptionPane.showMessageDialog(this, result);
            loadData();
        }
    }
}