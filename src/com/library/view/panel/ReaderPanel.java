package com.library.view.panel;

import com.library.entity.Reader;
import com.library.service.ReaderService;
import com.library.util.Constants;
import com.library.util.ValidationUtil;
import com.library.view.dialog.ReaderDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReaderPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private ReaderService readerService = new ReaderService();

    public ReaderPanel() {
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 搜索面板
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("读者姓名："));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        JButton btnSearch = new JButton("搜索");
        JButton btnShowAll = new JButton("显示全部");
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);

        add(searchPanel, BorderLayout.NORTH);

        // 表格
        tableModel = new DefaultTableModel(Constants.READER_COLUMN_NAMES, 0) {
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
        JButton btnAdd = new JButton("添加");
        JButton btnEdit = new JButton("编辑");
        JButton btnDelete = new JButton("删除");
        JButton btnRefresh = new JButton("刷新");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        add(buttonPanel, BorderLayout.SOUTH);

        // 事件监听
        btnSearch.addActionListener(e -> search());
        btnShowAll.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> addReader());
        btnEdit.addActionListener(e -> editReader());
        btnDelete.addActionListener(e -> deleteReader());
        btnRefresh.addActionListener(e -> loadData());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Reader> readers = readerService.getAllReaders();
        for (Reader reader : readers) {
            Object[] row = {
                    reader.getReaderID(),
                    reader.getReaderName(),
                    reader.getGender(),
                    reader.getReaderTypeName(),
                    reader.getPhone(),
                    reader.getEmail(),
                    reader.getRegDate(),
                    reader.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void search() {
        String keyword = txtSearch.getText().trim();
        if (ValidationUtil.isEmpty(keyword)) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);
        List<Reader> readers = readerService.searchByName(keyword);

        for (Reader reader : readers) {
            Object[] row = {
                    reader.getReaderID(),
                    reader.getReaderName(),
                    reader.getGender(),
                    reader.getReaderTypeName(),
                    reader.getPhone(),
                    reader.getEmail(),
                    reader.getRegDate(),
                    reader.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void addReader() {
        JTextField txtName = new JTextField();
        JComboBox<String> cmbGender = new JComboBox<>(new String[]{"男", "女"});
        JComboBox<String> cmbType = new JComboBox<>(new String[]{"学生", "教师", "职工", "校外人员"});
        JTextField txtPhone = new JTextField();
        JTextField txtEmail = new JTextField();

        Object[] message = {
                "姓名:", txtName,
                "性别:", cmbGender,
                "类型:", cmbType,
                "电话:", txtPhone,
                "邮箱:", txtEmail
        };

        int option = JOptionPane.showConfirmDialog(this, message, "添加读者",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            Reader reader = new Reader();
            reader.setReaderName(txtName.getText());
            reader.setGender((String) cmbGender.getSelectedItem());
            reader.setReaderTypeID(cmbType.getSelectedIndex() + 1);
            reader.setPhone(txtPhone.getText());
            reader.setEmail(txtEmail.getText());

            if (readerService.addReader(reader)) {
                JOptionPane.showMessageDialog(this, "添加成功！");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "添加失败！", "错误",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editReader() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的读者！");
            return;
        }

        // 获取选中的读者信息
        int readerID = (int) tableModel.getValueAt(selectedRow, 0);
        Reader reader = readerService.getReaderById(readerID);

        if (reader != null) {
            ReaderDialog dialog = new ReaderDialog((Frame) SwingUtilities.getWindowAncestor(this),
                    "编辑读者", reader);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                if (readerService.updateReader(dialog.getReader())) {
                    JOptionPane.showMessageDialog(this, "更新成功！");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "更新失败！", "错误",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteReader() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的读者！");
            return;
        }

        int readerID = (int) tableModel.getValueAt(selectedRow, 0);
        String readerName = (String) tableModel.getValueAt(selectedRow, 1);

        int option = JOptionPane.showConfirmDialog(this,
                "确定要删除读者" + readerName + "吗？", "确认删除",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            if (readerService.deleteReader(readerID)) {
                JOptionPane.showMessageDialog(this, "删除成功！");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "删除失败！可能有未归还的图书。",
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}