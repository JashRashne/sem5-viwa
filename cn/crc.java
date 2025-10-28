import java.util.Scanner;

public class crc {

    // Perform XOR operation between two binary strings
    static String xor(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i < b.length(); i++) {
            result.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return result.toString();
    }

    // Perform modulo-2 division
    static String mod2div(String dividend, String divisor) {
        int pick = divisor.length();
        String tmp = dividend.substring(0, pick);

        while (pick < dividend.length()) {
            if (tmp.charAt(0) == '1') {
                tmp = xor(divisor, tmp) + dividend.charAt(pick);
            } else {
                tmp = xor("0".repeat(pick), tmp) + dividend.charAt(pick);
            }
            pick++;
        }

        // Last step of division
        if (tmp.charAt(0) == '1') {
            tmp = xor(divisor, tmp);
        } else {
            tmp = xor("0".repeat(pick), tmp);
        }

        return tmp;
    }

    // Encode data using CRC
    static String encodeData(String data, String key) {
        int keylen = key.length();
        String appendedData = data + "0".repeat(keylen - 1);
        String remainder = mod2div(appendedData, key);
        return data + remainder;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter message bits: ");
        String data = sc.next();

        System.out.print("Enter generator polynomial: ");
        String key = sc.next();

        // Encode and transmit data
        String codeword = encodeData(data, key);
        System.out.println("Transmitted Codeword: " + codeword);

        // Receiver side
        System.out.print("Enter received codeword: ");
        String received = sc.next();

        String remainder = mod2div(received, key);

        // Check for errors
        if (Integer.parseInt(remainder) == 0) {
            System.out.println("No Error: Data received correctly!");
        } else {
            System.out.println("Error detected in received data!");
        }

        sc.close();
    }
}
