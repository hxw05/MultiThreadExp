package MultiThreadExp.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class LoginWindow extends JFrame {
    private final Consumer<LoginData> onLoginButtonClicked;
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginWindow(Consumer<LoginData> onLoginButtonClicked) {
        this.setTitle("系统登录");
        this.setSize(500, 400);
        // 关闭窗口=退出进程
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.onLoginButtonClicked = onLoginButtonClicked;

        var panel = new JPanel(new FlowLayout());
        var usernameLabel = new JLabel("用户名");
        var passwordLabel = new JLabel("密码");
        usernameField = new JTextField(16);
        passwordField = new JPasswordField(16);

        var loginButton = new JButton("确定");
        var exitButton = new JButton("取消");

        loginButton.addActionListener(e -> {
            this.onLoginButtonClicked.accept(
                    new LoginData(
                            usernameField.getText(),
                            new String(passwordField.getPassword())
                    )
            );
        });
        // 关闭窗口，结束进程
        exitButton.addActionListener(e -> this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(exitButton);

        this.add(panel);

        var rootPane = SwingUtilities.getRootPane(loginButton);
        rootPane.setDefaultButton(loginButton);
    }
}
