package MultiThreadExp.Server;

import MultiThreadExp.Request;
import MultiThreadExp.Utils;

import java.sql.SQLException;

public class GetAllHandler extends CommonHandler {
    public GetAllHandler(Request request) {
        super(request);
    }

    @Override
    public Response handle() {
        try {
            if (request.action().endsWith("doc")) {
                var res = ServerMain.db.getAllFiles();
                return new Response(true, null, Utils.serializeList(res));
            }

            if (request.action().endsWith("user")) {
                var res = ServerMain.db.getAllUser();
                return new Response(true, null, Utils.serializeList(res));
            }

            return new Response(false, null, null);
        } catch (SQLException e) {
            return new Response(false, e.getMessage(), null);
        }
    }
}
