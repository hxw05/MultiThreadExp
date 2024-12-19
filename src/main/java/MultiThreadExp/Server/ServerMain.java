package MultiThreadExp.Server;

import MultiThreadExp.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerMain {
    public static ServerThread server;
    public static Database db;
    public static ServerSocket serverSocket;

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
                Utils.logServer("Client #" + clientNumber + " connected to server");
                var thread = new ServerThread(socket, clientNumber);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
