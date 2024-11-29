package MultiThreadExp.GUI;

import MultiThreadExp.Objects.User;

import javax.swing.*;
import java.util.List;

public class MainWindow extends JFrame {
    public User user;
    public UserManagementWindow userManagementWindow;
    public FileManagementWindow fileManagementWindow;
    public PersonalManagementWindow personalManagementWindow;

    public MainWindow(User user) {
        this.user = user;
        this.setSize(900, 800);
        this.setTitle(
                switch (user.getRole()) {
                    case "administrator" -> "系统管理员";
                    case "operator" -> "系统操作员";
                    case "browser" -> "档案录入员";
                    default -> "";
                } + "界面"
        );
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        var menuBar = new JMenuBar();
        var menuUserManagement = new JMenu("用户管理");
        var menuFileManagement = new JMenu("档案管理");
        var menuPersonalManagement = new JMenu("个人信息管理");

        var menuUserManagementItems = List.of(
                new JMenuItem("新增用户"),
                new JMenuItem("修改用户"),
                new JMenuItem("删除用户")
        );
        this.userManagementWindow = new UserManagementWindow();
        for (int i = 0; i < menuUserManagementItems.size(); i++) {
            int finalI = i;
            menuUserManagementItems.get(i).addActionListener(e -> userManagementWindow.tabbedPane.setSelectedIndex(finalI));
        }
        menuUserManagementItems.forEach(s -> s.addActionListener(e -> userManagementWindow.setVisible(true)));

        var menuFileManagementItems = List.of(
                new JMenuItem("上传档案"),
                new JMenuItem("下载档案")
        );
        this.fileManagementWindow = new FileManagementWindow();
        for (int i = 0; i < menuFileManagementItems.size(); i++) {
            int finalI = i;
            menuFileManagementItems.get(i).addActionListener(e -> fileManagementWindow.tabbedPane.setSelectedIndex(finalI));
        }
        menuFileManagementItems.forEach(s -> s.addActionListener(e -> fileManagementWindow.setVisible(true)));

        var menuPersonalManagementItems = List.of(
                new JMenuItem("信息修改")
        );
        this.personalManagementWindow = new PersonalManagementWindow(this.user);
        menuPersonalManagementItems.get(0).addActionListener(e -> personalManagementWindow.setVisible(true));

        switch (user.getRole()) {
            case "operator" -> menuUserManagementItems.forEach(s -> s.setEnabled(false));
            case "browser" -> {
                menuUserManagementItems.forEach(s -> s.setEnabled(false));
                menuFileManagementItems.get(0).setEnabled(false);
            }
        }

        menuUserManagementItems.forEach(menuUserManagement::add);
        menuFileManagementItems.forEach(menuFileManagement::add);
        menuPersonalManagementItems.forEach(menuPersonalManagement::add);

        menuBar.add(menuUserManagement);
        menuBar.add(menuFileManagement);
        menuBar.add(menuPersonalManagement);
        this.setJMenuBar(menuBar);
    }
}
