package MultiThreadExp;

import MultiThreadExp.Objects.Doc;
import MultiThreadExp.Objects.User;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.sql.SQLException;
import java.util.Enumeration;

public class UserActions {
    public static boolean downloadDoc(Doc doc, String targetPath) {
        return Utils.downloadFile(doc.getFilename(), targetPath);
    }

    public static boolean changePassword(User user, String newPassword) {
        Utils.log("修改密码");

        try {
            return DataProcessing.updateUser(user.getName(), newPassword, user.getRole());
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return false;
        }
    }

    public static @Nullable Doc uploadDoc(User user, String id, String description, String filepath) {
        Utils.log("上传文件");

        if (!Utils.uploadFile(new File(filepath))) {
            Utils.log("上传过程出现问题");
            return null;
        }

        try {
            var doc = new Doc(id, user.getName(), Utils.getCurrentTimestamp(), description, filepath);
            return DataProcessing.insertFile(id, doc) ? doc : null;
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return null;
        }
    }

    public static void exit() {
        Utils.log("退出系统");
        System.exit(0);
    }

    public static boolean updateUser(User user, String newPassword, String newRole) {
        Utils.log("修改用户");

        try {
            return DataProcessing.updateUser(user.getName(), newPassword, newRole);
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return false;
        }
    }

    public static boolean deleteUser(User user) {
        Utils.log("删除用户");

        try {
            return DataProcessing.deleteUser(user);
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return false;
        }
    }

    public static @Nullable User insertUser(String username, String password, String role) {
        Utils.log("新增用户");

        try {
            var user = new User(username, password, role);
            return DataProcessing.insertUser(user) ? user : null;
        } catch (SQLException e) {
            return null;
        }
    }
}
