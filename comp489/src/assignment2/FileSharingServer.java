/**
 * 
 */
package assignment2;

/**
 * @author omara
 *
 */
//FileSharingServer.java
import assignment2.FileSharingApp.*;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import java.io.*;
import java.net.*;

public class FileSharingServer extends Thread {
    private static ServerSocket serverSocket;
    private static String sharedFolderPath;

    public FileSharingServer(int port, String sharedFolderPath) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.sharedFolderPath = sharedFolderPath;
        this.serverSocket.setSoTimeout(10000); // Optional: Set a timeout for blocking operations
    }
    public static void main(String args[]) {
    
    @Override
    public void run() {
        while(true) {
            try {
            	// Initialize the ORB
                ORB orb = ORB.init(args, null);
                POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
                rootpoa.the_POAManager().activate();

                // Instantiate the servant
                FileSharingImpl fileSharingImpl = new FileSharingImpl();
                fileSharingImpl.setORB(orb);  

                // Get object reference from the servant
                org.omg.CORBA.Object ref = rootpoa.servant_to_reference(fileSharingImpl);
                FileSharing href = FileSharingHelper.narrow(ref);

            	
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                
                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                
                String fileName = in.readUTF();
                File file = new File(sharedFolderPath + File.separator + fileName);
                byte[] bytes = new byte[16 * 1024];
                
                InputStream fileInputStream = new FileInputStream(file);
                OutputStream out = server.getOutputStream();
                
                int count;
                while ((count = fileInputStream.read(bytes)) > 0) {
                    out.write(bytes, 0, count);
                }
                
                server.close();
                fileInputStream.close();
                out.close();
                
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        }
    }
}
