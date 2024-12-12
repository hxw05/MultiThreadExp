package MultiThreadExp.Server;

import MultiThreadExp.Objects.User;
import MultiThreadExp.Request;

import java.sql.SQLException;

public class UserInsertHandler extends CommonHandler{
    public UserInsertHandler(Request request) {
        super(request);
    }

    @Override
    public Response handle() {
        var user = User.fromString(request.data()[0]);
        if (user == null) return new Response(false, "invalid user object", null);
        try {
            ServerMain.db.insertUser(user);
        } catch (SQLException e) {
            return new Response(false, e.getMessage(), null);
        }
        return new Response(true, null, null);
    }
}
