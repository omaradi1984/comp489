package assignment1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class MyClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//http://thinklistenlearn.com/wp-content/uploads/Demo-5.jpg
		
		String httpsURL = "ftp://speedtest.tele2.net/"; // Replace this with your target URL http://127.0.0.1:8000/testfile.html
        try {
            // Create URL object
            URL url = new URL(httpsURL);
            InetSocketAddress proxyAddress = new InetSocketAddress("localhost",
					6000);
			Proxy proxy = new Proxy(Type.HTTP, proxyAddress);
            // Open connection
            URLConnection conn = (URLConnection) url.openConnection(proxy);
            
            // Set request method to GET
            //conn.;
            conn.connect();
            // Set various request properties if needed (e.g., user-agent, headers)
            // conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            // Get the response code
            //int responseCode = conn.getResponseCode();
            //System.out.println("Response Code: " + responseCode);
            
            // Read the response if the request was successful (response code 200)
            //if (200 == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                // Print the response
                Map<String, List<String>> map = conn.getHeaderFields();
                for (Entry<String, List<String>> entry : map.entrySet()) {
                	String key = entry.getKey();
                	String value = entry.getValue().get(0);
                	System.out.println(key+": " + value);
                }
                System.out.println(response.toString());
            //} else {
                System.out.println("GET request not worked");
           // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
