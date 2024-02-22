package assignment2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.ORB;

import assignment2.FileSharingApp.FileSharingPOA;

class FileSharingImpl extends FileSharingPOA {
	 private ORB orb;
	 private Map<String, List<String>> fileMap = new HashMap<>();

	 public void setORB(ORB orb_val) {
	     orb = orb_val;
	 }

	// Inside FileSharingImpl class
	 private Map<String, String> fileOwnerMap = new HashMap<>(); // Maps filename to clientID
	 private Map<String, String[]> clientInfoMap = new HashMap<>(); // Maps clientID to [clientAddress, clientPort]

	 public void registerFile(String filename, String clientID, String clientAddress, String clientPort) {
	     fileOwnerMap.put(filename, clientID);
	     clientInfoMap.put(clientID, new String[]{clientAddress, clientPort});
	     // Additional implementation...
	 }
	 
	 public void removeFile(String filename, String clientID) {
	        fileOwnerMap.remove(filename);
	    }
	 
	 public String[] searchFile(String filename) {
	        // Implementation to search and return matching filenames
		 BufferedReader reader = null;
	        List<String> results = new ArrayList<>();

	        try {
	            reader = new BufferedReader(new FileReader("./filelist.txt"));
	            String line;

	            while ((line = reader.readLine()) != null) {
	                // Check if the current line (file name) contains the search term
	                // This example performs a case-insensitive contains check
	                if (line.toLowerCase().contains(filename.toLowerCase())) {
	                    results.add(line);
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
		    // Add matching filenames to results
		    return results.toArray(new String[0]); // Convert List to String array for return
		}

	 public String getFileOwnerInfo(String filename) {
	     String clientID = fileOwnerMap.get(filename);
	     if (clientID != null && clientInfoMap.containsKey(clientID)) {
	         String[] info = clientInfoMap.get(clientID);
	         return info[0] + ":" + info[1]; // clientAddress:clientPort
	     }
	     return "";
	 }
	 
	}