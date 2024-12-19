package MultiThreadExp;

import MultiThreadExp.GUI.LoginWindow;
import MultiThreadExp.GUI.MainWindow;
import MultiThreadExp.Objects.User;

import javax.swing.*;

public class Main {
    public static JFrame loginWindow;
    public static JFrame mainWindow;


    public static void main(String[] args) {
        Utils.init();
        Client.init();

        loginWindow = new LoginWindow(data -> Client.request(
                new Request("login", data.username(), data.password()),
                d -> {
                    if (!d.ok()) {
                        JOptionPane.showMessageDialog(null, "用户不存在或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    var user = User.fromString(d.data());
                    if (user == null) {
                        Utils.logClient("unable to parse user from " + d.data());
                        return;
                    }
                    loginWindow.setVisible(false);
                    mainWindow = new MainWindow(user);
                    mainWindow.setVisible(true);
                }
        ));

        loginWindow.setVisible(true);
    }
}