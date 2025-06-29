package com.library.view;

import com.library.entity.Admin;
import com.library.service.LoginService;
import com.library.util.Constants;
import com.library.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    private LoginService loginService = new LoginService();

    public LoginFrame() {
        initUI();
    }

    private void initUI() {
        setTitle(Constants.SYSTEM_TITLE + " - 登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        // 主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // 标题
        JLabel lblTitle = new JLabel(Constants.SYSTEM_TITLE);
        lblTitle.setFont(new Font("宋体", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        // 用户名
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("用户名："), gbc);

        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(txtUsername, gbc);

        // 密码
        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("密码："), gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        mainPanel.add(txtPassword, gbc);

        // 按钮面板
        JPanel btnPanel = new JPanel();
        btnLogin = new JButton("登录");
        btnCancel = new JButton("取消");
        btnPanel.add(btnLogin);
        btnPanel.add(btnCancel);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(btnPanel, gbc);

        // 添加事件监听
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // 回车登录
        getRootPane().setDefaultButton(btnLogin);

        add(mainPanel);
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (ValidationUtil.isEmpty(username) || ValidationUtil.isEmpty(password)) {
            JOptionPane.showMessageDialog(this, "请输入用户名和密码！", "提示",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Admin admin = loginService.adminLogin(username, password);
        if (admin != null) {
            JOptionPane.showMessageDialog(this, Constants.MSG_LOGIN_SUCCESS, "提示",
                    JOptionPane.INFORMATION_MESSAGE);

            // 打开主窗口
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new MainFrame(admin).setVisible(true);
                }
            });

            // 关闭登录窗口
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, Constants.MSG_LOGIN_FAILED, "错误",
                    JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }
}