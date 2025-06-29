package com.library.view.panel;

import com.library.entity.Book;
import com.library.service.BookService;
import com.library.util.Constants;
import com.library.util.ValidationUtil;
import com.library.view.dialog.BookDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

public class BookPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbSearchType;
    private BookService bookService = new BookService();

    public BookPanel() {
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 搜索面板
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("搜索类型："));
        cmbSearchType = new JComboBox<>(new String[]{"书名", "作者"});
        searchPanel.add(cmbSearchType);
        searchPanel.add(new JLabel("关键字："));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        JButton btnSearch = new JButton("搜索");
        JButton btnShowAll = new JButton("显示全部");
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);

        add(searchPanel, BorderLayout.NORTH);

        // 表格
        tableModel = new DefaultTableModel(Constants.BOOK_COLUMN_NAMES, 0) {
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
        btnAdd.addActionListener(e -> addBook());
        btnEdit.addActionListener(e -> editBook());
        btnDelete.addActionListener(e -> deleteBook());
        btnRefresh.addActionListener(e -> loadData());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Book> books = bookService.getAllBooks();
        for (Book book : books) {
            Object[] row = {
                    book.getBookID(),
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublisherName(),
                    book.getBookTypeName(),
                    book.getPrice(),
                    book.getStock(),
                    book.getTotalStock()
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
        List<Book> books;

        if (cmbSearchType.getSelectedIndex() == 0) {
            books = bookService.searchByTitle(keyword);
        } else {
            books = bookService.searchByAuthor(keyword);
        }

        for (Book book : books) {
            Object[] row = {
                    book.getBookID(),
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublisherName(),
                    book.getBookTypeName(),
                    book.getPrice(),
                    book.getStock(),
                    book.getTotalStock()
            };
            tableModel.addRow(row);
        }
    }

    private void addBook() {
        // 简单的输入对话框，实际应用中应该创建专门的对话框
        JTextField txtISBN = new JTextField();
        JTextField txtTitle = new JTextField();
        JTextField txtAuthor = new JTextField();
        JTextField txtPrice = new JTextField();
        JTextField txtStock = new JTextField();

        Object[] message = {
                "ISBN:", txtISBN,
                "书名:", txtTitle,
                "作者:", txtAuthor,
                "价格:", txtPrice,
                "库存:", txtStock
        };

        int option = JOptionPane.showConfirmDialog(this, message, "添加图书",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Book book = new Book();
                book.setIsbn(txtISBN.getText());
                book.setTitle(txtTitle.getText());
                book.setAuthor(txtAuthor.getText());
                book.setPrice(new BigDecimal(txtPrice.getText()));
                book.setStock(Integer.parseInt(txtStock.getText()));
                book.setTotalStock(book.getStock());
                book.setPublisherID(1); // 简化处理
                book.setBookTypeID(1);  // 简化处理

                if (bookService.addBook(book)) {
                    JOptionPane.showMessageDialog(this, "添加成功！");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "添加失败！", "错误",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "输入格式错误！", "错误",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editBook() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的图书！");
            return;
        }

        // 获取选中的图书信息
        int bookID = (int) tableModel.getValueAt(selectedRow, 0);
        Book book = bookService.getBookById(bookID);

        if (book != null) {
            BookDialog dialog = new BookDialog((Frame) SwingUtilities.getWindowAncestor(this),
                    "编辑图书", book);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                if (bookService.updateBook(dialog.getBook())) {
                    JOptionPane.showMessageDialog(this, "更新成功！");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "更新失败！", "错误",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteBook() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的图书！");
            return;
        }

        int bookID = (int) tableModel.getValueAt(selectedRow, 0);
        String bookTitle = (String) tableModel.getValueAt(selectedRow, 2);

        int option = JOptionPane.showConfirmDialog(this,
                "确定要删除《" + bookTitle + "》吗？", "确认删除",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            String result = bookService.deleteBook(bookID);
            if (result.contains("成功")) {
                JOptionPane.showMessageDialog(this, result);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, result, "错误",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
