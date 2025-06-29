package com.library.view.panel;

import com.library.entity.Book;
import com.library.entity.Reader;
import com.library.service.BookService;
import com.library.service.ReaderService;
import com.library.service.SaleService;
import com.library.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;

public class SalePanel extends JPanel {
    private JTextField txtReaderID;
    private JTextField txtBookID;
    private JTextField txtQuantity;
    private JTextArea txtInfo;
    private JButton btnCheckReader;
    private JButton btnCheckBook;
    private JButton btnSell;
    private JButton btnClear;

    private ReaderService readerService = new ReaderService();
    private BookService bookService = new BookService();
    private SaleService saleService = new SaleService();

    private Reader currentReader;
    private Book currentBook;

    public SalePanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("客户ID："), gbc);
        gbc.gridx = 1;
        txtReaderID = new JTextField(10);
        inputPanel.add(txtReaderID, gbc);
        gbc.gridx = 2;
        btnCheckReader = new JButton("查询客户");
        inputPanel.add(btnCheckReader, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("图书ID："), gbc);
        gbc.gridx = 1;
        txtBookID = new JTextField(10);
        inputPanel.add(txtBookID, gbc);
        gbc.gridx = 2;
        btnCheckBook = new JButton("查询图书");
        inputPanel.add(btnCheckBook, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("数量："), gbc);
        gbc.gridx = 1;
        txtQuantity = new JTextField("1",10);
        inputPanel.add(txtQuantity, gbc);

        JPanel buttonPanel = new JPanel();
        btnSell = new JButton("购买");
        btnClear = new JButton("清空");
        buttonPanel.add(btnSell);
        buttonPanel.add(btnClear);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.NORTH);

        txtInfo = new JTextArea();
        txtInfo.setEditable(false);
        txtInfo.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane sp = new JScrollPane(txtInfo);
        sp.setBorder(BorderFactory.createTitledBorder("销售信息"));
        add(sp, BorderLayout.CENTER);

        btnCheckReader.addActionListener(e -> checkReader());
        btnCheckBook.addActionListener(e -> checkBook());
        btnSell.addActionListener(e -> sellBook());
        btnClear.addActionListener(e -> clearForm());

        btnSell.setEnabled(false);
    }

    private void checkReader() {
        String idStr = txtReaderID.getText().trim();
        if (!ValidationUtil.isNumber(idStr)) {
            JOptionPane.showMessageDialog(this, "请输入有效的客户ID！");
            return;
        }
        int id = Integer.parseInt(idStr);
        currentReader = readerService.getReaderById(id);
        if (currentReader == null) {
            JOptionPane.showMessageDialog(this, "客户不存在！");
        }
        updateInfo();
    }

    private void checkBook() {
        String idStr = txtBookID.getText().trim();
        if (!ValidationUtil.isNumber(idStr)) {
            JOptionPane.showMessageDialog(this, "请输入有效的图书ID！");
            return;
        }
        int id = Integer.parseInt(idStr);
        currentBook = bookService.getBookById(id);
        if (currentBook == null) {
            JOptionPane.showMessageDialog(this, "图书不存在！");
        }
        updateInfo();
    }

    private void updateInfo() {
        StringBuilder sb = new StringBuilder();
        if (currentReader != null) {
            sb.append("========== 客户信息 ==========")
              .append("\nID：").append(currentReader.getReaderID())
              .append("\n姓名：").append(currentReader.getReaderName())
              .append("\n\n");
        }
        if (currentBook != null) {
            sb.append("========== 图书信息 ==========")
              .append("\nID：").append(currentBook.getBookID())
              .append("\n书名：").append(currentBook.getTitle())
              .append("\n作者：").append(currentBook.getAuthor())
              .append("\n库存：").append(currentBook.getStock())
              .append("\n");
        }
        txtInfo.setText(sb.toString());
        btnSell.setEnabled(currentReader != null && currentBook != null);
    }

    private void sellBook() {
        if (currentReader == null || currentBook == null) {
            JOptionPane.showMessageDialog(this, "请先选择客户和图书！");
            return;
        }
        String qtyStr = txtQuantity.getText().trim();
        if (!ValidationUtil.isNumber(qtyStr)) {
            JOptionPane.showMessageDialog(this, "请输入有效的数量！");
            return;
        }
        int qty = Integer.parseInt(qtyStr);
        String result = saleService.sellBook(currentReader.getReaderID(), currentBook.getBookID(), qty);
        JOptionPane.showMessageDialog(this, result);
        clearForm();
    }

    private void clearForm() {
        txtReaderID.setText("");
        txtBookID.setText("");
        txtQuantity.setText("1");
        txtInfo.setText("");
        currentReader = null;
        currentBook = null;
        btnSell.setEnabled(false);
    }
}
