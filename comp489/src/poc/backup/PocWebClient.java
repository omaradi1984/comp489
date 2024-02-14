package poc.backup;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class PocWebClient {
	/**
	 * @param arguments
	 */
	private static String URL = "", FILE_PATH = "";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		URL = "https://www.aljazeera.net/";
				
		try {
			URL url = new URL(URL);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("GET");
			connection.connect();
			
			Map<String, List<String>> map = connection.getHeaderFields();
			
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			    String key = entry.getKey();
			    Object value = entry.getValue();

			    System.out.println(key + ": " + value);
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

/*
 * if (args.length < 3) { System.out.
 * println("Invalid execution! Please run using the following arguments:\n" +
 * "proxy address (mandatory)" + "proxy port (mandatory)" +
 * "host address (mandatory)" +
 * "file path (optional to download requested resource, none html or txt types, to a specific directory"
 * + ". Otherwise, the application will save the file to its directory)\n" +
 * "Example: java pocwebclient.java 192.168.0.10 6000 http://example.come/sample.txt c://myfolder//"
 * ); }
 * 
 * // Set variables PROXY_ADDRESS = args[0]; // Proxy server address PROXY_PORT
 * = args[1]; // Proxy port URL = args[2]; // The address to the requested
 * resource in the web if (args.length == 4) { FILE_PATH = args[3]; // Directory
 * where the requested file will be downloaded }
 * 
 * // Validate user input validateInput(URL, PROXY_ADDRESS, PROXY_PORT);
 * 
 * // Send request processRequest(URL, PROXY_ADDRESS, PROXY_PORT); }
 * 
 * private static void processRequest(String URL, String PROXY_ADDRESS, String
 * PROXY_PORT) { // TODO Auto-generated method stub InputStream input;
 * OutputStream output; try { // create socket and connect to proxy server
 * Socket socket = new Socket(PROXY_ADDRESS, Integer.valueOf(PROXY_PORT));
 * System.out.println("Connected to proxy server at " + PROXY_ADDRESS + ":" +
 * PROXY_PORT);
 * 
 * output = socket.getOutputStream();
 * 
 * System.out.println("Sending request for " + URL +
 * "\nWaiting for response..."); // Sending request
 * output.write(URL.getBytes());
 * 
 * // Checking if resource type is html or txt if
 * ((URL.substring(URL.lastIndexOf(".")) == "html" ||
 * URL.substring(URL.lastIndexOf(".")) == "txt") && FILE_PATH == null) { while
 * (true) { int num = 0; input = socket.getInputStream(); byte[] bArry = new
 * byte[1024]; num = input.read(bArry); String response = new String(bArry, 0,
 * num); System.out.println("The response is:\n" + response); // closing stream
 * input.close(); socket.close(); } } else if
 * (URL.substring(URL.lastIndexOf(".")) != "html" &&
 * URL.substring(URL.lastIndexOf(".")) != "txt" && FILE_PATH != null) { input =
 * new BufferedInputStream(socket.getInputStream(), 8192); if
 * (Files.exists(Paths.get(FILE_PATH))) { String fileName = FILE_PATH +
 * UUID.randomUUID() + URL.substring(URL.lastIndexOf("/") + 1); FileOutputStream
 * fileOutput = new FileOutputStream(fileName); byte[] data = new byte[1024];
 * int count; while ((count = input.read(data)) != -1) { // writing data to
 * client fileOutput.write(data, 0, count); } // flushing output
 * fileOutput.flush(); // closing streams fileOutput.close();
 * System.out.println("File downloaded successfully:\n" + fileName);
 * socket.close(); } else { String fileName = "./" + UUID.randomUUID() +
 * URL.substring(URL.lastIndexOf("/") + 1); FileOutputStream fileOutput = new
 * FileOutputStream(fileName); byte[] data = new byte[1024]; int count; while
 * ((count = input.read(data)) != -1) { // writing data to client
 * fileOutput.write(data, 0, count); } // flushing output fileOutput.flush(); //
 * closing streams fileOutput.close();
 * System.out.println("File downloaded successfully:\n" + fileName);
 * socket.close(); } } } catch (NumberFormatException |
 * 
 * IOException e) { // TODO Auto-generated catch block System.out.
 * println("There has been an error, please try again!\nError message: " +
 * e.getMessage()); e.printStackTrace(); System.exit(1); } }
 * 
 * private static void validateInput(String URL, String PROXY_ADDRESS, String
 * PROXY_PORT) { // TODO Auto-generated method stub
 * 
 * if (PROXY_ADDRESS.length() == 0 || PROXY_ADDRESS.split("[.]").length != 4) {
 * System.out.println("Please provide a valid proxy address, e.g. 192.168.10.1."
 * ); System.exit(1); }
 * 
 * // Validate proxy port if (PROXY_PORT.length() == 0 ||
 * !(Integer.valueOf(PROXY_PORT) instanceof Integer)) {
 * System.out.println("Please provide a valid port number, e.g. 6000.");
 * System.exit(1); } // Validate host address try { if (URL.length() == 0 ||
 * !((new URL(URL)) instanceof URL)) { System.out.
 * println("Please provide a valid host address, e.g.  http://127.0.0.1:8000/testfile.html."
 * ); System.exit(1); } } catch (MalformedURLException e) { // TODO
 * Auto-generated catch block System.out.
 * println("Please provide a valid host address, e.g. http://127.0.0.1:8000/testfile.html."
 * ); System.exit(1); } }
 * 
 * }
 */
