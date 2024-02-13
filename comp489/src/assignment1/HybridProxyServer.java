package assignment1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class HybridProxyServer {
	/**
	 * @param argument
	 *       this is a comment     PORT is a port number the proxy server will listen to.
	 */
	private static int PORT = 0;
	private static String ADDRESS = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args.length == 0) {
			System.out.println(
					"Please provide a valid port number (mandatory) and host address (optional) to run the proxy server, "
							+ "e.g. java hybridproxyserver.java 6000 192.168.0.2");
			System.exit(1);
		}

		if (args.length == 1) {
			PORT = Integer.valueOf(args[0]);
			try {
				ServerSocket serverSocket = new ServerSocket(PORT);
				System.out.println("Server started and listening on "
						+ serverSocket.getLocalSocketAddress());
				while (true) {
	                Socket clientSocket = serverSocket.accept();
	                // Handle client connection in a separate thread
	                new Thread(new ClientHandler(clientSocket)).start();
	            }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (args.length == 2) {
			PORT = Integer.valueOf(args[0]);
			ADDRESS = args[1];
			try {
				ServerSocket serverSocket = new ServerSocket(PORT, 100,
						InetAddress.getByAddress(ADDRESS.getBytes()));
				System.out.println("Server started and listening on "
						+ serverSocket.getLocalSocketAddress());
				while (true) {
	                Socket clientSocket = serverSocket.accept();
	                // Handle client connection in a separate thread
	                new Thread(new ClientHandler(clientSocket)).start();
	            }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(
						"There has been an error while trying to start the proxy server\n"
								+ "Please try again.\n" + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream clientOutput = clientSocket.getOutputStream();
                String requestLine = reader.readLine();
                System.out.println(requestLine);
                // Only handling simple GET requests for this example
                if (requestLine != null && requestLine.startsWith("GET")) {
                    // Extract URL from the GET request
                    String url = requestLine.split(" ")[1];
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

                    // Forward the client's request to the server
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();
                    String contentType = connection.getContentType();
                    System.out.println(responseCode + " " + contentType);
                    String responseMessage = connection.getResponseMessage();
                    InputStream serverInput = connection.getInputStream();

                    // Send the server's response back to the client
                    clientOutput.write(("HTTP/1.1 " + responseCode + " \r\n").getBytes());
                    
                    clientOutput.write(("\r\n").getBytes());
                    byte[] buffer = new byte[4096];
                    int read;
                    while ((read = serverInput.read(buffer)) != -1) {
                        clientOutput.write(buffer, 0, read);
                        clientOutput.flush();
                    }

                    serverInput.close();
                }

                clientOutput.close();
                reader.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/*
 * try (ServerSocket serverSocket = new ServerSocket(PORT)) {
 * System.out.println("Server started and listening on " +
 * serverSocket.getLocalSocketAddress());
 * 
 * // Creating a thread pool to run the server on multiple threads
 * ExecutorService executorService = Executors.newCachedThreadPool();
 * 
 * while (true) { final Socket socket = serverSocket.accept();
 * 
 * executorService.execute(new Runnable() {
 * 
 * @Override public void run() { // TODO Auto-generated method stub
 * processRequest(socket); } }); } } catch (IOException e) { // TODO
 * Auto-generated catch block System.out.
 * println("There has been an error while trying to start the proxy server\n" +
 * "Please try again.\n" + e.getMessage()); e.printStackTrace(); } }
 * 
 * protected static void processRequest(Socket socket) { // TODO Auto-generated
 * method stub try { InputStream input = socket.getInputStream(); OutputStream
 * output = socket.getOutputStream();
 * 
 * int num = 0; byte[] bArry = new byte[1024]; num = input.read(bArry); String
 * message = new String(bArry, 0, num);
 * 
 * System.out.println("Message from client:\n" + message);
 * 
 * URL url = new URL(message);
 * 
 * String protocol = url.getProtocol();
 * 
 * byte[] byteArray = null;
 * 
 * if (protocol.contentEquals("http")) { byteArray = handleHttpRequest(url);
 * System.out.println("Sending response to client."); output.write(byteArray);
 * System.out.println("Server closing connection."); input.close();
 * output.close(); }
 * 
 * else if (protocol.contentEquals("ftp")) { byteArray = handleFtpRequest(url);
 * output.write(byteArray); System.out.println("Server closing connection.");
 * input.close(); output.close(); }
 * 
 * else { String response = "Protocol isn't supported by the proxy server.";
 * System.out.println(response); output = socket.getOutputStream();
 * output.write(response.getBytes());
 * System.out.println("Server closing connection."); input.close();
 * output.close(); } socket.close(); } catch (IOException e) { // TODO
 * Auto-generated catch block System.out.
 * println("There has been an error while trying to start the proxy server\n" +
 * "Please try again.\n" + e.getMessage()); e.printStackTrace(); } }
 * 
 * private static byte[] handleFtpRequest(URL url) { // TODO Auto-generated
 * method stub byte[] data = null;
 * 
 * return null; }
 * 
 * private static byte[] handleHttpRequest(URL url) throws IOException { // TODO
 * Auto-generated method stub byte[] data = null; HttpURLConnection connection =
 * null; try { connection = (HttpURLConnection) url.openConnection();
 * connection.setRequestMethod("GET");
 * System.out.println("Redirecting request to the destination server.");
 * connection.connect();
 * 
 * int contentLength = connection.getContentLength(); try (InputStream raw =
 * connection.getInputStream()) { InputStream in = new BufferedInputStream(raw);
 * data = new byte[8192]; int offset = 0; while (offset < 8192) { int bytesRead
 * = in.read(data, offset, data.length - offset); if (bytesRead == -1) break;
 * offset += bytesRead; } } catch (IOException e) { data = new
 * String(connection.getResponseCode() +"\n"+
 * connection.getResponseMessage()).getBytes(); } connection.disconnect(); }
 * catch (IOException e) { data = new String(connection.getResponseCode() +"\n"+
 * connection.getResponseMessage()).getBytes(); } return data; }
 * 
 * }
 */
