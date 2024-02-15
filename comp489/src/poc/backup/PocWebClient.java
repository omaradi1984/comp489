package poc.backup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PocWebClient {
	/**
	 * @param arguments
	 */
	
	public static void main(String[] args) {
        String url = "http://thinklistenlearn.com/wp-content/uploads/Demo-5.jpg";

        // Configure the proxy
        InetSocketAddress proxyAddress = new InetSocketAddress("localhost", 6000); // Replace "your-proxy-host" and 8080 with your proxy details
        HttpClient client = HttpClient.newBuilder()
                .proxy(ProxySelector.of(proxyAddress))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        System.out.println(request.toString());
        
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            Map<String, List<String>> map = response.headers().map();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			    String key = entry.getKey();
			    List<String> value = entry.getValue();
			    System.out.println(key + ": " + value);
            }
            
            System.out.println(response.statusCode());
            //System.out.println("Response Body:");
            //System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	/*private static String URL = "";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		URL = "http://www.tcpipguide.com/free/t_HTTPResponseMessageFormat.htm";
				
		try {
			URL url = new URL(URL);
			SocketAddress socketAddress = new InetSocketAddress("localhost", 6000);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
			
			connection.setRequestMethod("GET");
			connection.connect();	
			
			/*Map<String, List<String>> map = connection.getHeaderFields();
			String[] valueArray = new String[map.size()];
			int index = 0;
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			    String key = entry.getKey();
			    List<String> value = entry.getValue();
			    valueArray[index] = value.get(0);
			    index++;
			    System.out.println(key + ": " + value.get(0));
			}
			
			System.out.println();
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			//System.out.println(
					//"Response Content:\n" + response.toString());
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}