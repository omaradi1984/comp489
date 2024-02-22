package assignment2.FileSharingApp;

import assignment2.FileSharingApp.*;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;

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

         /* // Use a temporary socket to a well-known service to discover client's public IP and port
            try (Socket socket = new Socket("google.com", 80)) {
                String clientAddress = socket.getLocalAddress().getHostAddress();
                // It's important to note that the local port obtained here may not be externally reachable or consistent
                int clientPort = socket.getLocalPort();
                
                // Register the file with the obtained details
                System.out.println("Registering file: " + filename);
                fileSharingImpl.registerFile(filename, clientID, clientAddress, String.valueOf(clientPort));
                System.out.println("File registered successfully.");
            }

            System.out.println("Searching for 'testfile'...");
            String[] files = fileSharingImpl.searchFile("testfile");
            for (String file : files) {
                System.out.println("Found: " + file);
            }

            System.out.println("Getting file owner for 'testfile.txt'...");
            String owner = fileSharingImpl.getFileOwner("testfile.txt");
            System.out.println("Owner: " + owner);

            System.out.println("Removing 'testfile.txt'...");
            fileSharingImpl.removeFile("testfile.txt", "client1");

        } catch (Exception e) {
            System.out.println("ERROR : " + e);
            e.printStackTrace(System.out);
        }*/
        private static void registerFile(Scanner scanner) throws Exception {
            System.out.print("Enter filename to register: ");
            String filename = scanner.nextLine();
            String clientID = "client1"; // This can be made dynamic based on your application's requirements

            // Use a temporary socket to a well-known service to discover client's public IP and port
            try (Socket socket = new Socket("google.com", 80)) {
                String clientAddress = socket.getLocalAddress().getHostAddress();
                int clientPort = socket.getLocalPort();
                
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
