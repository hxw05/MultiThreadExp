package MultiThreadExp.Server;

import MultiThreadExp.Request;
import MultiThreadExp.Utils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class FileDownloadHandler extends CommonHandler {
    public FileDownloadHandler(Request request) {
        super(request);
    }

    @Override
    public Response handle() {
        var id = request.data()[0];
        try {
            var doc = ServerMain.db.getDocById(Integer.parseInt(id));
            if (doc == null) {
                return new Response(false, "could not find doc by id " + id, null);
            }
            var file = new File(FileUploadHandler.UPLOAD_PATH + "/" + doc.getFilename());
            if (!file.exists()) {
                return new Response(false, "target file is missing", null);
            }
            return new Response(true, null, Utils.base64FileToString(file));
        } catch (SQLException | IOException e) {
            return new Response(false, e.getMessage(), null);
        }
    }
}
