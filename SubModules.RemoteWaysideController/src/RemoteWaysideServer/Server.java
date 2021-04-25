package RemoteWaysideServer;

import java.util.Scanner;

/** simple Main class for hosting a server on a machine for testing purposes.
 *  creates a RemoteWaysideService for hosting a remote WaysideController and makes it available in the machine's RMI Registry.
 */
public class Server {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String IPAddress = "";
        int communicationPort = 0;

        try {
            System.out.printf("Enter your IP Address: ");
            IPAddress = in.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid IP Address input (must be of format X.X.X.X)");
        }
        try {
            System.out.printf("Enter an RMI Registry port: ");
            communicationPort = in.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid RMI Registry port (must be an integer value, port 0 indicates to use the first available port)");
        }

        RemoteWaysideServer server = new RemoteWaysideServer(IPAddress,communicationPort);
    }
}
