package poc.backup;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.UUID;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		Socket socket = null;
		Scanner sc = new Scanner(System.in);
		try {
			System.out.println("Please provide the IP address of the proxy server.");
			String ipAddress = sc.nextLine();

			System.out.println("Please provide a valid port number for the proxy server.");
			int port = Integer.valueOf(sc.nextLine());

			socket = new Socket(ipAddress, port);
			System.out.println("Connection made to proxy server on: " + ipAddress+":"+port);

			System.out.println("Please provide the type of the request, 'http' or 'ftp'.");
			String requestType = sc.nextLine();

			System.out.println("Please provide the link to the desired resource.");
			String request = sc.nextLine();

			System.out.println("Is this a link to a resource, e.g. image, audio, video...etc.? Type 'Yes' or 'No'");
			String isResource = sc.nextLine();

			String filePath = null;
			if(isResource.equalsIgnoreCase("yes")){
				System.out.println("Please provide the path where you want to save your resource. For example: 'C://my folder/'");
				filePath = sc.nextLine();
			}

			System.out.println("Sending request to proxy server.");
			String clientRequest = requestType + "|" + request;
			OutputStream out = socket.getOutputStream();
			out.write(clientRequest.getBytes());

			System.out.println("Waiting for response from proxy server...");

			InputStream input;
			if(requestType.equalsIgnoreCase("http|https") && isResource.equalsIgnoreCase("No")) {
				while(true) {
					int num = 0;
					input = socket.getInputStream();
					byte[] bArry = new byte[1024];
					num = input.read(bArry);
					String response = new String(bArry, 0, num);
					System.out.println("The HTTP server response is:\n" + response);
				}

			}else {
				input = new BufferedInputStream(socket.getInputStream(), 8192);
				String fileName = filePath+UUID.randomUUID()+request.substring(request.lastIndexOf("/")+1);
				FileOutputStream output = new FileOutputStream(fileName);
				byte data[] = new byte[1024];
	            int count;
	            while ((count = input.read(data)) != -1) {
	                // writing data to client
	                output.write(data, 0, count);
	            }
	         // flushing output
	            output.flush();
	            // closing streams
	            output.close();
	            System.out.println("Image downloaded successfully: " + fileName);

			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			sc.close();
			socket.close();
		}
	}
}
