package MultiThreadExp.Server;

import MultiThreadExp.Request;

import java.sql.SQLException;

public class UserUpdateHandler extends CommonHandler {
    public UserUpdateHandler(Request request) {
        super(request);
    }

    @Override
    public Response handle() {
        try {
            ServerMain.db.updateUser(request.data()[0], request.data()[1], request.data()[2]);
            return new Response(true, null, null);
        } catch (SQLException e) {
            return new Response(false, e.getMessage(), null);
        }
    }
}
