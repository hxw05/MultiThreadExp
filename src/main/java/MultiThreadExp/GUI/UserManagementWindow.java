package MultiThreadExp.GUI;

import MultiThreadExp.DataProcessing;
import MultiThreadExp.Objects.User;
import MultiThreadExp.UserActions;
import MultiThreadExp.Utils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class UserManagementWindow extends CancellableWindow {
    public final JTabbedPane tabbedPane = new JTabbedPane();
    private final String[] roles = new String[]{"administrator", "browser", "operator"};
    private final DefaultTableModel userTableModel;
    private final JTable userTable;
    private final String[] tableHeader = new String[]{"用户名", "密码", "角色"};

    public UserManagementWindow() {
        this.setLayout(null);
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);

        var data = getUserList();
        userTableModel = new DefaultTableModel(
                Utils.toDataVector(getUserList()),
                tableHeader
        );
        userTable = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable.setModel(userTableModel);
        userTable.setFillsViewportHeight(true);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabbedPane.addTab("添加用户", getAddUserPanel());
        tabbedPane.addTab("修改用户", getAlterUserPanel());
        tabbedPane.addTab("删除用户", getDeleteUserPanel());

        this.setContentPane(tabbedPane);
    }

    public List<User> getUserList() {
        Enumeration<User> userEnum;
        try {
            userEnum = DataProcessing.getAllUser();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Collections.list(userEnum);
    }

    private JPanel getDeleteUserPanel() {
        var panel = new JPanel();
        panel.setLayout(new BorderLayout());

        var scrollPane = new JScrollPane(userTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        var buttonPanel = new JPanel();
        final var deleteButton = getDeleteButton();

        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private @NotNull JButton getDeleteButton() {
        var deleteButton = new JButton("删除");

        deleteButton.addActionListener(e -> {
            Utils.showConfirmDialog("确定要删除该用户吗？", () -> {
                var selectedIndex = userTable.getSelectedRow();
                if (selectedIndex == -1) {
                    Utils.showWarnDialog("请选择要删除的用户");
                    return;
                }
                var targetUser = getUserList().get(selectedIndex);
                if (UserActions.deleteUser(targetUser)) {
                    Utils.showOKDialog("删除成功");
                    userTableModel.removeRow(selectedIndex);
                } else {
                    Utils.showErrorDialog("删除失败");
                }
            });
        });
        return deleteButton;
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

        final var addButton = getAddButton(usernameField, passwordField, roleSelection);

        controlPanel.add(addButton);
        controlPanel.add(cancelButton);

        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(inputPanel);
        panel.add(controlPanel);
        return panel;
    }

    private @NotNull JButton getAddButton(JTextField usernameField, JTextField passwordField, JComboBox<String> roleSelection) {
        var addButton = new JButton("添加");

        addButton.addActionListener(e -> {
            var username = usernameField.getText();

            try {
                var user = DataProcessing.searchUserByName(username);
                if (user != null) {
                    Utils.showWarnDialog("此用户名已存在");
                    return;
                }
            } catch (SQLException ex) {
                Utils.showErrorDialog("无法连接数据库");
                return;
            }

            var password = passwordField.getText();
            var role = (String) roleSelection.getSelectedItem();

            var user = UserActions.insertUser(username, password, role);
            if (user != null) {
                Utils.showOKDialog("添加成功");
                this.userTableModel.addRow(user.toDataRow());
            } else {
                Utils.showErrorDialog("添加失败");
            }
        });
        return addButton;
    }

    private JPanel getAlterUserPanel() {
        var inputPanel = new JPanel();

        var layout = new GroupLayout(inputPanel);
        inputPanel.setLayout(layout);

        var label1 = new JLabel("用户名");
        var label2 = new JLabel("密码");
        var label3 = new JLabel("角色");

        var usernameSelection = new JComboBox<>(getUserList().toArray(new User[0]));
        var passwordField = new JTextField(16);
        var roleSelection = new JComboBox<>(roles);

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
                                        .addComponent(usernameSelection)
                                        .addComponent(passwordField)
                                        .addComponent(roleSelection)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label1)
                                        .addComponent(usernameSelection)
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

        var controlPanel = new JPanel();

        final var alterButton = getAlterButton(usernameSelection, passwordField, roleSelection);

        controlPanel.add(alterButton);
        controlPanel.add(cancelButton);

        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(inputPanel);
        panel.add(controlPanel);

        return panel;
    }

    private @NotNull JButton getAlterButton(JComboBox<User> usernameSelection, JTextField passwordField, JComboBox<String> roleSelection) {
        var alterButton = new JButton("修改");

        alterButton.addActionListener(e -> {
            var targetUserIndex = usernameSelection.getSelectedIndex();
            if (targetUserIndex == -1) return;
            var targetUser = getUserList().get(targetUserIndex);

            var password = passwordField.getText();
            if (password.isBlank()) password = targetUser.getPassword();

            var role = (String) roleSelection.getSelectedItem();

            if (UserActions.updateUser(targetUser, password, role)) {
                Utils.showOKDialog("修改成功");
                userTableModel.setDataVector(
                        Utils.toDataVector(getUserList()),
                        tableHeader
                );
            } else {
                Utils.showErrorDialog("修改失败");
            }
        });
        return alterButton;
    }
}
