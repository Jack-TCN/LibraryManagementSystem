package com.library.view;

import com.library.entity.Admin;
import com.library.util.Constants;
import com.library.view.panel.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private Admin currentAdmin;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // 各功能面板
    private BookPanel bookPanel;
    private ReaderPanel readerPanel;
    private BorrowPanel borrowPanel;
    private ReturnPanel returnPanel;
    private SalePanel salePanel;
    private QueryPanel queryPanel;
    private StatisticsPanel statisticsPanel;

    public MainFrame(Admin admin) {
        this.currentAdmin = admin;
        initUI();
    }

    private void initUI() {
        setTitle(Constants.SYSTEM_TITLE + " - " + currentAdmin.getAdminName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // 菜单栏
        createMenuBar();

        // 工具栏
        add(createToolBar(), BorderLayout.NORTH);

        // 内容面板
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // 初始化各功能面板
        bookPanel = new BookPanel();
        readerPanel = new ReaderPanel();
        borrowPanel = new BorrowPanel();
        returnPanel = new ReturnPanel();
        salePanel = new SalePanel();
        queryPanel = new QueryPanel();
        statisticsPanel = new StatisticsPanel();

        // 添加到卡片布局
        contentPanel.add(bookPanel, "book");
        contentPanel.add(readerPanel, "reader");
        contentPanel.add(borrowPanel, "borrow");
        contentPanel.add(returnPanel, "return");
        contentPanel.add(salePanel, "sale");
        contentPanel.add(queryPanel, "query");
        contentPanel.add(statisticsPanel, "statistics");

        add(contentPanel, BorderLayout.CENTER);

        // 状态栏
        add(createStatusBar(), BorderLayout.SOUTH);

        // 默认显示图书管理
        cardLayout.show(contentPanel, "book");
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // 系统菜单
        JMenu systemMenu = new JMenu("系统");
        JMenuItem changePasswordItem = new JMenuItem("修改密码");
        JMenuItem exitItem = new JMenuItem("退出");

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
@@ -86,96 +89,105 @@ public class MainFrame extends JFrame {
        systemMenu.add(changePasswordItem);
        systemMenu.addSeparator();
        systemMenu.add(exitItem);

        // 图书管理菜单
        JMenu bookMenu = new JMenu("图书管理");
        JMenuItem bookManageItem = new JMenuItem("图书信息管理");
        bookManageItem.addActionListener(e -> cardLayout.show(contentPanel, "book"));
        bookMenu.add(bookManageItem);

        // 读者管理菜单
        JMenu readerMenu = new JMenu("读者管理");
        JMenuItem readerManageItem = new JMenuItem("读者信息管理");
        readerManageItem.addActionListener(e -> cardLayout.show(contentPanel, "reader"));
        readerMenu.add(readerManageItem);

        // 借阅管理菜单
        JMenu borrowMenu = new JMenu("借阅管理");
        JMenuItem borrowItem = new JMenuItem("图书借阅");
        JMenuItem returnItem = new JMenuItem("图书归还");
        borrowItem.addActionListener(e -> cardLayout.show(contentPanel, "borrow"));
        returnItem.addActionListener(e -> cardLayout.show(contentPanel, "return"));
        borrowMenu.add(borrowItem);
        borrowMenu.add(returnItem);

        JMenu saleMenu = new JMenu("销售管理");
        JMenuItem saleItem = new JMenuItem("图书销售");
        saleItem.addActionListener(e -> cardLayout.show(contentPanel, "sale"));
        saleMenu.add(saleItem);

        // 查询统计菜单
        JMenu queryMenu = new JMenu("查询统计");
        JMenuItem queryItem = new JMenuItem("综合查询");
        JMenuItem statisticsItem = new JMenuItem("统计分析");
        queryItem.addActionListener(e -> cardLayout.show(contentPanel, "query"));
        statisticsItem.addActionListener(e -> cardLayout.show(contentPanel, "statistics"));
        queryMenu.add(queryItem);
        queryMenu.add(statisticsItem);

        // 帮助菜单
        JMenu helpMenu = new JMenu("帮助");
        JMenuItem aboutItem = new JMenuItem("关于");
        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    Constants.SYSTEM_TITLE + "\n版本：" + Constants.VERSION +
                            "\n\n数据库原理课程设计作品",
                    "关于", JOptionPane.INFORMATION_MESSAGE);
        });
        helpMenu.add(aboutItem);

        menuBar.add(systemMenu);
        menuBar.add(bookMenu);
        menuBar.add(readerMenu);
        menuBar.add(borrowMenu);
        menuBar.add(saleMenu);
        menuBar.add(queryMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton btnBook = new JButton("图书管理");
        JButton btnReader = new JButton("读者管理");
        JButton btnBorrow = new JButton("借书");
        JButton btnReturn = new JButton("还书");
        JButton btnSale = new JButton("销售");
        JButton btnQuery = new JButton("查询");
        JButton btnStatistics = new JButton("统计");

        btnBook.addActionListener(e -> cardLayout.show(contentPanel, "book"));
        btnReader.addActionListener(e -> cardLayout.show(contentPanel, "reader"));
        btnBorrow.addActionListener(e -> cardLayout.show(contentPanel, "borrow"));
        btnReturn.addActionListener(e -> cardLayout.show(contentPanel, "return"));
        btnSale.addActionListener(e -> cardLayout.show(contentPanel, "sale"));
        btnQuery.addActionListener(e -> cardLayout.show(contentPanel, "query"));
        btnStatistics.addActionListener(e -> cardLayout.show(contentPanel, "statistics"));

        toolBar.add(btnBook);
        toolBar.add(btnReader);
        toolBar.addSeparator();
        toolBar.add(btnBorrow);
        toolBar.add(btnReturn);
        toolBar.add(btnSale);
        toolBar.addSeparator();
        toolBar.add(btnQuery);
        toolBar.add(btnStatistics);

        return toolBar;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createEtchedBorder());

        JLabel lblStatus = new JLabel("当前用户：" + currentAdmin.getAdminName() +
                " | 权限级别：" + currentAdmin.getPermissionLevel());
        statusBar.add(lblStatus);

        return statusBar;
    }
}
