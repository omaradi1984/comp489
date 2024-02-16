package assignment1.poc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Socket socket = null;
		try (ServerSocket server = new ServerSocket(9000);) {

			while (true) {
				System.out.println(
						"Server listening");
				socket = server.accept();
				
				InputStream fromClient = socket.getInputStream();
				OutputStream toClient = socket.getOutputStream();

				
				byte[] buffer = new byte[4096];
				int read;
				String line;
				StringBuilder request = new StringBuilder();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								socket.getInputStream()));
				while ((line = reader.readLine()) != null) {
					request.append(line);
					}

				System.out.println(
						"Http Client says: " + request);

				String response = "HTTP/1.1 200 OK\r\n"
						+ "Date: Mon, 27 Jul 2009 12:28:53 GMT\r\n"
						+ "Server: Apache/2.2.14 (Win32)\r\n"
						+ "Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT\r\n"
						+ "Content-Length: 88\r\n"
						+ "Content-Type: text/html\r\n"
						+ "Connection: Closed";
				System.out.println(
						response);
				toClient.write(response.getBytes());

			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			socket.close();
		}

	}

}
