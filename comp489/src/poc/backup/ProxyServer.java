package poc.backup;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProxyServer {

	protected static int PORT;

    public static void main(String[] args) throws IOException {

    	if(args.length == 0) {
    		System.out.println("Please provide a valid port number and number of threads for the proxy server.");
    		System.exit(1);
    	}

    	PORT = Integer.parseInt(args[0]);

    	ExecutorService executorService = Executors.newCachedThreadPool();
    	try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			while(true) {
				System.out.println("The proxy server is ready and listening...");
				final Socket socket = serverSocket.accept();
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						RedirectRequest(socket);
						}
					}
				);
			}
		}
    }

    protected static void RedirectRequest(Socket socket) {
		// TODO Auto-generated method stub
    	//InputStream in;
		try {
			Writer out = new BufferedWriter(
					new OutputStreamWriter(
					socket.getOutputStream(), "US-ASCII"
					)
					);
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
			String[] pieces = get.split("\\w*");
			String theFile = pieces[1];
			/*in = socket.getInputStream();
			int num = 0;
			byte[] bArry = new byte[1024];
			num = in.read(bArry);
			String clientRequest = new String(bArry, 0, num);*/

	    	System.out.println("Client is requesting the following page/resource: " + get);

	    	/*if("HTTP".equalsIgnoreCase(requestType)) {
	    		sendHttpRequest(socket, requestedResource);
	    	}else {
	    		//sendFtpRequest(socket, requestedResource);
	    	}*/

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    protected static void processRequest(Socket socket) {
		// TODO Auto-generated method stub
    	InputStream in;
		try {
			in = socket.getInputStream();
			int num = 0;
			byte[] bArry = new byte[1024];
			num = in.read(bArry);
			String clientRequest = new String(bArry, 0, num);

	    	System.out.println("Client is requesting the following page/resource: " + clientRequest);

	    	/*if("HTTP".equalsIgnoreCase(requestType)) {
	    		sendHttpRequest(socket, requestedResource);
	    	}else {
	    		//sendFtpRequest(socket, requestedResource);
	    	}*/

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void sendHttpRequest(Socket socket, String requestedResource) {
		// TODO Auto-generated method stub
		InputStream input = null;
		OutputStream out = null;
		try {
			URL url = new URL(requestedResource);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.connect();

	        out = socket.getOutputStream();
	        out.write("Proxy server forwarded message to http server".getBytes());

	     // Check for successful response code or send error
	        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	            String response = "Error, server returned HTTP " + conn.getResponseCode()
	            + " " + conn.getResponseMessage();
	            System.out.println(response);
	            out = socket.getOutputStream();
	        	out.write(response.toString().getBytes());
	        }else {
	        	String response = "Server returned HTTP " + conn.getResponseCode()
	            + " " + conn.getResponseMessage();
	        	System.out.println(response);

	        	if(url.getPath().toString().contains(".html|.htm|/")) {
	        		input = conn.getInputStream();
		        	out = socket.getOutputStream();
		        	//out.write(response.toString().getBytes());
		        	out.write(input.readAllBytes());
	        	}else {

	        		// Input stream to read file - with 8k buffer
	            input = new BufferedInputStream(conn.getInputStream(), 8192);

	            // Output stream to send file to client
	            out = socket.getOutputStream();

	            byte data[] = new byte[1024];
	            int count;
	            while ((count = input.read(data)) != -1) {
	                // writing data to client
	                out.write(data, 0, count);
	            }

	        }conn.disconnect();
	        }
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				input.close();
				//socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}