package MultiThreadExp.Server;

import MultiThreadExp.Request;

import java.sql.SQLException;

public class UserDeleteHandler extends CommonHandler {
    public UserDeleteHandler(Request request) {
        super(request);
    }

    @Override
    public Response handle() {
        var username = request.data()[0];
        try {
            ServerMain.db.deleteUser(username);
            return new Response(true, null, null);
        } catch (SQLException e) {
            return new Response(false, e.getMessage(), null);
        }
    }
}
