package poc.backup;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PocProxyServer {
	/**
	 * @param argument PORT is a port number the proxy server will listen to.
	 */
	private static int PORT = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*PORT = Integer.valueOf(args[0]);

		if (PORT == 0) {
			System.out.println("Please provide a valid port number to run the proxy server, e.g. 6000");
			System.exit(1);
		}*/

		// Creating a thread pool to run the server on multiple threads
		ExecutorService executorService = Executors.newCachedThreadPool();

		try (ServerSocket serverSocket = new ServerSocket(6000)) {
			while (true) {
				System.out.println("The proxy server is ready and listening...");
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
		String protocol = "";
		OutputStream output;
		InputStream input;
		byte[] byteArray = new byte[1024];
		try {
			input = socket.getInputStream();
			while (true) {
				Reader in = new InputStreamReader(
						new BufferedInputStream(
						socket.getInputStream()
								)
						);
				// read the first line only; that's all we need
				StringBuilder request = new StringBuilder(80);
				while (true) {
				int c = in.read();
				if (c == '\r' || c == '\n' || c == -1) break;
				request.append((char) c);
				}
				String get = request.toString();
				URL url = new URL(get);
				protocol = url.getProtocol();

				if (protocol.contentEquals("http")) {
					byteArray = handleHttpRequest(url);
					output = socket.getOutputStream();
					output.write(byteArray);
					output.close();
				} else if (protocol == "ftp") {
					byteArray = handleFtpRequest(url);
					output = socket.getOutputStream();
					output.write(byteArray);
					output.close();
				} else {
					String message = "Protocol isn't supported by the proxy server.";
					System.out.println(message);
					output = socket.getOutputStream();
					output.write(message.getBytes());
					output.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static byte[] handleFtpRequest(URL url) {
		// TODO Auto-generated method stub
		byte[] data = null;

		return data;
	}

	private static byte[] handleHttpRequest(URL url) {
		// TODO Auto-generated method stub
		byte[] data = null;
		try {
			// Openning a connection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// Set the HTTP request method (GET in this case)
			connection.setRequestMethod("GET");
			connection.connect();

			int contentLength = connection.getContentLength();
			try (InputStream raw = connection.getInputStream()) {
				InputStream in = new BufferedInputStream(raw);
				data = new byte[contentLength];
				int offset = 0;
				while (offset < contentLength) {
					int bytesRead = in.read(data, offset, data.length - offset);
					if (bytesRead == -1)
						break;
					offset += bytesRead;
				}
				raw.close();
				in.close();
			}
			//connection.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out
					.println("There has been an error while connecting to the requested resource.\n" + e.getMessage());
			e.printStackTrace();
		}
		return data;
	}

}
