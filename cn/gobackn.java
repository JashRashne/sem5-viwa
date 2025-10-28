import java.util.Random;
import java.util.Scanner;

public class gobackn {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of frames to send: ");
        int totalFrames = sc.nextInt();

        System.out.print("Enter window size: ");
        int windowSize = sc.nextInt();

        Random rand = new Random();
        int nextFrameToSend = 0;
        int ackExpected = 0;

        while (ackExpected < totalFrames) {
            System.out.println("\nSending frames:");

            // Send frames within the window
            for (int i = 0; i < windowSize && nextFrameToSend < totalFrames; i++) {
                System.out.println("Frame " + (nextFrameToSend + 1) + " sent.");
                nextFrameToSend++;
            }

            // Wait for acknowledgments
            for (int i = ackExpected; i < nextFrameToSend; i++) {
                boolean frameLost = rand.nextInt(10) < 2; // 20% chance of loss

                if (frameLost) {
                    System.out.println("Frame " + (i + 1) + " lost! Go back to frame " + (i + 1));
                    nextFrameToSend = i;
                    ackExpected = i;
                    break; // Retransmit from lost frame
                } else {
                    System.out.println("ACK for frame " + (i + 1) + " received.");
                    ackExpected = i + 1;
                }
            }
        }

        System.out.println("\nAll frames sent successfully!");
        sc.close();
    }
}
