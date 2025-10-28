import java.util.*;

public class subnetting {

    // Determine class of the IP address
    static char getClass(int firstOctet) {
        if (firstOctet >= 0 && firstOctet <= 127)
            return 'A';
        else if (firstOctet <= 191)
            return 'B';
        else if (firstOctet <= 223)
            return 'C';
        else if (firstOctet <= 239)
            return 'D';
        else
            return 'E';
    }

    // Convert IP string to list of integers
    static List<Integer> getAddress(String str) {
        List<Integer> addr = new ArrayList<>();
        String[] parts = str.split("\\.");
        for (String p : parts) {
            addr.add(Integer.parseInt(p));
        }
        return addr;
    }

    // Get default subnet mask based on class
    static String getNetMask(char netClass) {
        switch (netClass) {
            case 'A': return "255.0.0.0";
            case 'B': return "255.255.0.0";
            case 'C': return "255.255.255.0";
            default: return "N/A";
        }
    }

    // Get total number of IPs per class
    static int getDefaultIPCount(char netClass) {
        switch (netClass) {
            case 'A': return (int) Math.pow(2, 24);
            case 'B': return (int) Math.pow(2, 16);
            case 'C': return 256;
            default: return 0;
        }
    }

    // Print subnetting details
    static void printSubnetInfo(List<Integer> addr, int totalIPs, int subnets) {
        int ipsPerSubnet = totalIPs / subnets;
        int usableIPsPerSubnet = ipsPerSubnet - 2;

        System.out.println("Total IP addresses per subnet: " + ipsPerSubnet);
        System.out.println("Usable IP addresses per subnet: " + usableIPsPerSubnet);

        for (int i = 0; i < subnets; i++) {
            int subnetStart = i * ipsPerSubnet;
            int subnetEnd = subnetStart + ipsPerSubnet - 1;

            List<Integer> subnetAddr = new ArrayList<>(addr);
            List<Integer> broadcastAddr = new ArrayList<>(addr);

            // For Class C
            if (totalIPs == 256) {
                subnetAddr.set(3, subnetStart);
                broadcastAddr.set(3, subnetEnd);
            }
            // For Class B
            else if (totalIPs == (int) Math.pow(2, 16)) {
                subnetAddr.set(2, subnetStart / 256);
                subnetAddr.set(3, subnetStart % 256);
                broadcastAddr.set(2, subnetEnd / 256);
                broadcastAddr.set(3, subnetEnd % 256);
            }
            // For Class A
            else if (totalIPs == (int) Math.pow(2, 24)) {
                subnetAddr.set(1, subnetStart / (256 * 256));
                subnetAddr.set(2, (subnetStart / 256) % 256);
                subnetAddr.set(3, subnetStart % 256);
                broadcastAddr.set(1, subnetEnd / (256 * 256));
                broadcastAddr.set(2, (subnetEnd / 256) % 256);
                broadcastAddr.set(3, subnetEnd % 256);
            }

            System.out.println("\nSubnet " + (i + 1) + " Address: "
                    + subnetAddr.get(0) + "." + subnetAddr.get(1) + "."
                    + subnetAddr.get(2) + "." + subnetAddr.get(3));

            System.out.println("Broadcast Address: "
                    + broadcastAddr.get(0) + "." + broadcastAddr.get(1) + "."
                    + broadcastAddr.get(2) + "." + broadcastAddr.get(3));

            System.out.println("Range of usable IPs: "
                    + subnetAddr.get(0) + "." + subnetAddr.get(1) + "."
                    + subnetAddr.get(2) + "." + (subnetAddr.get(3) + 1)
                    + " - " + broadcastAddr.get(0) + "." + broadcastAddr.get(1)
                    + "." + broadcastAddr.get(2) + "." + (broadcastAddr.get(3) - 1));
        }
    }

    // Calculate subnet mask for given number of subnets
    static String calculateSubnetMask(char netClass, int subnets) {
        int totalHostBits;
        int borrowedBits = (int) Math.ceil(Math.log(subnets) / Math.log(2));

        switch (netClass) {
            case 'A': totalHostBits = 24; break;
            case 'B': totalHostBits = 16; break;
            case 'C': totalHostBits = 8; break;
            default: return "N/A";
        }

        int remainingHostBits = totalHostBits - borrowedBits;
        int maskBits = 32 - remainingHostBits;

        StringBuilder subnetMask = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            if (maskBits >= 8) {
                subnetMask.append("255");
                maskBits -= 8;
            } else if (maskBits > 0) {
                subnetMask.append(256 - (int) Math.pow(2, 8 - maskBits));
                maskBits = 0;
            } else {
                subnetMask.append("0");
            }
            if (i < 3) subnetMask.append(".");
        }

        return subnetMask.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the IPv4 address: ");
        String str = sc.nextLine();

        List<Integer> addr = getAddress(str);
        char netClass = getClass(addr.get(0));

        if (netClass == 'D' || netClass == 'E') {
            System.out.println("Class " + netClass + " is not suitable for subnetting.");
            return;
        }

        System.out.println("Class: " + netClass);
        System.out.println("Network ID: " + addr.get(0) + "." + addr.get(1) + "." + addr.get(2));
        System.out.println("Default Subnet Mask: " + getNetMask(netClass));

        System.out.print("Enter the number of subnets: ");
        int subnets = sc.nextInt();

        int totalIPs = getDefaultIPCount(netClass);
        printSubnetInfo(addr, totalIPs, subnets);

        String newSubnetMask = calculateSubnetMask(netClass, subnets);
        System.out.println("\nNew Subnet Mask: " + newSubnetMask);

        sc.close();
    }
}
