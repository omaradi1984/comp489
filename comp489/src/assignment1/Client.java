package assignment1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class Client {
	/**
	 * @param arguments
	 */
	private static String URL = "", PROXY_ADDRESS = "", PROXY_PORT = "", FILE_PATH = null;
	//this is s test
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//This is a new comment
		if (args.length < 3) {
			System.out.println("Please provide the minimum required arguments\nProxy server address, "
					+ "proxy server port, and URL to desired resource"
					+ ".\n(Optional) path to the directory where you want the resource to be saved.\n"
					+ "For example: java MyWebClient.java <proxy address> <proxy port> <resource URL> (optional) <directory path>");
			System.exit(1);
		}

		// Set variables
		PROXY_ADDRESS = args[0]; // Proxy server address
		PROXY_PORT = args[1]; // Proxy port
		URL = args[2]; // The address to the requested resource in the web
		if (args.length == 4) {
			FILE_PATH = args[3]; // Directory where the requested file will be downloaded
		}

		// Validate user input
		validateInput(URL, PROXY_ADDRESS, PROXY_PORT);

		// Process request
		processRequest(URL, PROXY_ADDRESS, PROXY_PORT, FILE_PATH);
	}

	private static void processRequest(String URL, String PROXY_ADDRESS, String PROXY_PORT, String FILE_PATH) {
		// TODO Auto-generated method stub
		try {
			Socket socket = new Socket(PROXY_ADDRESS, Integer.valueOf(PROXY_PORT));
			System.out.println("Connected to proxy server at " + socket.getInetAddress() + ":" + socket.getPort());

			OutputStream output = socket.getOutputStream();
			InputStream input = socket.getInputStream();

			System.out.println("Sending request for " + URL + "\nWaiting for response...");

			output.write(URL.getBytes());

			if((URL.substring(URL.lastIndexOf(".")) == "html" || URL.substring(URL.lastIndexOf(".")) == "txt") && FILE_PATH == null) {

				byte[] array = input.readAllBytes();

				String response = new String(array);

				System.out.println("Response:\n" + response);

				input.close();
				output.close();
				socket.close();
			}
			else if(FILE_PATH != null && Files.isDirectory(Paths.get(FILE_PATH)) && Files.isWritable(Paths.get(FILE_PATH))) {
					String filePath = FILE_PATH + UUID.randomUUID() + URL.substring(URL.lastIndexOf("/") + 1);
					File file = new File(filePath);

					try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
			            int read;
			            byte[] bytes = new byte[8192];
			            while ((read = input.read(bytes)) != -1) {
			                outputStream.write(bytes, 0, read);
			            }
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if(URL.substring(URL.lastIndexOf(".")) != "html" && URL.substring(URL.lastIndexOf(".")) != "txt") {
				String filePath = "./" + UUID.randomUUID() + URL.substring(URL.lastIndexOf("/") + 1);
				File file = new File(filePath);

				try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
		            int read;
		            byte[] bytes = new byte[8192];
		            while ((read = input.read(bytes)) != -1) {
		                outputStream.write(bytes, 0, read);
		            }
				System.out.println(
								"File downloaded successfully: "
										+ file);
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	}
	}catch(IOException e) {
		e.printStackTrace();
	}
	}

	private static void validateInput(String URL, String PROXY_ADDRESS, String PROXY_PORT) {
		// TODO Auto-generated method stub

		if (PROXY_ADDRESS.length() == 0 || PROXY_ADDRESS.split("[.]").length != 4) {
			System.out.println("Please provide a valid proxy address, e.g. 192.168.10.1.");
			System.exit(1);
		}

		// Validate proxy port
		if (PROXY_PORT.length() == 0 || !(Integer.valueOf(PROXY_PORT) instanceof Integer)) {
			System.out.println("Please provide a valid port number, e.g. 6000.");
			System.exit(1);
		}
		// Validate host address
		try {
			if (URL.length() == 0 || !((new URL(URL)) instanceof URL)) {
				System.out.println("Please provide a valid url, e.g.  http://127.0.0.1:8000/testfile.html.");
				System.exit(1);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("Please provide a valid url, e.g. http://127.0.0.1:8000/testfile.html.");
			System.exit(1);
		}
	}

}
