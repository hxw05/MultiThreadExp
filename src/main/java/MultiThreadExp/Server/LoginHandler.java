package MultiThreadExp.Server;

import MultiThreadExp.Objects.User;
import MultiThreadExp.Request;
import MultiThreadExp.Utils;

import java.sql.SQLException;

public class LoginHandler extends CommonHandler {
    public LoginHandler(Request request) {
        super(request);
    }

    @Override
    public Response handle() {
        String username = request.data()[0];
        String password = request.data()[1];
        User user;
        try {
            user = ServerMain.db.getUserByUsername(username);
        } catch (SQLException e) {
            Utils.logServer("数据库连接错误");
            return new Response(false, "数据库连接错误", null);
        }
        if (user == null) {
            Utils.logServer("用户名不存在！");
            return new Response(false, "用户名不存在！", null);
        }
        if (!user.getPassword().equals(password)) {
            Utils.logServer("密码错误");
            return new Response(false, "密码错误", null);
        }
        return new Response(true, "", user.toString());
    }
}
