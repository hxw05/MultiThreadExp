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
            Main.db.updateUser(user.getName(), newPassword, user.getRole());
            return true;
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return false;
        }
    }

    public static boolean uploadDoc(User user, Doc doc) {
        Utils.log("上传文件");

        if (!Utils.uploadFile(new File(doc.getFilepath()))) {
            Utils.log("上传过程出现问题");
            return false;
        }

        try {
            Main.db.insertFile(Integer.parseInt(doc.getID()), doc);
            return true;
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return false;
        } catch (NumberFormatException e) {
            Utils.log("无效编号");
            return false;
        }
    }

    public static void exit() {
        Utils.log("退出系统");
        System.exit(0);
    }

    public static boolean updateUser(User user, String newPassword, String newRole) {
        Utils.log("修改用户");

        try {
            Main.db.updateUser(user.getName(), newPassword, newRole);
            return true;
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return false;
        }
    }

    public static boolean deleteUser(User user) {
        Utils.log("删除用户");

        try {
            Main.db.deleteUser(user);
            return true;
        } catch (SQLException e) {
            Utils.log("数据库连接错误");
            return false;
        }
    }

    public static boolean insertUser(User user) {
        Utils.log("新增用户");

        try {
            Main.db.insertUser(user);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
