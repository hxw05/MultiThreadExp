package MultiThreadExp.Server;

import MultiThreadExp.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ServerMain {
    public static Database db;
    public static ServerSocket serverSocket;
    public static List<ServerThread> serverThreads = new ArrayList<>();

    public static void main(String[] args) {
        db = new Database();
        try {
            serverSocket = new ServerSocket(4567);
        } catch (IOException e) {
            e.printStackTrace();
            Utils.logServer("Cannot open server socket");
            return;
        }

        int clientNumber = 0;

        try {
            while (true) {
                var socket = serverSocket.accept();
                clientNumber++;
                Utils.logServer("Client #" + clientNumber + " connected to server.");
                var thread = new ServerThread(socket, clientNumber);
                serverThreads.add(thread);
                thread.start();

                Utils.logServer("Current online: " + String.join(",",
                        serverThreads.stream().filter(Thread::isAlive).map(x -> "#" + x.getNumber()).toList()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
