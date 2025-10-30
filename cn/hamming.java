import java.util.*;

class HammingCode {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("\nRushikesh Sarap (Comps) TE C31 2303151");

        System.out.print("\nEnter the data bits (binary string): ");
        String data = sc.nextLine();

        String encoded = encodeHamming(data);
        System.out.println("\nEncoded Hamming Code: ");
        for (int i = encoded.length() - 1; i >= 0; i--)
            System.out.print(encoded.charAt(i));

        String received = introduceError(encoded);
        System.out.println("\n\nReceived with Error: ");
        for (int i = received.length() - 1; i >= 0; i--)
            System.out.print(received.charAt(i));

        System.out.println("\n\nCorrected Hamming Code: ");
        String corrected = correctHamming(received);
        for (int i = corrected.length() - 1; i >= 0; i--)
            System.out.print(corrected.charAt(i));

        System.out.println("\n\nInformation Obtained at the end: ");
        System.out.println("Data Bits: " + getDataBits(corrected));
        System.out.println("Parity Bits: " + getParityBits(corrected));

        System.out.println("\n");
        sc.close();
    }

    // Encode Hamming code
    public static String encodeHamming(String data) {
        int n = data.length(), p = 0;

        while (Math.pow(2, p) < n + p + 1)
            p++;

        StringBuilder encoded = new StringBuilder();
        int j = 0, k = data.length() - 1;
        encoded.append('0');

        for (int i = 1; i <= n + p; i++) {
            if (Math.pow(2, j) == i) {
                encoded.append('0');
                j++;
            } else {
                encoded.append(data.charAt(k));
                k--;
            }
        }

        for (int i = 0; i < p; i++) {
            int parityIndex = (int) Math.pow(2, i);
            int count = 0;
            for (int bitIndex = parityIndex; bitIndex <= n + p; bitIndex += parityIndex * 2) {
                for (int l = 0; l < parityIndex && bitIndex + l <= n + p; l++) {
                    if (encoded.charAt(bitIndex + l) == '1')
                        count++;
                }
            }
            if (count % 2 != 0)
                encoded.setCharAt(parityIndex, '1');
        }

        return encoded.toString();
    }

    // Introduce error randomly
    public static String introduceError(String encoded) {
        int errorPosition = (int) (Math.random() * encoded.length());
        char[] received = encoded.toCharArray();
        received[errorPosition] = (received[errorPosition] == '0') ? '1' : '0';
        return new String(received);
    }

    // Correct Hamming code
    public static String correctHamming(String received) {
        int n = received.length() - 1, p = 0;
        StringBuilder parityError = new StringBuilder();

        while (Math.pow(2, p) < n + 1)
            p++;

        for (int i = 0; i < p; i++) {
            int parityIndex = (int) Math.pow(2, i);
            int count = 0;

            for (int bitIndex = parityIndex; bitIndex <= n; bitIndex += parityIndex * 2) {
                for (int l = 0; l < parityIndex && bitIndex + l <= n; l++) {
                    if (received.charAt(bitIndex + l) == '1')
                        count++;
                }
            }

            if (count % 2 != 0)
                parityError.insert(0, "1");
            else
                parityError.insert(0, "0");
        }

        int errorPosition = Integer.parseInt(parityError.toString(), 2);

        if (errorPosition > 0) {
            char[] corrected = received.toCharArray();
            corrected[errorPosition] = (corrected[errorPosition] == '0') ? '1' : '0';
            System.out.println("Error was at Position " + errorPosition);
            return new String(corrected);
        }

        return received;
    }

    // Extract Data Bits
    public static String getDataBits(String encoded) {
        StringBuilder dataBits = new StringBuilder();
        int n = encoded.length();
        for (int i = n - 1; i > 0; i--) {
            if ((i & (i - 1)) != 0)
                dataBits.append(encoded.charAt(i));
        }
        return dataBits.toString();
    }

    // Extract Parity Bits
    public static String getParityBits(String encoded) {
        StringBuilder parityBits = new StringBuilder();
        int n = encoded.length();
        for (int i = 1; i < n; i++) {
            if ((i & (i - 1)) == 0)
                parityBits.append(encoded.charAt(i));
        }
        return parityBits.toString();
    }
}
