import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            // Create a DatagramSocket to listen on port 65432
            socket = new DatagramSocket(65432);
            byte[] buffer = new byte[1024];

            System.out.println("UDP Server is running...");

            while (true) {
                // Receive packet
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // Get data from the packet
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received: " + message);

                // Echo back the message to the client
                DatagramPacket responsePacket = new DatagramPacket(
                        packet.getData(), packet.getLength(),
                        packet.getAddress(), packet.getPort());
                socket.send(responsePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
