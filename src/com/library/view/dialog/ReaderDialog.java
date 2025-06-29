package com.library.view.dialog;

import com.library.entity.Reader;
import com.library.entity.ReaderType;
import com.library.util.Constants;
import com.library.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReaderDialog extends JDialog {
    private JTextField txtReaderName;
    private JComboBox<String> cmbGender;
    private JComboBox<ReaderType> cmbReaderType;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbStatus;

    private JButton btnSave;
    private JButton btnCancel;

    private Reader reader;
    private boolean confirmed = false;

    public ReaderDialog(Frame parent, String title, Reader reader) {
        super(parent, title, true);
        this.reader = reader;
        initUI();
        if (reader != null) {
            loadReaderData();
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setSize(400, 350);
        setLocationRelativeTo(getParent());

        // 主面板
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 姓名
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("姓名："), gbc);

        gbc.gridx = 1;
        txtReaderName = new JTextField(20);
        mainPanel.add(txtReaderName, gbc);

        // 性别
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("性别："), gbc);

        gbc.gridx = 1;
        cmbGender = new JComboBox<>(new String[]{Constants.GENDER_MALE, Constants.GENDER_FEMALE});
        mainPanel.add(cmbGender, gbc);

        // 读者类型
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("读者类型："), gbc);

        gbc.gridx = 1;
        cmbReaderType = new JComboBox<>();
        loadReaderTypes();
        mainPanel.add(cmbReaderType, gbc);

        // 电话
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("电话："), gbc);

        gbc.gridx = 1;
        txtPhone = new JTextField(20);
        mainPanel.add(txtPhone, gbc);

        // 邮箱
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("邮箱："), gbc);

        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        mainPanel.add(txtEmail, gbc);

        // 密码
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("密码："), gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        if (reader == null) {
            txtPassword.setText(Constants.DEFAULT_PASSWORD);
        }
        mainPanel.add(txtPassword, gbc);

        // 状态
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(new JLabel("状态："), gbc);

        gbc.gridx = 1;
        cmbStatus = new JComboBox<>(new String[]{
                Constants.STATUS_NORMAL,
                Constants.STATUS_LOST,
                Constants.STATUS_CANCELLED
        });
        mainPanel.add(cmbStatus, gbc);

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
                saveReader();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void loadReaderTypes() {
        // 简化处理，实际应从数据库加载
        cmbReaderType.addItem(new ReaderType() {{ setTypeID(1); setTypeName("学生"); setBorrowDays(30); setBorrowCount(5); }});
        cmbReaderType.addItem(new ReaderType() {{ setTypeID(2); setTypeName("教师"); setBorrowDays(60); setBorrowCount(10); }});
        cmbReaderType.addItem(new ReaderType() {{ setTypeID(3); setTypeName("职工"); setBorrowDays(45); setBorrowCount(8); }});
        cmbReaderType.addItem(new ReaderType() {{ setTypeID(4); setTypeName("校外人员"); setBorrowDays(15); setBorrowCount(3); }});
    }

    private void loadReaderData() {
        txtReaderName.setText(reader.getReaderName());
        cmbGender.setSelectedItem(reader.getGender());
        txtPhone.setText(reader.getPhone());
        txtEmail.setText(reader.getEmail());
        cmbStatus.setSelectedItem(reader.getStatus());

        // 选择对应的读者类型
        for (int i = 0; i < cmbReaderType.getItemCount(); i++) {
            if (cmbReaderType.getItemAt(i).getTypeID() == reader.getReaderTypeID()) {
                cmbReaderType.setSelectedIndex(i);
                break;
            }
        }
    }

    private void saveReader() {
        // 验证输入
        if (ValidationUtil.isEmpty(txtReaderName.getText())) {
            JOptionPane.showMessageDialog(this, "请输入姓名！", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String phone = txtPhone.getText().trim();
        if (!ValidationUtil.isEmpty(phone) && !ValidationUtil.isPhone(phone)) {
            JOptionPane.showMessageDialog(this, "电话格式不正确！", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String email = txtEmail.getText().trim();
        if (!ValidationUtil.isEmpty(email) && !ValidationUtil.isEmail(email)) {
            JOptionPane.showMessageDialog(this, "邮箱格式不正确！", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String password = new String(txtPassword.getPassword());
        if (!ValidationUtil.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "密码至少6位！", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 创建或更新读者对象
        if (reader == null) {
            reader = new Reader();
        }

        reader.setReaderName(txtReaderName.getText().trim());
        reader.setGender((String) cmbGender.getSelectedItem());
        reader.setReaderTypeID(((ReaderType) cmbReaderType.getSelectedItem()).getTypeID());
        reader.setPhone(phone);
        reader.setEmail(email);
        reader.setPassword(password);
        reader.setStatus((String) cmbStatus.getSelectedItem());

        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Reader getReader() {
        return reader;
    }
}