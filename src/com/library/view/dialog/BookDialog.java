package com.library.view.dialog;

import com.library.entity.Book;
import com.library.entity.BookType;
import com.library.entity.Publisher;
import com.library.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDialog extends JDialog {
    private JTextField txtISBN;
    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JComboBox<Publisher> cmbPublisher;
    private JComboBox<BookType> cmbBookType;
    private JTextField txtPrice;
    private JTextField txtStock;
    private JTextField txtTotalStock;

    private JButton btnSave;
    private JButton btnCancel;

    private Book book;
    private boolean confirmed = false;

    public BookDialog(Frame parent, String title, Book book) {
        super(parent, title, true);
        this.book = book;
        initUI();
        if (book != null) {
            loadBookData();
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        // 主面板
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ISBN
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("ISBN："), gbc);

        gbc.gridx = 1;
        txtISBN = new JTextField(20);
        mainPanel.add(txtISBN, gbc);

        // 书名
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("书名："), gbc);

        gbc.gridx = 1;
        txtTitle = new JTextField(20);
        mainPanel.add(txtTitle, gbc);

        // 作者
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("作者："), gbc);

        gbc.gridx = 1;
        txtAuthor = new JTextField(20);
        mainPanel.add(txtAuthor, gbc);

        // 出版社
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("出版社："), gbc);

        gbc.gridx = 1;
        cmbPublisher = new JComboBox<>();
        loadPublishers();
        mainPanel.add(cmbPublisher, gbc);

        // 图书类型
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("图书类型："), gbc);

        gbc.gridx = 1;
        cmbBookType = new JComboBox<>();
        loadBookTypes();
        mainPanel.add(cmbBookType, gbc);

        // 价格
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("价格："), gbc);

        gbc.gridx = 1;
        txtPrice = new JTextField(20);
        mainPanel.add(txtPrice, gbc);

        // 当前库存
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(new JLabel("当前库存："), gbc);

        gbc.gridx = 1;
        txtStock = new JTextField(20);
        mainPanel.add(txtStock, gbc);

        // 总库存
        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(new JLabel("总库存："), gbc);

        gbc.gridx = 1;
        txtTotalStock = new JTextField(20);
        mainPanel.add(txtTotalStock, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        btnSave = new JButton("保存");
        btnCancel = new JButton("取消");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);

        // 事件监听
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBook();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void loadPublishers() {
        // 简化处理，实际应从数据库加载
        cmbPublisher.addItem(new Publisher() {{ setPublisherID(1); setPublisherName("清华大学出版社"); }});
        cmbPublisher.addItem(new Publisher() {{ setPublisherID(2); setPublisherName("人民邮电出版社"); }});
        cmbPublisher.addItem(new Publisher() {{ setPublisherID(3); setPublisherName("机械工业出版社"); }});
        cmbPublisher.addItem(new Publisher() {{ setPublisherID(4); setPublisherName("电子工业出版社"); }});
        cmbPublisher.addItem(new Publisher() {{ setPublisherID(5); setPublisherName("高等教育出版社"); }});
    }

    private void loadBookTypes() {
        // 简化处理，实际应从数据库加载
        cmbBookType.addItem(new BookType() {{ setTypeID(1); setTypeName("计算机科学"); }});
        cmbBookType.addItem(new BookType() {{ setTypeID(2); setTypeName("文学艺术"); }});
        cmbBookType.addItem(new BookType() {{ setTypeID(3); setTypeName("经济管理"); }});
        cmbBookType.addItem(new BookType() {{ setTypeID(4); setTypeName("自然科学"); }});
        cmbBookType.addItem(new BookType() {{ setTypeID(5); setTypeName("社会科学"); }});
    }

    private void loadBookData() {
        txtISBN.setText(book.getIsbn());
        txtTitle.setText(book.getTitle());
        txtAuthor.setText(book.getAuthor());
        txtPrice.setText(book.getPrice().toString());
        txtStock.setText(String.valueOf(book.getStock()));
        txtTotalStock.setText(String.valueOf(book.getTotalStock()));

        // 选择对应的出版社和类型
        for (int i = 0; i < cmbPublisher.getItemCount(); i++) {
            if (cmbPublisher.getItemAt(i).getPublisherID() == book.getPublisherID()) {
                cmbPublisher.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < cmbBookType.getItemCount(); i++) {
            if (cmbBookType.getItemAt(i).getTypeID() == book.getBookTypeID()) {
                cmbBookType.setSelectedIndex(i);
                break;
            }
        }
    }

    private void saveBook() {
        // 验证输入
        if (ValidationUtil.isEmpty(txtISBN.getText()) ||
                ValidationUtil.isEmpty(txtTitle.getText()) ||
                ValidationUtil.isEmpty(txtAuthor.getText())) {
            JOptionPane.showMessageDialog(this, "请填写必填字段！", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ValidationUtil.isDecimal(txtPrice.getText())) {
            JOptionPane.showMessageDialog(this, "价格格式不正确！", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ValidationUtil.isNumber(txtStock.getText()) ||
                !ValidationUtil.isNumber(txtTotalStock.getText())) {
            JOptionPane.showMessageDialog(this, "库存必须是数字！", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 创建或更新图书对象
        if (book == null) {
            book = new Book();
        }

        book.setIsbn(txtISBN.getText().trim());
        book.setTitle(txtTitle.getText().trim());
        book.setAuthor(txtAuthor.getText().trim());
        book.setPublisherID(((Publisher) cmbPublisher.getSelectedItem()).getPublisherID());
        book.setBookTypeID(((BookType) cmbBookType.getSelectedItem()).getTypeID());
        book.setPrice(new BigDecimal(txtPrice.getText()));
        book.setStock(Integer.parseInt(txtStock.getText()));
        book.setTotalStock(Integer.parseInt(txtTotalStock.getText()));

        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Book getBook() {
        return book;
    }
}
