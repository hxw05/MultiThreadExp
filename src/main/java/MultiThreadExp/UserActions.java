package MultiThreadExp;

import MultiThreadExp.Objects.Doc;
import MultiThreadExp.Objects.User;
import MultiThreadExp.Server.Response;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class UserActions {
    public static boolean downloadDoc(Doc doc, String targetPath) {
        Utils.logClient("下载文件");

        String res = ClientUtil.request(
                new Request("file-download", doc.getID()),
                d -> {
                    if (!d.ok()) {
                        Utils.logClient("服务端错误：" + d.message());
                        return null;
                    }
                    return d.data();
                }
        );

        if (res == null) return false;

        var decoded = Utils.base64StringToBytes(res);
        try {
            Utils.writeTo(targetPath, doc.getFilename(), decoded);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Utils.logClient("无法写入文件");
            return false;
        }
    }

    public static boolean changePassword(User user, String newPassword) {
        Utils.logClient("修改密码");

        return Boolean.TRUE.equals(ClientUtil.request(new Request("user-update", user.getName(), newPassword, user.getRole()), Response::ok));
    }

    public static boolean uploadDoc(Doc doc) {
        Utils.logClient("上传文件");

        var file = new File(doc.getFilepath());
        if (!file.exists()) {
            Utils.logClient("文件不存在");
            return false;
        }

        String encoded;
        try {
            encoded = Utils.base64FileToString(file);
        } catch (IOException e) {
            Utils.logClient("无法解析文件");
            return false;
        }

        return Boolean.TRUE.equals(ClientUtil.request(new Request("file-upload", encoded, doc.toString()), Response::ok));
    }

    public static boolean updateUser(User user, String newPassword, String newRole) {
        Utils.logClient("修改用户");

        return Boolean.TRUE.equals(ClientUtil.request(new Request("user-update", user.getName(), newPassword, newRole), Response::ok));
    }

    public static boolean deleteUser(User user) {
        Utils.logClient("删除用户");

        return Boolean.TRUE.equals(ClientUtil.request(new Request("user-delete", user.getName()), Response::ok));
    }

    public static boolean insertUser(User user) {
        Utils.logClient("新增用户");

        return Boolean.TRUE.equals(ClientUtil.request(new Request("user-insert", user.toString()), Response::ok));
    }
}
