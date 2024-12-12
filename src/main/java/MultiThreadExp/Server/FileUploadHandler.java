package MultiThreadExp.Server;

import MultiThreadExp.Objects.Doc;
import MultiThreadExp.Request;
import MultiThreadExp.Utils;

import java.io.IOException;
import java.sql.SQLException;

public class FileUploadHandler extends CommonHandler {
    public static final String UPLOAD_PATH = "./upload";

    public FileUploadHandler(Request request) {
        super(request);
    }

    @Override
    public Response handle() {
        var decoded = Utils.base64StringToBytes(request.data()[0]);
        var doc = Doc.fromString(request.data()[1]);
        if (doc == null) {
            return new Response(false, "invalid doc object", null);
        }

        try {
            Utils.writeTo(UPLOAD_PATH, doc.getFilename(), decoded);
            ServerMain.db.insertFile(Integer.parseInt(doc.getID()), doc);
        } catch (IOException | SQLException e) {
            return new Response(false, e.getMessage(), null);
        }
        return new Response(true, null, null);
    }
}
