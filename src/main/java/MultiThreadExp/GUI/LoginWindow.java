package MultiThreadExp.GUI;

import MultiThreadExp.Client;
import MultiThreadExp.LayoutUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

import static MultiThreadExp.LayoutUtil.boxt;

public class LoginWindow extends JFrame {
    private final Consumer<LoginData> onLoginButtonClicked;
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginWindow(Consumer<LoginData> onLoginButtonClicked) {
        this.setTitle("系统登录");
        this.setSize(300, 200);
        // 关闭窗口=退出进程
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.onLoginButtonClicked = onLoginButtonClicked;

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Client.close();
                e.getWindow().dispose();
            }
        });

        var mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        var inputPanel = new JPanel();
        var layout = new GroupLayout(inputPanel);
        inputPanel.setLayout(layout);

        var label1 = new JLabel("用户名");
        var label2 = new JLabel("密码");
        usernameField = new JTextField(16);
        boxt(usernameField);
        passwordField = new JPasswordField(16);
        boxt(passwordField);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(label1)
                                        .addComponent(label2)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(usernameField)
                                        .addComponent(passwordField)
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
        );

        var controlPanel = new JPanel();

        var loginButton = new JButton("确定");
        var exitButton = new JButton("退出");

        loginButton.addActionListener(e -> this.onLoginButtonClicked.accept(
                new LoginData(
                        usernameField.getText(),
                        new String(passwordField.getPassword())
                )
        ));
        // 关闭窗口，结束进程
        exitButton.addActionListener(e -> this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        controlPanel.add(loginButton);
        controlPanel.add(exitButton);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(inputPanel);
        mainPanel.add(controlPanel);
        mainPanel.add(Box.createVerticalGlue());

        this.getRootPane().setDefaultButton(loginButton);

        this.setContentPane(mainPanel);
    }
}
