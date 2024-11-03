import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final int port;
    private final List<ServerWorker> workerList;

    public Server(int port) {
        this.port = port;
        this.workerList = new ArrayList<>();
    }

    public static void main(String[] args) {
        Server server = new Server(12345);
        server.start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                ServerWorker worker = new ServerWorker(this, clientSocket);
                workerList.add(worker);
                new Thread(worker).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void broadcastMessage(String message, ServerWorker sender) {
        for (ServerWorker worker : workerList) {
            if (worker != sender) {
                worker.sendMessage(message);
            }
        }
    }


    public void removeWorker(ServerWorker worker) {
        workerList.remove(worker);
    }
}


