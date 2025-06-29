package com.library.view.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageDialog extends JDialog {
    public enum MessageType {
        INFO, WARNING, ERROR, SUCCESS
    }

    private JLabel lblIcon;
    private JLabel lblMessage;
    private JButton btnOK;

    public MessageDialog(Frame parent, String title, String message, MessageType type) {
        super(parent, title, true);
        initUI(message, type);
    }

    private void initUI(String message, MessageType type) {
        setLayout(new BorderLayout());
        setSize(400, 150);
        setLocationRelativeTo(getParent());
        setResizable(false);

        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 图标
        lblIcon = new JLabel();
        setIcon(type);
        mainPanel.add(lblIcon, BorderLayout.WEST);

        // 消息
        lblMessage = new JLabel(message);
        lblMessage.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        mainPanel.add(lblMessage, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        btnOK = new JButton("确定");
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(btnOK);

        add(buttonPanel, BorderLayout.SOUTH);

        // 设置默认按钮
        getRootPane().setDefaultButton(btnOK);
    }

    private void setIcon(MessageType type) {
        int iconType = JOptionPane.INFORMATION_MESSAGE;

        switch (type) {
            case INFO:
                iconType = JOptionPane.INFORMATION_MESSAGE;
                break;
            case WARNING:
                iconType = JOptionPane.WARNING_MESSAGE;
                break;
            case ERROR:
                iconType = JOptionPane.ERROR_MESSAGE;
                break;
            case SUCCESS:
                iconType = JOptionPane.INFORMATION_MESSAGE;
                break;
        }

        Icon icon = UIManager.getIcon("OptionPane.informationIcon");
        if (type == MessageType.WARNING) {
            icon = UIManager.getIcon("OptionPane.warningIcon");
        } else if (type == MessageType.ERROR) {
            icon = UIManager.getIcon("OptionPane.errorIcon");
        }

        lblIcon.setIcon(icon);
    }

    /**
     * 显示信息对话框
     */
    public static void showInfo(Frame parent, String message) {
        MessageDialog dialog = new MessageDialog(parent, "信息", message, MessageType.INFO);
        dialog.setVisible(true);
    }

    /**
     * 显示警告对话框
     */
    public static void showWarning(Frame parent, String message) {
        MessageDialog dialog = new MessageDialog(parent, "警告", message, MessageType.WARNING);
        dialog.setVisible(true);
    }

    /**
     * 显示错误对话框
     */
    public static void showError(Frame parent, String message) {
        MessageDialog dialog = new MessageDialog(parent, "错误", message, MessageType.ERROR);
        dialog.setVisible(true);
    }

    /**
     * 显示成功对话框
     */
    public static void showSuccess(Frame parent, String message) {
        MessageDialog dialog = new MessageDialog(parent, "成功", message, MessageType.SUCCESS);
        dialog.setVisible(true);
    }
}