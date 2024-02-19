package assignment_final;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.net.ftp.FTPClient;

import java.net.*;

public class FtpClient {

	public static void main(String[] args) {
		// Proxy settings
		String proxyHost = "localhost"; // Replace with your proxy host
		int proxyPort = 6000; // Replace with your proxy port
		// Set system properties for HTTP proxy

		// FTP server information
		String server = "ftp.dlptest.com"; // Replace with your FTP server
											// address
		int port = 21;
		String user = "dlpuser"; // Replace with your FTP username
		String pass = "rNrKYTX9g7z3RgJRmxWuGHbeu"; // Replace with your FTP
													// password
		String remoteFile = "test-cron-2024-02-19.xlsx";
		String localFile = "./" + remoteFile;
		File file = new File(localFile);
		// "ftp://user:password@domain.com/" + name +";type=i"
		// ftp://dlpuser:rNrKYTX9g7z3RgJRmxWuGHbeu@ftp.dlptest.com/test-cron-2024-02-19.xlsx
		String ftpUrl = "ftp://" + user + ":" + pass + "@" + server + "/"
				+ remoteFile;

		InetSocketAddress proxyAddress = new InetSocketAddress(proxyHost,
				proxyPort);
		Proxy proxy = new Proxy(Type.HTTP, proxyAddress);

		URL url = null;
		//Method 1: using URLConnection
		/*try {
			url = new URL(ftpUrl);
			URLConnection connection = (URLConnection) url.openConnection(proxy);

			System.out.println("Connecting to FTP server...");

			connection.connect();

			Map<String, List<String>> headers = connection.getHeaderFields();

			for (Entry<String, List<String>> entry : headers.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue().get(0);

				System.out.println(key + ":" + value);
			}
			int contentLength = connection.getContentLength();
			byte[] buffer = new byte[contentLength];
			int bytesRead;

			InputStream input = connection.getInputStream();

			try (OutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(file))) {
				while ((bytesRead = input.read()) != -1) {
					input.read(buffer, 0, contentLength);
				}
				fileOutput.write(buffer);
				fileOutput.flush();
				fileOutput.close();
				if (file.exists())
					System.out.println("File downloaded successfully!");
			}
			input.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}*/
		
		//Method 2: Using Socket connection
	/*	try(Socket socket = new Socket(server, port)){
			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();
			
			output.write(ftpUrl.getBytes());
			
			byte[] buffer = new byte[4096];
			int bytesRead;
			
			try (OutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(file))) {
				while ((bytesRead = input.read()) != -1) {
					input.read(buffer, 0, 4096);
					String response = new String(buffer);
					System.out.println(response);
					break;
				}
				
				output.write(user.getBytes());
				
				while ((bytesRead = input.read()) != -1) {
					input.read(buffer, 0, 4096);
					String response = new String(buffer);
					System.out.println(response);
					break;
				}
				
				fileOutput.write(buffer);
				fileOutput.flush();
				fileOutput.close();
				if (file.exists())
					System.out.println("File downloaded successfully!");
			}
			input.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//Method 3: using Http Tunnelling
		FTPClient client = new FTPClient();
		client.setProxy(proxy);;
		HTTPTunnelConnector connector = new HTTPTunnelConnector(proxyHost,proxyPort);
		client.setConnector(connector);
		
		client.connect(ftpHost);
		client.login(ftpUser, ftpPass);

		System.out.println("initial file listing in root");
		FTPFile[] list = client.list();
		for(FTPFile file : list) {
			System.out.println(file.getName());
		}
		
		System.out.println("changing to dir: " + changeDir);
		client.changeDirectory(changeDir);
		
		System.out.println("listing after change directory:");
		list = client.list();
		for(FTPFile file : list) {
			System.out.println(file.getName());
		}
		
		client.disconnect(true);
		System.out.println("done");

	}
}
/*
 * FTPClient ftpClient = new FTPClient(); try {
 * 
 * ftpClient.connect(server, port); ftpClient.login(user, pass);
 * ftpClient.enterLocalPassiveMode();
 * 
 * ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
 * 
 * // APPROACH #1: uploads first file using an InputStream File firstLocalFile =
 * new File("D:/Test/Projects.zip");
 * 
 * String firstRemoteFile = "Projects.zip"; InputStream inputStream = new
 * FileInputStream(firstLocalFile);
 * 
 * System.out.println("Start uploading first file"); boolean done =
 * ftpClient.storeFile(firstRemoteFile, inputStream); inputStream.close(); if
 * (done) { System.out.println("The first file is uploaded successfully."); }
 * 
 * // APPROACH #2: uploads second file using an OutputStream File
 * secondLocalFile = new File("E:/Test/Report.doc"); String secondRemoteFile =
 * "test/Report.doc"; inputStream = new FileInputStream(secondLocalFile);
 * 
 * System.out.println("Start uploading second file"); OutputStream outputStream
 * = ftpClient.storeFileStream(secondRemoteFile); byte[] bytesIn = new
 * byte[4096]; int read = 0;
 * 
 * while ((read = inputStream.read(bytesIn)) != -1) {
 * outputStream.write(bytesIn, 0, read); } inputStream.close();
 * outputStream.close();
 * 
 * boolean completed = ftpClient.completePendingCommand(); if (completed) {
 * System.out.println("The second file is uploaded successfully."); }
 * 
 * } catch (IOException ex) { System.out.println("Error: " + ex.getMessage());
 * ex.printStackTrace(); } finally { try { if (ftpClient.isConnected()) {
 * ftpClient.logout(); ftpClient.disconnect(); } } catch (IOException ex) {
 * ex.printStackTrace(); } }
 * 
 * 
 * 
 * 
 */
