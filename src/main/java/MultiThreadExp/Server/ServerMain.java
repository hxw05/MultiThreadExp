package MultiThreadExp.Server;

public class ServerMain {
    public static Server server;
    public static Database db;

    public static void main(String[] args) {
        db = new Database();
        server = new Server(4567);
        server.start();
    }
}
