import java.util.Scanner;

public class hamming {

    // Check if a number is a power of two
    static boolean isPowerOfTwo(int x) {
        rex turn x != 0 && (x & (x - 1)) == 0;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter received Hamming code: ");
        String input = sc.next();

        int len = input.length();
        int[] bits = new int[len + 1];
        int[] parityCheckBits = new int[20];
        int parityCount = 0;

        // Convert string input to integer array (1-indexed)
        for (int i = 0; i < len; i++) {
            bits[i + 1] = input.charAt(i) - '0';
        }

        // Calculate parity bits
        for (int i = 1; i <= len; i *= 2) {
            int parity = 0;
            for (int j = 1; j <= len; j++) {
                if ((j & i) != 0) {
                    parity ^= bits[j];
                }
            }
            parityCheckBits[parityCount++] = parity;
        }

        // Determine error position
        int errorPos = 0;
        for (int i = 0; i < parityCount; i++) {
            if (parityCheckBits[i] != 0) {
                errorPos += (1 << i);
            }
        }

        // Display results
        if (errorPos == 0) {
            System.out.println("No error detected.");
        } else {
            System.out.println("Error found at bit position: " + errorPos);
            bits[errorPos] ^= 1; // Correct the error
        }

        // Print corrected code
        System.out.print("Corrected code: ");
        for (int i = 1; i <= len; i++) {
            System.out.print(bits[i]);
        }
        System.out.println();

        sc.close();
    }
}
