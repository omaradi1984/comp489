package assignment1.poc;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;

public class MyClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//http://thinklistenlearn.com/wp-content/uploads/Demo-5.jpg
		
		try {
			
			String request = "http://thinklistenlearn.com:80/wp-content/uploads/Demo-5.jpg";
			
			InetSocketAddress proxyAddress = new InetSocketAddress("127.0.0.1", 9000);
			
			Proxy proxy = new Proxy(Type.HTTP, proxyAddress);
			
			URL url = new URL(request);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
			
			connection.connect();
	        
			InputStream  fromServer = connection.getInputStream();
			//OutputStream toServer = connection.getOutputStream();
			
			byte[] array = new byte[connection.getContentLength()];
			
			//toServer.write(request.getBytes());
			
			int bytesRead = fromServer.read(array);
			
			String response = new String(array, 0, bytesRead);
			
			System.out.println("Server response: " + response);
			
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

}
