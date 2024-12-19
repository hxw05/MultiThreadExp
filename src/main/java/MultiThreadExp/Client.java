package MultiThreadExp;

import MultiThreadExp.Server.Response;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.function.Function;

public class Client {
    public static Socket client;
    public static OutputStream outputStream;
    public static InputStream inputStream;
    public static BufferedWriter writer;
    public static BufferedReader reader;

    public static void init() {
        try {
            client = new Socket("localhost", 4567);
            outputStream = client.getOutputStream();
            inputStream = client.getInputStream();
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            reader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (IOException e) {
            client = null;
        }
    }

    public static void sendMessage(String message) {
        if (client == null) {
            Utils.showErrorDialog("无法连接到服务器");
            return;
        }

        try {
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> @Nullable T request(Request request, Function<Response, T> receiver) {
        if (client == null) {
            Utils.showErrorDialog("无法连接到服务器");
            return null;
        }

        try {
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
