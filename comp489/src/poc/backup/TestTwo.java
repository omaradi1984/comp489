package poc.backup;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class TestTwo {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		String address = sc.nextLine();

		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        /*
    	InputStream input = conn.getInputStream();
    	byte[] bArry = new byte[1024];
		int num = input.read(bArry);
		String response = new String(bArry, 0, num);
		System.out.println(response);
		conn.disconnect();*/

        InputStream input = new BufferedInputStream(conn.getInputStream(), 8192);
		String fileName = "G:/My Drive/"+address.substring(address.lastIndexOf("/")+1);
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
        input.close();
        System.out.println("Image downloaded successfully: " + fileName);
	}

}
