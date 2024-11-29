package MultiThreadExp.GUI;

import MultiThreadExp.DataProcessing;
import MultiThreadExp.Objects.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class UserManagementWindow extends JFrame {
    public final JTabbedPane tabbedPane = new JTabbedPane();
    private final String[] roles = new String[]{"administrator", "browser", "operator"};
    private final List<User> users;

    public UserManagementWindow() {
        this.setLayout(null);
        this.setSize(400, 400);

        Enumeration<User> userEnum;
        try {
            userEnum = DataProcessing.getAllUser();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        users = Collections.list(userEnum);

        tabbedPane.addTab("添加用户", getAddUserPanel());
        tabbedPane.addTab("修改用户", getAlterUserPanel());
        tabbedPane.addTab("删除用户", getDeleteUserPanel());

        this.setContentPane(tabbedPane);
    }

    private JPanel getDeleteUserPanel() {
        var panel = new JPanel();
        panel.setLayout(new BorderLayout());
        var data = users.stream().map(u -> new String[]{u.getName(), u.getPassword(), u.getRole()}).toList().toArray(new String[0][0]);
        var table = new JTable(data, new String[]{"用户名", "密码", "角色"}) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        var scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        var buttonPanel = new JPanel();
        var addButton = new JButton("删除");
        var cancelButton = new JButton("取消");

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getAddUserPanel() {
        var inputPanel = new JPanel();

        var layout = new GroupLayout(inputPanel);

        var label1 = new JLabel("用户名");
        var label2 = new JLabel("密码");
        var label3 = new JLabel("角色");

        var usernameField = new JTextField(16);
        var passwordField = new JTextField(16);
        var roleSelection = new JComboBox<>(roles);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(label1)
                                        .addComponent(label2)
                                        .addComponent(label3)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(usernameField)
                                        .addComponent(passwordField)
                                        .addComponent(roleSelection)
                        )
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label1)
                                        .addComponent(usernameField)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label2)
                                        .addComponent(passwordField)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label3)
                                        .addComponent(roleSelection)
                        )
        );

        inputPanel.setLayout(layout);

        var controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        var addButton = new JButton("添加");
        var cancelButton = new JButton("取消");

        controlPanel.add(addButton);
        controlPanel.add(cancelButton);

        var panel = new JPanel();
        panel.add(inputPanel);
        panel.add(controlPanel);
        return panel;
    }

    private JPanel getAlterUserPanel() {
        var panel = new JPanel();

        var inputPanel = new JPanel();

        var layout = new GroupLayout(inputPanel);
        inputPanel.setLayout(layout);

        var label1 = new JLabel("用户名");
        var label2 = new JLabel("密码");
        var label3 = new JLabel("角色");

        var usernameSelection = new JComboBox<>(users.toArray(new User[0]));
        var passwordField = new JTextField(16);
        var roleSelection = new JComboBox<>(roles);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup()
                                        .addComponent(label1)
                                        .addComponent(label2)
                                        .addComponent(label3)
                        )
                        .addGroup(
                                layout.createParallelGroup()
                                        .addComponent(usernameSelection)
                                        .addComponent(passwordField)
                                        .addComponent(roleSelection)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup()
                                        .addComponent(label1)
                                        .addComponent(usernameSelection)
                        )
                        .addGroup(
                                layout.createParallelGroup()
                                        .addComponent(label2)
                                        .addComponent(passwordField)
                        )
                        .addGroup(
                                layout.createParallelGroup()
                                        .addComponent(label3)
                                        .addComponent(roleSelection)
                        )
        );

        var controlPanel = new JPanel();

        var alterButton = new JButton("修改");
        var cancelButton = new JButton("取消");

        controlPanel.add(alterButton);
        controlPanel.add(cancelButton);

        panel.add(inputPanel);
        panel.add(controlPanel);

        return panel;
    }
}
