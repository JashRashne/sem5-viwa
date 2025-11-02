import java.io.*;
import java.net.*;

public class client {
    public static void main(String[] args) {
        final String SERVER_IP = "127.0.0.1"; // Localhost (can be changed to remote IP)
        final int SERVER_PORT = 5000;

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            System.out.println("Connected to the server!");

            // For reading messages from server
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // For sending messages to server
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            // For reading user input
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            // Thread for receiving messages from server
            new Thread(() -> {
                String messageFromServer;
                try {
                    while ((messageFromServer = input.readLine()) != null) {
                        System.out.println("Server: " + messageFromServer);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            }).start();

            // Main thread handles sending messages
            String messageToServer;
            while ((messageToServer = console.readLine()) != null) {
                output.println(messageToServer);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
