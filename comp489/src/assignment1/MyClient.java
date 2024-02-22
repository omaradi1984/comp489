package assignment1;
/**
 * title: MyClient.java
 * description: An example of a web client application
 * date: December 11, 2023
 * @author Omar Zohouradi
 */

/**
 * DOCUMENTATION...
 */

/**                                                                               
 *
 *<H1>Web client tool</H1>
 *
 *<H3>Purpose and Description</H3>
 *
 *<P>
 * An application that sends an HTTP, HTTPS or FTP request and retrieves responses.
 *</P>
 *<P>
 * This program uses a a variety of java net libraries to send network requests and recieve responses.
 *</P>
 *                                                                              
 *<DL>
 *<DT> Compiling and running instructions</DT>
 *<DT> Assuming SDK 8 (or later) and the CLASSPATH are set up properly.</DT>
 *<DT> Change to the directory containing the source code.</DT>
 *<DD> Compile:    javac myclient.java</DD>
 *<DD> Run:        java myclient <proxy address> <proxy port> <request><path to save requested file></DD>
 *<DD> Document:   javadoc myclient.java</DD>
 *</DL>
 */

 /**
 *
 * <H3>myclient Methods</H3>
 *
 *<P>
   private static void processFtpRequest(URL request, String address,
			String proxyPort, String filePath) {<BR>
   ftp request processor. creates a URLConnection object and connects to destination server.
 *</P>
 *<P>
   public static void main(String args[]) {<BR>
   This method is used to execute the application
 *</P>
 *<P>
   private static void processHttpsRequest(URL request, String address,
			String proxyPort, String filePath) {<BR>
   https request processor. creates a URLConnection object and connects to destination server.
 *</P>
 **<P>
   private static void processHttpRequest(URL request, String address,
			String proxyPort, String filePath) {<BR>
   http request processor. creates a URLConnection object and connects to destination server.
 *</P>
 * 
 *<P>
 * private static void getInput(String[] args) {<BR>
 * This method is required to store arguments
 *</p>
 *
 *<P>
 * rivate static void validateInput(String URL, String PROXY_ADDRESS,
			String PROXY_PORT) {<BR>
 * This method is required to validate input values
 *</p>
 * <H3>myclient Instance Variables</H3>
 *
 *<P>
 * private static String REQUEST = null, PROXY_ADDRESS = null,
			PROXY_PORT = null, FILE_PATH = null;
 *</P>
 */

/**
 *
 * <H3>Test Plan</H3>
 *
 *<P>
 * 1. Run the application with the following arguments
 * Proxy address (mandatory)
 * Proxy port  (mandatory)
 * Request URL  (mandatory)
 * File path to store file (optional)
 * EXPECTED:
 *    the appliaction will send request via the proxy server and successfully consume response
 * ACTUAL:
 *    GUI frame displays as expected.
 *</P>
 */ 

/**
 * CODE...
 */

/** Java core packages */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

public class MyClient {

	/**
	 * @param arguments
	 **/
	private static String REQUEST = null, PROXY_ADDRESS = null,
			PROXY_PORT = null, FILE_PATH = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// http://thinklistenlearn.com/wp-content/uploads/Demo-5.jpg

		// Setting variables from arguments http://127.0.0.1:8000/testfile.html
		getInput(args);

		// Validate user input
		validateInput(REQUEST, PROXY_ADDRESS, PROXY_PORT);

		URL url = null;

		// String httpsURL = "https://example.com/"; // Replace this with your
		// target URL
		// http://127.0.0.1:8000/testfile.html
		try {
			// Create URL object
			url = new URL(REQUEST);

			String protocol = url.getProtocol();
			if (protocol.contentEquals("http"))
				// Process request
				processHttpRequest(url, PROXY_ADDRESS, PROXY_PORT, FILE_PATH);

			else if (protocol.contains("ftp"))
				processFtpRequest(url, PROXY_ADDRESS, PROXY_PORT, FILE_PATH);

			else if (protocol.contains("https"))
				processHttpsRequest(url, PROXY_ADDRESS, PROXY_PORT, FILE_PATH);

		} catch (IOException ex) {
			System.out.println(
					"Couldn't establish connection! Please try again.");
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	private static void processFtpRequest(URL request, String address,
			String proxyPort, String filePath) {
		// TODO Auto-generated method stub
		try {
			URL url = request;
			// System.out.println(url.toString());
			// Configure the proxy
			InetSocketAddress proxyAddress = new InetSocketAddress(address,
					Integer.valueOf(proxyPort));
			Proxy proxy = new Proxy(Type.HTTP, proxyAddress);

			// Create connection proxy
			URLConnection connection = (URLConnection) url
					.openConnection();

			connection.connect();

			System.out.println(
					"Sending request via " + proxy.type() + " proxy server at "
							+ proxy.address() + "\nWaiting for response...");

			Map<String, List<String>> headers = connection.getHeaderFields();

			for (Entry<String, List<String>> entry : headers.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue().get(0);

				System.out.println(key + ":" + value);
			}
			
			String remoteFile = url.getFile();
			String fileName = filePath + UUID.randomUUID()
					+ remoteFile.substring(remoteFile.lastIndexOf('/') + 1);
			File file = new File(fileName);
			
			//int responseCode = ((HttpURLConnection) connection).getResponseCode();
			String contentType = connection.getContentType();
			int contentLength = connection.getContentLength();
			byte[] data = null;
			InputStream raw = connection.getInputStream();
			InputStream in = new BufferedInputStream(raw);
			data = new byte[contentLength];
			int offset = 0;
			int oldProgress = 0;
			int currentProgress = 0;
			while (offset < contentLength) {
				int bytesRead = in.read(data, offset,
						data.length - offset);
				if (bytesRead == -1)
					break;
				offset += bytesRead;
				oldProgress = (int) ((((double) offset)
						/ ((double) data.length)) * 100d);
				if (currentProgress < oldProgress) {
					currentProgress = oldProgress;
					System.out.printf(
							"Successfully downloaded: %d%%\n",
							currentProgress);
				}
				
			}
			try (FileOutputStream fout = new FileOutputStream(
					fileName)) {
				fout.write(data);
				System.out.println("File downloaded successfully: "
						+ fileName);
				fout.flush();
			}
		}catch(IOException ex) {
			
		}
	}

	private static void processHttpsRequest(URL request, String address,
			String proxyPort, String filePath) {
		try {
			URL url = request;
			System.out.println(url.toString());
			// Configure the proxy
			InetSocketAddress proxyAddress = new InetSocketAddress(address,
					Integer.valueOf(proxyPort));
			Proxy proxy = new Proxy(Type.HTTP, proxyAddress);

			// Create connection
			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection(proxy);

			// connection.setRequestMethod("GET");
			connection.connect();

			System.out.println(
					"Sending request via " + proxy.type() + " proxy server at "
							+ proxy.address() + "\nWaiting for response...");

			int responseCode = connection.getResponseCode();
			String contentType = connection.getContentType();
			int contentLength = connection.getContentLength();
			byte[] data = null;
			if (responseCode != HttpsURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuilder response = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
				System.out.println("Response Content:\n" + response.toString());
			} else if (responseCode == HttpsURLConnection.HTTP_OK) {
				if (filePath == null && (contentType.contains("text")
						|| contentType.contains("html"))) {
					// Read and print the response content
					Map<String, List<String>> map = connection
							.getHeaderFields();
					for (Entry<String, List<String>> entry : map.entrySet()) {
						String key = entry.getKey();
						String value = entry.getValue().get(0);
						System.out.println(key + ":" + value);
					}
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
					String line;
					StringBuilder response = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					reader.close();
					System.out.println(
							"Response Content:\n" + response.toString());
				} else {
					if (filePath != null
							&& Files.isDirectory(Paths.get(filePath))
							&& Files.isWritable(Paths.get(filePath))) {
						try (InputStream raw = connection.getInputStream()) {
							System.out.println("Response: " + responseCode + " "
									+ connection.getResponseMessage());
							Map<String, List<String>> map = connection
									.getHeaderFields();
							for (Entry<String, List<String>> entry : map
									.entrySet()) {
								String key = entry.getKey();
								String value = entry.getValue().get(0);
								System.out.println(key + ":" + value);
							}
							InputStream in = new BufferedInputStream(raw);
							data = new byte[contentLength];
							int offset = 0;
							int oldProgress = 0;
							int currentProgress = 0;
							while (offset < contentLength) {
								int bytesRead = in.read(data, offset,
										data.length - offset);
								if (bytesRead == -1)
									break;
								offset += bytesRead;
								oldProgress = (int) ((((double) offset)
										/ ((double) data.length)) * 100d);
								if (currentProgress < oldProgress) {
									currentProgress = oldProgress;
									System.out.printf(
											"Successfully downloaded: %d%%\n",
											currentProgress);
								}
							}
							String filename = url.getFile();
							filename = filePath + UUID.randomUUID() + filename
									.substring(filename.lastIndexOf('/') + 1);
							try (FileOutputStream fout = new FileOutputStream(
									filename)) {
								fout.write(data);
								System.out.println(
										"File downloaded successfully: "
												+ filename);
								fout.flush();
							}
						}
					} else {
						try (InputStream raw = connection.getInputStream()) {
							Map<String, List<String>> map = connection
									.getHeaderFields();
							for (Entry<String, List<String>> entry : map
									.entrySet()) {
								String key = entry.getKey();
								String value = entry.getValue().get(0);
								System.out.println(key + ":" + value);
							}
							InputStream in = new BufferedInputStream(raw);
							data = new byte[contentLength];
							int offset = 0;
							int oldProgress = 0;
							int currentProgress = 0;
							while (offset < contentLength) {
								int bytesRead = in.read(data, offset,
										data.length - offset);
								if (bytesRead == -1)
									break;
								offset += bytesRead;
								oldProgress = (int) ((((double) offset)
										/ ((double) data.length)) * 100d);
								if (currentProgress < oldProgress) {
									currentProgress = oldProgress;
									System.out.printf(
											"Successfully downloaded: %d%%\n",
											currentProgress);
								}
							}
							String filename = url.getFile();
							filename = "./" + UUID.randomUUID() + filename
									.substring(filename.lastIndexOf('/') + 1);
							try (FileOutputStream fout = new FileOutputStream(
									filename)) {
								fout.write(data);
								System.out.println(
										"File downloaded successfully: "
												+ filename);
								fout.flush();
							}
						}
					}

				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void processHttpRequest(URL request, String address,
			String proxyPort, String filePath) {
		try {
			URL url = request;

			// Configure the proxy
			InetSocketAddress proxyAddress = new InetSocketAddress(address,
					Integer.valueOf(proxyPort));
			Proxy proxy = new Proxy(Type.HTTP, proxyAddress);

			// Create connection
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection(proxy);

			connection.setRequestMethod("GET");
			connection.connect();
			System.out.println(
					"Sending request via " + proxy.type() + " proxy server at "
							+ proxy.address() + "\nWaiting for response...");

			int responseCode = connection.getResponseCode();
			String contentType = connection.getContentType();
			int contentLength = connection.getContentLength();
			byte[] data = null;
			if (responseCode != HttpURLConnection.HTTP_OK) {
				System.out.println(
						responseCode + " " + connection.getResponseMessage());
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuilder response = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
				System.out.println("Response Content:\n" + response.toString());
			} else if (responseCode == HttpURLConnection.HTTP_OK) {
				if (filePath == null && (contentType.contains("text")
						|| contentType.contains("html"))) {
					// Read and print the response content
					Map<String, List<String>> map = connection
							.getHeaderFields();
					for (Entry<String, List<String>> entry : map.entrySet()) {
						String key = entry.getKey();
						String value = entry.getValue().get(0);
						System.out.println(key + ": " + value);
					}
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
					String line;
					StringBuilder response = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					reader.close();
					System.out.println(
							"Response Content:\n" + response.toString());
				} else if (filePath == null && (!contentType.contains("text")
						|| !contentType.contains("html"))) {
					try (InputStream raw = connection.getInputStream()) {
						System.out.println("Response: " + responseCode + " "
								+ connection.getResponseMessage());
						Map<String, List<String>> map = connection
								.getHeaderFields();
						for (Entry<String, List<String>> entry : map
								.entrySet()) {
							String key = entry.getKey();
							String value = entry.getValue().get(0);
							System.out.println(key + ":" + value);
						}
						InputStream in = new BufferedInputStream(raw);
						data = new byte[contentLength];
						int offset = 0;
						int oldProgress = 0;
						int currentProgress = 0;
						while (offset < contentLength) {
							int bytesRead = in.read(data, offset,
									data.length - offset);
							if (bytesRead == -1)
								break;
							offset += bytesRead;
							oldProgress = (int) ((((double) offset)
									/ ((double) data.length)) * 100d);
							if (currentProgress < oldProgress) {
								currentProgress = oldProgress;
								System.out.printf(
										"Successfully downloaded: %d%%\n",
										currentProgress);
							}
						}
						String filename = url.getFile();
						filename = "./" + UUID.randomUUID() + filename
								.substring(filename.lastIndexOf('/') + 1);
						try (FileOutputStream fout = new FileOutputStream(
								filename)) {
							fout.write(data);
							System.out.println("File downloaded successfully: "
									+ filename);
							fout.flush();
						}
					}
				} else if (filePath != null
						&& Files.isDirectory(Paths.get(filePath))
						&& Files.isWritable(Paths.get(filePath))) {
					try (InputStream raw = connection.getInputStream()) {
						System.out.println("Response: " + responseCode + " "
								+ connection.getResponseMessage());
						Map<String, List<String>> map = connection
								.getHeaderFields();
						for (Entry<String, List<String>> entry : map
								.entrySet()) {
							String key = entry.getKey();
							String value = entry.getValue().get(0);
							System.out.println(key + ":" + value);
						}
						InputStream in = new BufferedInputStream(raw);
						data = new byte[contentLength];
						int offset = 0;
						int oldProgress = 0;
						int currentProgress = 0;
						while (offset < contentLength) {
							int bytesRead = in.read(data, offset,
									data.length - offset);
							if (bytesRead == -1)
								break;
							offset += bytesRead;
							oldProgress = (int) ((((double) offset)
									/ ((double) data.length)) * 100d);
							if (currentProgress < oldProgress) {
								currentProgress = oldProgress;
								System.out.printf(
										"Successfully downloaded: %d%%\n",
										currentProgress);
							}
						}
						String filename = url.getFile();
						filename = filePath + UUID.randomUUID() + filename
								.substring(filename.lastIndexOf('/') + 1);
						try (FileOutputStream fout = new FileOutputStream(
								filename)) {
							fout.write(data);
							System.out.println("File downloaded successfully: "
									+ filename);
							fout.flush();
						}
					}
				} else if (filePath != null
						&& (!Files.isDirectory(Paths.get(filePath))
								|| !Files.isWritable(Paths.get(filePath)))) {
					try (InputStream raw = connection.getInputStream()) {
						Map<String, List<String>> map = connection
								.getHeaderFields();
						for (Entry<String, List<String>> entry : map
								.entrySet()) {
							String key = entry.getKey();
							String value = entry.getValue().get(0);
							System.out.println(key + ":" + value);
						}
						InputStream in = new BufferedInputStream(raw);
						data = new byte[contentLength];
						int offset = 0;
						int oldProgress = 0;
						int currentProgress = 0;
						while (offset < contentLength) {
							int bytesRead = in.read(data, offset,
									data.length - offset);
							if (bytesRead == -1)
								break;
							offset += bytesRead;
							oldProgress = (int) ((((double) offset)
									/ ((double) data.length)) * 100d);
							if (currentProgress < oldProgress) {
								currentProgress = oldProgress;
								System.out.printf(
										"Successfully downloaded: %d%%\n",
										currentProgress);
							}
						}
						String filename = url.getFile();
						filename = "./" + UUID.randomUUID() + filename
								.substring(filename.lastIndexOf('/') + 1);
						try (FileOutputStream fout = new FileOutputStream(
								filename)) {
							fout.write(data);
							System.out.println("File downloaded successfully: "
									+ filename);
							fout.flush();
						}
					}
				}

			}

		} catch (IOException ex) {
			System.out.println(
					"Couldn't establish connection! Please try again.");
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	private static void getInput(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 3) {
			System.out.println(
					"Please provide the minimum required arguments\nProxy server address, "
							+ "proxy server port, and URL to desired resource"
							+ ".\n(Optional) path to the directory where you want the resource to be saved.\n"
							+ ".\n(Optional) username and password for ftp requests if required.\n"
							+ "For example: java MyWebClient.java <proxy address> <proxy port> <resource URL> "
							+ "(optional) <directory path> (optional) <username> (optional) <password>");
			System.exit(1);
		}

		// Set variables
		PROXY_ADDRESS = args[0]; // Proxy server address
		PROXY_PORT = args[1]; // Proxy port
		REQUEST = args[2]; // The address to the requested resource in the web

		if (args.length >= 4) {
			FILE_PATH = args[3]; // Directory where the requested file will be
									// downloaded
		}
	}

	private static void validateInput(String URL, String PROXY_ADDRESS,
			String PROXY_PORT) {
		// TODO Auto-generated method stub

		if (PROXY_ADDRESS == null) {
			System.out.println(
					"Please provide a valid proxy address, e.g. 192.168.10.1.");
			System.exit(1);
		}

		// Validate proxy port
		if (!(Integer.valueOf(PROXY_PORT) instanceof Integer)) {
			System.out
					.println("Please provide a valid port number, e.g. 6000.");
			System.exit(1);
		}
		// Validate host address
		try {
			if (!((new URL(URL)) instanceof URL)) {
				System.out.println(
						"Please provide a valid url, e.g.  http://127.0.0.1:8000/testfile.html.");
				System.exit(1);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println(
					"Please provide a valid url, e.g. http://127.0.0.1:8000/testfile.html.");
			System.exit(1);
		}
	}

}
