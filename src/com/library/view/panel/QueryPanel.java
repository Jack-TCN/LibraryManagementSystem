package com.library.view.panel;

import com.library.entity.BorrowRecord;
import com.library.service.BorrowService;
import com.library.util.DateUtil;
import com.library.util.ValidationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QueryPanel extends JPanel {
    private JComboBox<String> cmbQueryType;
    private JTextField txtKeyword;
    private JButton btnQuery;
    private JTable table;
    private DefaultTableModel tableModel;

    private BorrowService borrowService = new BorrowService();

    public QueryPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 查询条件面板
        JPanel queryPanel = new JPanel();
        queryPanel.add(new JLabel("查询类型："));
        cmbQueryType = new JComboBox<>(new String[]{
                "所有借阅记录", "当前借阅", "逾期未还", "按读者ID查询"
        });
        queryPanel.add(cmbQueryType);

        queryPanel.add(new JLabel("关键字："));
        txtKeyword = new JTextField(15);
        queryPanel.add(txtKeyword);

        btnQuery = new JButton("查询");
        queryPanel.add(btnQuery);

        add(queryPanel, BorderLayout.NORTH);

        // 结果表格
        String[] columnNames = {
                "记录ID", "读者姓名", "图书名称", "借阅日期",
                "应还日期", "归还日期", "状态", "逾期天数", "罚款"
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

        // 事件监听
        btnQuery.addActionListener(e -> query());

        cmbQueryType.addActionListener(e -> {
            int index = cmbQueryType.getSelectedIndex();
            txtKeyword.setEnabled(index == 3); // 只有按读者ID查询时才需要输入
        });

        // 初始状态
        txtKeyword.setEnabled(false);
    }

    private void query() {
        tableModel.setRowCount(0);
        List<BorrowRecord> records = null;

        int queryType = cmbQueryType.getSelectedIndex();
        switch (queryType) {
            case 0: // 所有借阅记录
                records = borrowService.getAllBorrowRecords();
                break;
            case 1: // 当前借阅
                records = borrowService.getCurrentBorrowRecords();
                break;
            case 2: // 逾期未还
                records = borrowService.getOverdueBorrowRecords();
                break;
            case 3: // 按读者ID查询
                String keyword = txtKeyword.getText().trim();
                if (!ValidationUtil.isNumber(keyword)) {
                    JOptionPane.showMessageDialog(this, "请输入有效的读者ID！");
                    return;
                }
                int readerID = Integer.parseInt(keyword);
                records = borrowService.getBorrowRecordsByReader(readerID);
                break;
        }

        if (records != null) {
            for (BorrowRecord record : records) {
                Object[] row = {
                        record.getRecordID(),
                        record.getReaderName(),
                        record.getBookTitle(),
                        DateUtil.dateToString(record.getBorrowDate()),
                        DateUtil.dateToString(record.getDueDate()),
                        DateUtil.dateToString(record.getReturnDate()),
                        record.getStatus(),
                        record.getOverdueDays() > 0 ? record.getOverdueDays() : "",
                        record.getFine()
                };
                tableModel.addRow(row);
            }
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "没有找到符合条件的记录！");
        }
    }
}
