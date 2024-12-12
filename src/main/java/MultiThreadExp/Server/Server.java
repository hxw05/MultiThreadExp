package MultiThreadExp.Server;

import MultiThreadExp.Request;
import MultiThreadExp.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private ServerSocket socket;

    public Server(int port) {
        try {
            socket = new ServerSocket(port);
            socket.setSoTimeout(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int i = 0;
        while (true) {
            i++;
            Utils.logServer("Waiting for incoming input");
            try (
                    Socket client = socket.accept();
                    var reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    var writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            ) {
                var request = Request.from(reader.readLine());
                Utils.logServer("request=" + request);

                Response response;

                if (request == null) {
                    Utils.logServer("Cannot process request.");
                    response = new Response(false, "internal", "");
                } else {
                    response = switch (request.action()) {
                        case "login" -> new LoginHandler(request).handle();
                        case "user-exist", "doc-exist" -> new ExistenceHandler(request).handle();
                        case "user-delete" -> new UserDeleteHandler(request).handle();
                        case "user-insert" -> new UserInsertHandler(request).handle();
                        case "user-update" -> new UserUpdateHandler(request).handle();
                        case "file-upload" -> new FileUploadHandler(request).handle();
                        case "file-download" -> new FileDownloadHandler(request).handle();
                        case "get-all-doc", "get-all-user" -> new GetAllHandler(request).handle();
                        default -> new Response(false, "invalid action <" + request.action() + ">", null);
                    };

                    if (response.ok()) Utils.logServer("OK action <" + request.action() + ">");
                    else Utils.logServer("Failed action <" + request.action() + ">: " + response.message());
                }

                writer.write(response + "\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
                Utils.logServer("Catched IO exception");
                break;
            }
            Utils.logServer("Loop completed " + i);
        }
    }
}
