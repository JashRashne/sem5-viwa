import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        final int PORT = 5000; // Server port number

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for a client...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            // For reading messages from client
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // For sending messages to client
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            // For reading messages from server console
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            // Thread for receiving messages from client
            new Thread(() -> {
                String messageFromClient;
                try {
                    while ((messageFromClient = input.readLine()) != null) {
                        System.out.println("Client: " + messageFromClient);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            }).start();

            // Main thread handles sending messages
            String messageToClient;
            while ((messageToClient = console.readLine()) != null) {
                output.println(messageToClient);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
