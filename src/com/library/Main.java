package com.library;

import com.library.view.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 设置界面风格
        try {
            // 使用系统默认外观
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 启动登录窗口
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}
