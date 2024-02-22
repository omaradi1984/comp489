package assignment2.FileSharingApp;

import java.net.Socket;
import java.util.Scanner;

import org.omg.CORBA.*;
import org.omg.CosNaming.*;

public class FileSharingClient {
    static FileSharing fileSharingImpl;

    public static void main(String args[]) {
        try {
            // Initialize the ORB
            ORB orb = ORB.init(args, null);
            // Get the root naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Resolve the Object Reference in Naming
            String name = "FileSharing";
            fileSharingImpl = FileSharingHelper.narrow(ncRef.resolve_str(name));

         // Display menu and handle user input
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nAvailable Operations:");
                System.out.println("1. Register File");
                System.out.println("2. Remove File");
                System.out.println("3. Search File");
                System.out.println("4. Get File Owner");
                System.out.println("5. Exit");
                System.out.print("Select an operation (1-5): ");
                String clientID = "123456";
                int choice = scanner.nextInt();
                
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        registerFile(scanner);
                        break;
                    case 2:
                        removeFile(scanner);
                        break;
                    case 3:
                        searchFile(scanner);
                        break;
                    case 4:
                        getFileOwner(scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please select a valid operation.");
                        System.exit(1);
                }
            }
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
    }
        private static void registerFile(Scanner scanner) throws Exception {
            System.out.print("Enter filename to register: ");
            String filename = scanner.nextLine();
            
            // Use a temporary socket to a well-known service to discover client's public IP and port
            try (Socket socket = new Socket("google.com", 80)) {
                String clientAddress = socket.getLocalAddress().getHostAddress();
                int clientPort = socket.getLocalPort();
                String clientID = socket.getLocalAddress().getHostAddress()+":"+socket.getLocalPort();
                fileSharingImpl.registerFile(filename, clientID, clientAddress, String.valueOf(clientPort));
                System.out.println("File registered successfully.");
            }
        }

        private static void removeFile(Scanner scanner) {
            System.out.print("Enter filename to remove: ");
            String filename = scanner.nextLine();
            String clientID = "client1"; // This can be assumed or input by the user
            fileSharingImpl.removeFile(filename, clientID);
            System.out.println("File removed successfully.");
        }

        private static void searchFile(Scanner scanner) {
            System.out.print("Enter filename to search: ");
            String filename = scanner.nextLine();
            String[] files = fileSharingImpl.searchFile(filename);
            System.out.println("Search results:");
            for (String file : files) {
                System.out.println(file);
            }
        }

        private static void getFileOwner(Scanner scanner) {
            System.out.print("Enter filename to get owner: ");
            String filename = scanner.nextLine();
            String owner = fileSharingImpl.getFileOwner(filename);
            System.out.println("File owner: " + owner);
        }
}
