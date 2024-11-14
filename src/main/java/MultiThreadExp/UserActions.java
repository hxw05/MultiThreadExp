package MultiThreadExp;

import MultiThreadExp.Objects.File;
import MultiThreadExp.Objects.User;

import java.sql.SQLException;
import java.util.Enumeration;

public class UserActions {
    public static boolean downloadFile() {
        Utils.log("下载文件");
        var archiveNumber = Utils.read("请输入档案号：", String.class);

        if (archiveNumber == null) {
            Utils.log("请输入正确的值");
            return false;
        }

        return true;
    }

    public static void listFiles() {
        Utils.log("文件列表");
        Enumeration<File> archives = null;
        try {
            archives = DataProcessing.getAllFiles();
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return;
        }
        Utils.log("文件编号\t文件名\t文件简介");
        Utils.enumForEach(archives, a -> Utils.log(a.id() + "\t" + a.filename() + "\t" + a.description()));
    }

    public static boolean changePassword(User user) {
        Utils.log("修改密码");
        var newPassword = Utils.read("请输入新密码：", String.class);
        if (newPassword == null) {
            Utils.log("请输入正确的值");
            return false;
        }

        try {
            return DataProcessing.updateUser(user.getName(), newPassword, user.getRole());
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return false;
        }
    }

    public static boolean uploadArchive() {
        Utils.log("上传文件");
        var filename = Utils.read("请输入文件名：", String.class);
        if (filename == null) {
            Utils.log("请输入有效的文件名");
            return false;
        }
        var description = Utils.read("请输入文件简介：", String.class);
        // 简介可以没有，所以不进行判断
        if (description == null) description = "";
        var id = Utils.read("请输入文件编号：", String.class);
        if (id == null) {
            Utils.log("请输入有效的文件编号");
            return false;
        }

        try {
            return DataProcessing.insertFile(id, new File(id, filename, description));
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return false;
        }
    }

    public static void exit() {
        Utils.log("退出系统");
        System.exit(0);
    }

    public static boolean updateUser() {
        Utils.log("修改用户");
        var username = Utils.read("请输入用户名：", String.class);
        if (username == null) {
            Utils.log("请输入正确的用户名");
            return false;
        }
        User user = null;
        try {
            user = DataProcessing.searchUserByName(username);
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return false;
        }
        if (user == null) {
            Utils.log("用户不存在");
            return false;
        }

        var password = Utils.read("请输入新密码（留空则保持不变）：", String.class);
        var role = Utils.read("请输入新角色（留空则保持不变）：", String.class);

        try {
            return DataProcessing.updateUser(username, password, role);
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return false;
        }
    }

    public static boolean deleteUser() {
        Utils.log("删除用户");
        var username = Utils.read("请输入用户名：", String.class);
        if (username == null) {
            Utils.log("请输入正确的用户名");
            return false;
        }
        User user = null;
        try {
            user = DataProcessing.searchUserByName(username);
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return false;
        }
        if (user == null) {
            Utils.log("用户不存在");
            return false;
        }

        try {
            return DataProcessing.deleteUser(username);
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return false;
        }
    }

    public static boolean insertUser() {
        Utils.log("新增用户");
        var username = Utils.read("请输入用户名：", String.class);
        if (username == null) {
            Utils.log("请输入正确的用户名");
            return false;
        }
        User user = null;
        try {
            user = DataProcessing.searchUserByName(username);
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return false;
        }
        if (user != null) {
            Utils.log("用户已经存在");
            return false;
        }
        var password = Utils.read("请输入密码：", String.class);
        if (password == null) {
            Utils.log("请输入正确的密码");
            return false;
        }
        var role = Utils.read("请输入角色：", String.class);
        if (role == null) {
            Utils.log("请输入正确的角色");
            return false;
        }

        try {
            return DataProcessing.insertUser(username, password, role);
        } catch (SQLException e) {
            return false;
        }
    }
}
