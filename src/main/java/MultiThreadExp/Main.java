package MultiThreadExp;

import MultiThreadExp.Objects.User;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        while (true) {
            Utils.log("****欢迎进入档案系统****");
            Utils.log("1. 登录");
            Utils.log("2. 退出");
            Utils.log("**********************");

            var choice = Utils.read("请选择菜单：", Integer.class);

            if (choice == null) {
                Utils.log("请输入有效的数字");
                continue;
            }

            switch (choice) {
                case 1: {
                    var loginUser = login();
                    if (loginUser == null) continue;
                    loginUser.startLoop();
                    break;
                }

                case 2: {
                    Utils.log("退出系统");
                    return;
                }
            }
        }
    }

    public static User login() {
        var name = Utils.read("请输入用户名：", String.class);

        User user = null;
        try {
            user = DataProcessing.searchUserByName(name);
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return null;
        }
        if (user == null) {
            Utils.log("用户名不存在！");
            return null;
        }
        var password = Utils.read("请输入密码：", String.class);
        if (!user.getPassword().equals(password)) {
            Utils.log("密码错误");
            return null;
        }
        return user;
    }
}