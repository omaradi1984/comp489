package assignment2.FileSharingApp;

import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class FileSharingImpl extends FileSharingPOA {
    private ORB orb;
    private HashMap<String, List<String>> fileRegistry = new HashMap<>();
    private HashMap<String, String> fileOwner = new HashMap<>();
    private Connection connection = null;
    private HikariDataSource ds;
    
    public void setORB(ORB orb_val) {
        orb = orb_val;
    }
    
    public FileSharingImpl() {
        try {
        	HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost/FileSharingDB");
            config.setUsername("root");
            config.setPassword("your_password");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            ds = new HikariDataSource(config);
            // Connect to the database
           // String url = "jdbc:mysql://localhost/FileSharingDB?user=root&password=your_password";
           connection = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Registers a file with the server to be shared with others
    public void registerFile(String filename, String clientID, String clientAddress, String clientPort) {
    	String sql = "INSERT INTO files (filename, clientID, clientAddress, clientPort) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, filename);
            stmt.setString(2, clientID);
            stmt.setString(3, clientAddress);
            stmt.setString(4, clientPort);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
    	List<String> fileInfo = new ArrayList<>();
        fileInfo.add(clientID);
        fileInfo.add(clientAddress);
        fileInfo.add(clientPort);
        fileRegistry.put(filename, fileInfo);
        fileOwner.put(filename, clientID);
    }

    // Removes a file from the list of shared files
    public void removeFile(String filename, String clientID) {
        if (fileOwner.get(filename).equals(clientID)) {
            fileRegistry.remove(filename);
            fileOwner.remove(filename);
            String sql = "DELETE FROM files WHERE filename = ? AND clientID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, filename);
                stmt.setString(2, clientID);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Searches for a file by name and returns a list of files matching the query
    public String[] searchFile(String filename) {
    	List<String> fileList = new ArrayList<>();
        String sql = "SELECT filename FROM files WHERE filename LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + filename + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fileList.add(rs.getString("filename"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fileList.toArray(new String[0]);
    	/*
    	if (fileRegistry.containsKey(filename)) {
            List<String> fileInfo = fileRegistry.get(filename);
            String[] fileInfoArray = new String[fileInfo.size()];
            fileInfo.toArray(fileInfoArray);
            return fileInfoArray;
        } else {
            return new String[0]; // Empty array if no match found
        }*/
    }

    // Retrieves the owner information for a specific file
    public String getFileOwner(String filename) {
        //return fileOwner.getOrDefault(filename, "");
    	String sql = "SELECT clientID FROM files WHERE filename = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, filename);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("clientID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    }

public class FileSharingServer {
    public static void main(String args[]) {
        try {
            // Initialize ORB
            ORB orb = ORB.init(args, null);

            // Get reference to root POA & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // Create servant and register it with the ORB
            FileSharingImpl fileSharingImpl = new FileSharingImpl();
            fileSharingImpl.setORB(orb);

            // Get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(fileSharingImpl);
            FileSharing href = FileSharingHelper.narrow(ref);

            // Get the root naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Bind the object reference in naming
            String name = "FileSharing";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);

            System.out.println("FileSharingServer ready and waiting ...");

            // Wait for invocations from clients
            orb.run();
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }

        System.out.println("FileSharingServer Exiting ...");
    }
}
