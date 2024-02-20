package assignment_final;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Proxy.Type;
//import org.apache.commons.net.ftp.FTPClient;
import it.sauronsoftware.ftp4j.*;
import it.sauronsoftware.ftp4j.connectors.HTTPTunnelConnector;

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
		String remoteFile = "test-cron-2024-02-20.xlsx";
		String localFile = "./" + remoteFile;
		File file = new File(localFile);
		// "ftp://user:password@domain.com/" + name +";type=i"
		// ftp://dlpuser:rNrKYTX9g7z3RgJRmxWuGHbeu@ftp.dlptest.com/test-cron-2024-02-19.xlsx
		String ftpUrl = "ftp://" + user + ":" + pass + "@" + server + "/" + remoteFile;

		InetSocketAddress proxyAddress = new InetSocketAddress(proxyHost, proxyPort);
		Proxy proxy = new Proxy(Type.HTTP, proxyAddress);
		URL url = null;
		// Method 1: using URLConnection
		/*
		 * 
		 * try { url = new URL(ftpUrl); URLConnection connection = (URLConnection)
		 * url.openConnection(proxy);
		 * 
		 * System.out.println("Connecting to FTP server...");
		 * 
		 * connection.connect();
		 * 
		 * Map<String, List<String>> headers = connection.getHeaderFields();
		 * 
		 * for (Entry<String, List<String>> entry : headers.entrySet()) { String key =
		 * entry.getKey(); String value = entry.getValue().get(0);
		 * 
		 * System.out.println(key + ":" + value); } int contentLength =
		 * connection.getContentLength(); byte[] buffer = new byte[contentLength]; int
		 * bytesRead;
		 * 
		 * InputStream input = connection.getInputStream();
		 * 
		 * try (OutputStream fileOutput = new BufferedOutputStream(new
		 * FileOutputStream(file))) { while ((bytesRead = input.read()) != -1) {
		 * input.read(buffer, 0, bytesRead); } fileOutput.write(buffer);
		 * fileOutput.flush(); fileOutput.close(); if (file.exists())
		 * System.out.println("File downloaded successfully!"); } input.close();
		 * 
		 * } catch (MalformedURLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated catch
		 * block e.printStackTrace();
		 * 
		 * }
		 */

		// Method 2: Using Socket connection
		/*
		 * try(Socket socket = new Socket(server, port)){ InputStream input =
		 * socket.getInputStream(); OutputStream output = socket.getOutputStream();
		 * 
		 * output.write(ftpUrl.getBytes());
		 * 
		 * byte[] buffer = new byte[4096]; int bytesRead;
		 * 
		 * try (OutputStream fileOutput = new BufferedOutputStream(new
		 * FileOutputStream(file))) { while ((bytesRead = input.read()) != -1) {
		 * input.read(buffer, 0, 4096); String response = new String(buffer);
		 * System.out.println(response); break; }
		 * 
		 * output.write(user.getBytes());
		 * 
		 * while ((bytesRead = input.read()) != -1) { input.read(buffer, 0, 4096);
		 * String response = new String(buffer); System.out.println(response); break; }
		 * 
		 * fileOutput.write(buffer); fileOutput.flush(); fileOutput.close(); if
		 * (file.exists()) System.out.println("File downloaded successfully!"); }
		 * input.close();
		 * 
		 * } catch (UnknownHostException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */

		// Method 3: 

		// Replace these with your proxy, FTP server, and login details
		FTPClient client = new FTPClient();
		//FTPClient ftpClient = new FTPClient();
				
		try { // Configure the client to use the proxy //
			// ftpClient.setProxyPort(proxyPort);
			HTTPTunnelConnector connector = new HTTPTunnelConnector(proxyHost, proxyPort);
			client.setConnector(connector);
			//ftpClient.setProxy(proxy);
			// Connect to the FTP server through the proxy
			client.connect(server);
			client.login(user, pass);
			//ftpClient.connect(server);
			if(client.isConnected()) {
				System.out.println("Login successful.");
			}
			// Login to the FTP server
			/*if (ftpClient.login(user, pass)) {
				System.out.println("Login successful.");

				// Download the file
				try (OutputStream outputStream = new FileOutputStream(localFile)) {
					if (ftpClient.retrieveFile(remoteFile, outputStream)) {
						System.out.println("File downloaded successfully.");
					} else {
						System.out.println("File download failed.");
					}
				} catch (IOException ex) {
					System.out.println("Error downloading file: " + ex.getMessage());
				}

			} else {
				System.out.println("Login failed.");
			}*/
		} catch (

		IOException | IllegalStateException | FTPIllegalReplyException | FTPException ex) {
			ex.printStackTrace();
		} /*finally {
			try { // Logout and disconnect from theserver
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}*/

	}
}