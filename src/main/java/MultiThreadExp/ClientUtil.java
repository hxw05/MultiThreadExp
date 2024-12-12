package MultiThreadExp;

import MultiThreadExp.Server.Response;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.function.Function;

public class ClientUtil {
    public static Socket getSocket() throws IOException {
        return new Socket("localhost", 4567);
    }

    public static void sendMessage(String message) {
        try (
                var client = getSocket();
                var writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))
        ) {
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> @Nullable T request(Request request, Function<Response, T> receiver) {
        try (
                var client = getSocket();
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
