package MultiThreadExp;

import MultiThreadExp.GUI.LoginData;
import MultiThreadExp.GUI.LoginWindow;
import MultiThreadExp.GUI.MainWindow;
import MultiThreadExp.Objects.User;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static JFrame loginWindow;
    public static JFrame mainWindow;

    public static void main(String[] args) {
        Utils.init();

        loginWindow = new LoginWindow(data -> {
            var user = login(data);
            if (user == null) {
                JOptionPane.showMessageDialog(null, "用户不存在或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            loginWindow.setVisible(false);
            mainWindow = new MainWindow(user);
            mainWindow.setVisible(true);
        });

        loginWindow.setVisible(true);
    }

    public static User login(LoginData loginData) {
        User user;
        try {
            user = DataProcessing.searchUserByName(loginData.username());
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return null;
        }
        if (user == null) {
            Utils.log("用户名不存在！");
            return null;
        }
        if (!user.getPassword().equals(loginData.password())) {
            Utils.log("密码错误");
            return null;
        }
        return user;
    }
}