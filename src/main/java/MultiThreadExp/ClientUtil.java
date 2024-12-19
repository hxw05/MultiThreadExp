package MultiThreadExp;

import MultiThreadExp.Server.Response;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.function.Function;

public class ClientUtil {
    public static Socket getSocket() {
        try {
            return new Socket("localhost", 4567);
        } catch (IOException e) {
            return null;
        }
    }

    public static void sendMessage(String message) {
        var client = getSocket();
        if (client == null) {
            Utils.showErrorDialog("无法连接到服务器");
            return;
        }

        try (
                var writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))
        ) {
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> @Nullable T request(Request request, Function<Response, T> receiver) {
        var client = getSocket();
        if (client == null) {
            Utils.showErrorDialog("无法连接到服务器");
            return null;
        }

        try (
                var writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                var reader = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            writer.write(request.toString() + "\n");
            writer.flush();

            var respText = reader.readLine();
            Utils.logClient("respText=" + respText);
            return receiver.apply(Response.from(respText));
        } catch (IOException e) {
            e.printStackTrace();
            Utils.showErrorDialog("io exception caught");
        } catch (IllegalStateException e) {
            Utils.logClient("cannot parse response");
        }
        return null;
    }

    public static void request(Request request, Consumer<Response> receiver) {
        request(request, d -> {
            receiver.accept(d);
            return null;
        });
    }
}
