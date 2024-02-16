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
import java.net.MalformedURLException;
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
		
		byte[] array = new byte[1024];
		int bytesRead = input.read(array);
		
		
		String request = new String(array, 0, bytesRead);
		String firstLine = "";
		boolean httpsFlag = request.contains(":443");
		if(httpsFlag == true)
			firstLine = request.split(" ")[1].split(":")[0];
		else	
			firstLine = request.split(" ")[1];
		System.out.println(firstLine);
		System.out.println(httpsFlag);
		URL url = null;
		try {
			if(httpsFlag == true)
				url = new URL("https://"+firstLine);
			url = new URL(firstLine);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			OutputStream out = clientSocket.getOutputStream();
			out.write("Unknown protocol".getBytes());
			e.printStackTrace();
		}
		String host = url.getHost();
		int port = url.getPort();
		
		InputStream stream = new ByteArrayInputStream(request.getBytes
                (Charset.forName("UTF-8")));
		
		objectArray[0] = stream;
		objectArray[1] = host;
		
		if(port == -1)
			if(httpsFlag == true)
				port = 443;
			else
				port = 80;
		System.out.println(port);
		objectArray[2] = port;
		
		return objectArray;
	}
}