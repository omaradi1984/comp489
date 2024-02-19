package assignment1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class PocWebClient {
	/**
	 * @param arguments
	 */
	private static String REQUEST = "", PROXY_ADDRESS = "", PROXY_PORT = "",
			FTP_USERNAME = "", FTP_PASSWORD = "", FILE_PATH = null;

	public static void main(String[] args) {

		// Setting variables from arguments http://127.0.0.1:8000/testfile.html
		getInput(args);

		// Validate user input
		validateInput(REQUEST, PROXY_ADDRESS, PROXY_PORT);

		URL url = null;
		try {
			url = new URL(REQUEST);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String protocol = url.getProtocol();
		if (protocol.contentEquals("http") || protocol.contentEquals("https"))
			// Process request
			processHttpRequest(REQUEST, PROXY_ADDRESS, PROXY_PORT, FILE_PATH);

		else if (protocol.contains("ftp"))
			processFtpRequest(REQUEST, PROXY_ADDRESS, PROXY_PORT, FTP_USERNAME,
					FTP_PASSWORD, FILE_PATH);

		else if (protocol.contains("https"))
			processHttpsRequest(REQUEST, PROXY_ADDRESS, PROXY_PORT, FILE_PATH);

	}

	private static void processHttpsRequest(String request, String address,
			String proxyPort, String filePath) {
		try {
			URL url = new URL(request);
			System.out.println(url.toString());
			// Configure the proxy
			InetSocketAddress proxyAddress = new InetSocketAddress(address,
					Integer.valueOf(proxyPort));
			Proxy proxy = new Proxy(Type.HTTP, proxyAddress);

			// Create connection
			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection(proxy);
			System.out.println(
					"Sending request via " + proxy.type() + " proxy server at "
							+ proxy.address() + "\nWaiting for response...");
			connection.setRequestMethod("GET");
			connection.connect();

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

	private static void processFtpRequest(String request, String address,
			String proxyPort, String username, String password, String filePath) {
		// TODO Auto-generated method stub
		
	}

	private static void processHttpRequest(String request, String address,
			String proxyPort, String filePath) {
		try {
			URL url = new URL(request);

			// Configure the proxy
			InetSocketAddress proxyAddress = new InetSocketAddress(address,
					Integer.valueOf(proxyPort));
			Proxy proxy = new Proxy(Type.HTTP, proxyAddress);

			// Create connection
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection(proxy);
			System.out.println(
					"Sending request via " + proxy.type() + " proxy server at "
							+ proxy.address() + "\nWaiting for response...");
			connection.setRequestMethod("GET");
			connection.connect();

			int responseCode = connection.getResponseCode();
			String contentType = connection.getContentType();
			int contentLength = connection.getContentLength();
			byte[] data = null;
			if (responseCode != HttpURLConnection.HTTP_OK) {
				System.out.println(responseCode + " " + connection.getResponseMessage());
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
				} else if (filePath == null
						&& (!contentType.contains("text")
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
							System.out.println(
									"File downloaded successfully: "
											+ filename);
							fout.flush();
						}
					}
			}else if (filePath != null
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
				}else if (filePath != null
							&& (!Files.isDirectory(Paths.get(filePath))
							|| !Files.isWritable(Paths.get(filePath)))){
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

		} catch (IOException ex) {
			//System.out.println(ex.getMessage());
			//ex.printStackTrace();
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
		if (args.length >= 5) {
			FTP_USERNAME = args[4]; // FTP username
		}
		if (args.length == 6) {
			FTP_PASSWORD = args[5]; // FTP password
		}
	}

	private static void validateInput(String URL, String PROXY_ADDRESS,
			String PROXY_PORT) {
		// TODO Auto-generated method stub

		if (PROXY_ADDRESS.length() == 0
				|| PROXY_ADDRESS.split("[.]").length != 4) {
			System.out.println(
					"Please provide a valid proxy address, e.g. 192.168.10.1.");
			System.exit(1);
		}

		// Validate proxy port
		if (PROXY_PORT.length() == 0
				|| !(Integer.valueOf(PROXY_PORT) instanceof Integer)) {
			System.out
					.println("Please provide a valid port number, e.g. 6000.");
			System.exit(1);
		}
		// Validate host address
		try {
			if (URL.length() == 0 || !((new URL(URL)) instanceof URL)) {
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