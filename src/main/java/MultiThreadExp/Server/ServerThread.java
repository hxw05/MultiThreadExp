package MultiThreadExp.Server;

import MultiThreadExp.Request;
import MultiThreadExp.Utils;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    public final int number;
    private final Socket client;

    public ServerThread(Socket client, int number) {
        this.client = client;
        this.number = number;
    }

    @Override
    public void run() {
        int i = 0;
        Utils.logServer("Waiting for incoming input", number);
        try (
                var reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                var writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        ) {
            while (true) {
                i++;
                var request = Request.from(reader.readLine());
                Utils.logServer("request=" + request, number);

                Response response;

                if (request == null) {
                    Utils.logServer("Cannot process request.");
                    response = new Response(false, "internal", "");
                } else {
                    if (request.action().equals("disconnect")) {
                        Utils.logServer("Connection closed by client", number);
                        break;
                    }

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

                    if (response.ok()) Utils.logServer("OK action <" + request.action() + ">", number);
                    else Utils.logServer("Failed action <" + request.action() + ">: " + response.message(), number);
                }

                writer.write(response + "\n");
                writer.flush();

                Utils.logServer("Loop completed " + i, number);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Utils.logServer("Catched IO exception", number);
        }

        interrupt();
    }

    public int getNumber() {
        return number;
    }
}
