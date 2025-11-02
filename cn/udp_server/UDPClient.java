import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            // Create a DatagramSocket
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");

            // Message to be sent to the server
            String message = "Hello, UDP Server!";
            byte[] buffer = message.getBytes();

            // Create a packet to send
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, 65432);
            socket.send(packet);
            System.out.println("Sent: " + message);

            // Prepare to receive the response
            byte[] responseBuffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(responsePacket);

            // Get the response from the server
            String responseMessage = new String(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.println("Received from server: " + responseMessage);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
