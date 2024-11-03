import java.io.*;
import java.net.Socket;

class ServerWorker implements Runnable {
    private final Server server;
    private final Socket clientSocket;
    private PrintWriter out;
    private String username;

    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            this.out = out;

            
            out.println("Enter your username:");
            this.username = reader.readLine();
            System.out.println("User connected: " + username);

            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Received from " + username + ": " + message);
                server.broadcastMessage(username + ": " + message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.removeWorker(this);
            System.out.println("User disconnected: " + username);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public String getUsername() {
        return username;
    }
}