package assignment1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HybridProxyServer {
	/**
	 * @param argument
	 *            PORT is a port number the proxy server will listen to. Address
	 *            is the IP address that the proxy server should listen to, if
	 *            not defined, then server will listen to IP addresses of all
	 *            interfaces.
	 */
	private static int PORT = 0;
	private static String ADDRESS = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args.length == 0) {
			System.out.println(
					"Please provide a valid port number (mandatory) and if applicable IP address to run the proxy server, "
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
				System.exit(1);
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
			//Object[] array = new Object[3];
			Object[] array = null;
			try {
				array = requestProcessor(clientSocket);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			InputStream in = (InputStream) array[0];
			String destinationHost =  (String) array[1];
			int destinationPort = (int) array[2];
			
			
			try (Socket serverSocket = new Socket(destinationHost, destinationPort);
					InputStream fromClient = in;//clientSocket.getInputStream();//(InputStream) array[0];
					OutputStream toClient = clientSocket.getOutputStream();
					InputStream fromServer = serverSocket.getInputStream();
					OutputStream toServer = serverSocket.getOutputStream();) {
				
				// Forward client to server
				Thread clientToServerThread = new Thread(
						() -> forwardData(fromClient, toServer));
				clientToServerThread.start();

				// Forward server to client
				forwardData(fromServer, toClient);

			} catch (Exception e) {
				e.printStackTrace();
			} 
		}

		private void forwardData(InputStream input, OutputStream output) {
			byte[] buffer = new byte[4096];
			int read;

			try {
				while ((read = input.read(buffer)) != -1) {
					output.write(buffer, 0, read);
					output.flush();
				}
			} catch (Exception e) {
				// Connection might be closed, ignore error
			}
		}

	}
	public static Object[] requestProcessor(Socket clientSocket) throws IOException {
		// TODO Auto-generated method stub
		Object[] objectArray = new Object[3];
		InputStream input = clientSocket.getInputStream();
		DataInputStream in = new DataInputStream(input);
		byte[] array = new byte[in.readInt()];
		int bytesRead = input.read(array);
		
		
		String request = new String(array, 0, bytesRead);
		String firstLine = request.split(" ")[1];
		URL url = new URL(firstLine);
		String host = url.getHost();
		int port = url.getPort();
		
		System.out.println("request: " + request);
		System.out.println("host: " + host);
		System.out.println("port: " + port);
		
		InputStream stream = new ByteArrayInputStream(request.getBytes
                (Charset.forName("UTF-8")));
		
		objectArray[0] = stream;
		objectArray[1] = host;
		
		if(port == -1)
			port = 80;
		objectArray[2] = port;
		
		return objectArray;
	}
}