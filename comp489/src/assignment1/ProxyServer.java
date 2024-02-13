package assignment1;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProxyServer {
	/**
	 * @param argument PORT is a port number the proxy server will listen to.
	 */
	private static int PORT = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args[0].length() == 0) {
			System.out.println("Please provide a valid port number to run the proxy server, e.g. 6000");
			System.exit(1);
		}

		PORT = Integer.valueOf(args[0]);

		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("Server started and listening on " + serverSocket.getLocalSocketAddress());

			// Creating a thread pool to run the server on multiple threads
			ExecutorService executorService = Executors.newCachedThreadPool();

			while (true) {
				final Socket socket = serverSocket.accept();

				executorService.execute(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						processRequest(socket);
					}
				});
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("There has been an error while trying to start the proxy server\n"
					+ "Please try again.\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	protected static void processRequest(Socket socket) {
		// TODO Auto-generated method stub
		try {
			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();

			int num = 0;
			byte[] bArry = new byte[1024];
			num = input.read(bArry);
			String message = new String(bArry, 0, num);

			System.out.println("Message from client:\n" + message);

			URL url = new URL(message);

			String protocol = url.getProtocol();

			byte[] byteArray = null;

			if (protocol.contentEquals("http")) {
				byteArray = handleHttpRequest(url);
				System.out.println("Sending response to client.");
				output.write(byteArray);
				System.out.println("Server closing connection.");
				input.close();
				output.close();
			}

			else if (protocol.contentEquals("ftp")) {
				byteArray = handleFtpRequest(url);
				output.write(byteArray);
				System.out.println("Server closing connection.");
				input.close();
				output.close();
			}

			else {
				String response = "Protocol isn't supported by the proxy server.";
				System.out.println(response);
				output = socket.getOutputStream();
				output.write(response.getBytes());
				System.out.println("Server closing connection.");
				input.close();
				output.close();
			}
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("There has been an error while trying to start the proxy server\n"
					+ "Please try again.\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	private static byte[] handleFtpRequest(URL url) {
		// TODO Auto-generated method stub
		byte[] data = null;

		return null;
	}

	private static byte[] handleHttpRequest(URL url) throws IOException {
		// TODO Auto-generated method stub
		byte[] data = null;
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			System.out.println("Redirecting request to the destination server.");
			connection.connect();

			int contentLength = connection.getContentLength();
			try (InputStream raw = connection.getInputStream()) {
				InputStream in = new BufferedInputStream(raw);
				data = new byte[8192];
				int offset = 0;
				while (offset < 8192) {
					int bytesRead = in.read(data, offset, data.length - offset);
					if (bytesRead == -1)
						break;
					offset += bytesRead;
				}
			} catch (IOException e) {
				data = new String(connection.getResponseCode() +"\n"+ connection.getResponseMessage()).getBytes();
			}
			connection.disconnect();
		} catch (IOException e) {
			data = new String(connection.getResponseCode() +"\n"+ connection.getResponseMessage()).getBytes();
		}
		return data;
	}

}
