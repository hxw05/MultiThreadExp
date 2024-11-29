package MultiThreadExp.GUI;

import MultiThreadExp.Objects.User;
import MultiThreadExp.UserActions;
import MultiThreadExp.Utils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class PersonalManagementWindow extends CancellableWindow {
    public User user;

    public PersonalManagementWindow(User user) {
        this.setSize(400, 400);
        this.user = user;

        this.setContentPane(getAlterPanel());
        this.setTitle("个人信息管理");
        this.setLocationRelativeTo(null);

    }

    private JPanel getAlterPanel() {
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        var inputPanel = new JPanel();
        var layout = new GroupLayout(inputPanel);
        inputPanel.setLayout(layout);

        var label1 = new JLabel("用户名");
        var label2 = new JLabel("原密码");
        var label3 = new JLabel("新密码");
        var label4 = new JLabel("确认新密码");
        var label5 = new JLabel("角色");

        var usernameField = new JTextField(this.user.getName(), 16);
        usernameField.setEnabled(false);
        var originalPasswordField = new JPasswordField(16);
        var newPasswordField = new JPasswordField(16);
        var confirmPasswordField = new JPasswordField(16);
        var roleField = new JTextField(this.user.getRole(), 16);
        roleField.setEnabled(false);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(label1)
                                        .addComponent(label2)
                                        .addComponent(label3)
                                        .addComponent(label4)
                                        .addComponent(label5)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(usernameField)
                                        .addComponent(originalPasswordField)
                                        .addComponent(newPasswordField)
                                        .addComponent(confirmPasswordField)
                                        .addComponent(roleField)
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
                                        .addComponent(originalPasswordField)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label3)
                                        .addComponent(newPasswordField)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label4)
                                        .addComponent(confirmPasswordField)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label5)
                                        .addComponent(roleField)
                        )
        );

        var controlPanel = new JPanel();
        final var okButton = getOKButton(originalPasswordField, newPasswordField, confirmPasswordField);

        controlPanel.add(okButton);
        controlPanel.add(cancelButton);

        panel.add(inputPanel);
        panel.add(controlPanel);

        return panel;
    }

    private @NotNull JButton getOKButton(JPasswordField originalPasswordField, JPasswordField newPasswordField, JPasswordField confirmPasswordField) {
        var okButton = new JButton("确定");

        okButton.addActionListener(l -> {
            var originalPassword = new String(originalPasswordField.getPassword());
            if (!originalPassword.equals(this.user.getPassword())) {
                Utils.showWarnDialog("原密码不符");
                return;
            }
            var newPassword = new String(newPasswordField.getPassword());
            var confirmPassword = new String(confirmPasswordField.getPassword());
            if (!confirmPassword.equals(newPassword)) {
                Utils.showWarnDialog("确认密码与新密码不符");
                return;
            }
            if (UserActions.changePassword(this.user, newPassword)) {
                Utils.showOKDialog("修改成功");
            } else {
                Utils.showErrorDialog("修改过程出现问题");
            }
        });

        return okButton;
    }
}
