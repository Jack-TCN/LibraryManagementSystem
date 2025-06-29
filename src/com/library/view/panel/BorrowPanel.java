package com.library.view.panel;

import com.library.entity.Book;
import com.library.entity.Reader;
import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.service.ReaderService;
import com.library.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BorrowPanel extends JPanel {
    private JTextField txtReaderID;
    private JTextField txtBookID;
    private JTextArea txtInfo;
    private JButton btnCheckReader;
    private JButton btnCheckBook;
    private JButton btnBorrow;
    private JButton btnClear;

    private ReaderService readerService = new ReaderService();
    private BookService bookService = new BookService();
    private BorrowService borrowService = new BorrowService();

    private Reader currentReader;
    private Book currentBook;

    public BorrowPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 输入面板
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 读者ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("读者ID："), gbc);

        gbc.gridx = 1;
        txtReaderID = new JTextField(15);
        inputPanel.add(txtReaderID, gbc);

        gbc.gridx = 2;
        btnCheckReader = new JButton("查询读者");
        inputPanel.add(btnCheckReader, gbc);

        // 图书ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("图书ID："), gbc);

        gbc.gridx = 1;
        txtBookID = new JTextField(15);
        inputPanel.add(txtBookID, gbc);

        gbc.gridx = 2;
        btnCheckBook = new JButton("查询图书");
        inputPanel.add(btnCheckBook, gbc);

        // 按钮
        JPanel buttonPanel = new JPanel();
        btnBorrow = new JButton("借书");
        btnClear = new JButton("清空");
        buttonPanel.add(btnBorrow);
        buttonPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // 信息显示区
        txtInfo = new JTextArea();
        txtInfo.setEditable(false);
        txtInfo.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(txtInfo);
        scrollPane.setBorder(BorderFactory.createTitledBorder("借阅信息"));
        add(scrollPane, BorderLayout.CENTER);

        // 事件监听
        btnCheckReader.addActionListener(e -> checkReader());
        btnCheckBook.addActionListener(e -> checkBook());
        btnBorrow.addActionListener(e -> borrowBook());
        btnClear.addActionListener(e -> clearForm());

        // 初始状态
        btnBorrow.setEnabled(false);
    }

    private void checkReader() {
        String readerIDStr = txtReaderID.getText().trim();
        if (!ValidationUtil.isNumber(readerIDStr)) {
            JOptionPane.showMessageDialog(this, "请输入有效的读者ID！");
            return;
        }

        int readerID = Integer.parseInt(readerIDStr);
        Reader reader = readerService.getReaderById(readerID);

        if (reader == null) {
            JOptionPane.showMessageDialog(this, "读者不存在！");
            currentReader = null;
            updateInfo();
            return;
        }

        currentReader = reader;
        updateInfo();

        // 检查是否可以借书
        if (!borrowService.canBorrow(readerID)) {
            JOptionPane.showMessageDialog(this,
                    "该读者不能借书！可能有逾期未还图书或已达借阅上限。",
                    "提示", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void checkBook() {
        String bookIDStr = txtBookID.getText().trim();
        if (!ValidationUtil.isNumber(bookIDStr)) {
            JOptionPane.showMessageDialog(this, "请输入有效的图书ID！");
            return;
        }

        int bookID = Integer.parseInt(bookIDStr);
        Book book = bookService.getBookById(bookID);

        if (book == null) {
            JOptionPane.showMessageDialog(this, "图书不存在！");
            currentBook = null;
            updateInfo();
            return;
        }

        currentBook = book;
        updateInfo();

        if (book.getStock() <= 0) {
            JOptionPane.showMessageDialog(this, "该图书库存不足！",
                    "提示", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateInfo() {
        StringBuilder info = new StringBuilder();

        if (currentReader != null) {
            info.append("========== 读者信息 ==========\n");
            info.append("读者ID：").append(currentReader.getReaderID()).append("\n");
            info.append("姓名：").append(currentReader.getReaderName()).append("\n");
            info.append("类型：").append(currentReader.getReaderTypeName()).append("\n");
            info.append("状态：").append(currentReader.getStatus()).append("\n");
            info.append("可借天数：").append(currentReader.getBorrowDays()).append("\n");
            info.append("可借数量：").append(currentReader.getBorrowCount()).append("\n\n");
        }

        if (currentBook != null) {
            info.append("========== 图书信息 ==========\n");
            info.append("图书ID：").append(currentBook.getBookID()).append("\n");
            info.append("书名：").append(currentBook.getTitle()).append("\n");
            info.append("作者：").append(currentBook.getAuthor()).append("\n");
            info.append("出版社：").append(currentBook.getPublisherName()).append("\n");
            info.append("类型：").append(currentBook.getBookTypeName()).append("\n");
            info.append("库存：").append(currentBook.getStock()).append("/")
                    .append(currentBook.getTotalStock()).append("\n");
        }

        txtInfo.setText(info.toString());

        // 只有读者和图书都选择了才能借书
        btnBorrow.setEnabled(currentReader != null && currentBook != null);
    }

    private void borrowBook() {
        if (currentReader == null || currentBook == null) {
            JOptionPane.showMessageDialog(this, "请先选择读者和图书！");
            return;
        }

        String result = borrowService.borrowBook(currentReader.getReaderID(),
                currentBook.getBookID());

        if (result.contains("成功")) {
            JOptionPane.showMessageDialog(this, result, "成功",
                    JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, result, "失败",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtReaderID.setText("");
        txtBookID.setText("");
        txtInfo.setText("");
        currentReader = null;
        currentBook = null;
        btnBorrow.setEnabled(false);
    }
}