package MultiThreadExp.Server;

import MultiThreadExp.Request;

import java.sql.SQLException;

public class ExistenceHandler extends CommonHandler {

    public ExistenceHandler(Request request) {
        super(request);
    }

    @Override
    public Response handle() {
        try {
            if (request.action().startsWith("doc")) {
                var doc = ServerMain.db.getDocById(Integer.parseInt(request.data()[0]));
                return new Response(true, "", doc != null ? "yes" : "no");
            } else {
                var user = ServerMain.db.getUserByUsername(request.data()[0]);
                return new Response(true, "", user != null ? "yes" : "no");
            }
        } catch (SQLException e) {
            return new Response(false, e.getMessage(), "");
        }
    }
}
