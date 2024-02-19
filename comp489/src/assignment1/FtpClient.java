package assignment1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class FtpClient {
	/**
	 * @param arguments
	 */
	private static String URL = "", PROXY_ADDRESS = "", PROXY_PORT = "",
			FTP_USERNAME = "", FTP_PASSWORD = "", FILE_PATH = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//This is a comment
		// Setting variables from arguments
		getInput(args);

		// Validate user input
		validateInput(URL, PROXY_ADDRESS, PROXY_PORT);

		URL url = null;
		try {
			url = new URL(URL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String protocol = url.getProtocol();
		if (protocol.contains("http"))
			// Process request
			processHttpRequest(URL, PROXY_ADDRESS, PROXY_PORT, FILE_PATH);

		/*else if (protocol.contains("ftp"))
			processFtpRequest(URL, PROXY_ADDRESS, PROXY_PORT, FTP_USERNAME,
					FTP_PASSWORD, FILE_PATH);*/
	}

	private static void processHttpRequest(String url, String proxyAddress,
			String proxyPort, String filePath) {
		// TODO Auto-generated method stub
		SocketAddress proxyAddr = new InetSocketAddress(proxyAddress,
				Integer.valueOf(proxyPort));
		Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);

		byte[] data = null;
		HttpURLConnection connection = null;

		try {
			// Creating URL connection using the above proxy
			// by creating an object of URL class
			URL urlLink = new URL(url);

			// Now setting the connecting by
			// creating an object of HttpURLConnection class
			connection = (HttpURLConnection) urlLink.openConnection(proxy);
			System.out.println(
					"Sending request via " + proxy.type() + " proxy server at "
							+ proxy.address() + "\nWaiting for response...");
			connection.setRequestMethod("GET");
			connection.connect();

			// Creating streams
			//OutputStream output = connection.getOutputStream();
			//InputStream input = connection.getInputStream();
			// Get the content length
			//int contentLength = connection.getContentLength();
			// Get the response code
			int responseCode = connection.getResponseCode();
			
			//System.out.println("Response Code: " + responseCode);
			//System.out.println("Content Length: " + contentLength);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				//String contentType = connection.getContentType();
				//System.out.println("File type: " + contentType);
				//contentType.startsWith("text/html") && 
				if (filePath == null) {
					// Read and print the response content
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(
									connection.getInputStream()));
					String line;
					StringBuilder response = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					reader.close();
					System.out.println(
							"Response Content:\n" + response.toString());
				} else {
					if (filePath != null) {
						try (InputStream raw = connection
								.getInputStream()) {
							InputStream in = new BufferedInputStream(raw);
							data = new byte[8091];
							int offset = 0;
							int oldProgress = 0;
							int currentProgress = 0;
							while (offset < 8091) {
								int bytesRead = in.read(data, offset,
										data.length - offset);
								if (bytesRead == -1)
									break;
								offset += bytesRead;
								oldProgress = (int) ((((double)offset) / ((double)data.length)) * 100d);								
								if(currentProgress < oldProgress) {
									currentProgress = oldProgress;
									System.out.printf("Successfully downloaded: %d%%\n", currentProgress);
								}
							}
							String filename = urlLink.getFile();
							filename = filePath + UUID.randomUUID()
									+ filename.substring(
											filename.lastIndexOf('/') + 1);
							try (FileOutputStream fout = new FileOutputStream(
									filename)) {
								fout.write(data);
								System.out.println(
										"File downloaded successfully: "
												+ filename);
								fout.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println(
								"Please run the application again and provide a destination path.");
						return;
					}
					// Close the connection
					connection.disconnect();
			}
		}
		}// Catch block to handle the exceptions
		catch (Exception e) {

			// Print the line number here exception occurred
			// using the printStackTrace() method
			e.printStackTrace();

			// Display message only illustrating
			//System.out.println(false);
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
							+ "(optional) <username> (optional) <password> (optional) <directory path>");
			System.exit(1);
		}

		// Set variables
		PROXY_ADDRESS = args[0]; // Proxy server address
		PROXY_PORT = args[1]; // Proxy port
		URL = args[2]; // The address to the requested resource in the web

		/*try {
			if (new URL(URL).getProtocol().contentEquals("ftp")
					&& !FTP_USERNAME.isBlank() && !FTP_PASSWORD.isBlank())
				FTP_USERNAME = args[3];
			FTP_PASSWORD = args[4];
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		if (args.length == 4) {
			FILE_PATH = args[3]; // Directory where the requested file will be
									// downloaded
		} else if (args.length == 6) {
			FILE_PATH = args[5]; // Directory where the requested file will be
									// downloaded
		}
	}

	/*private static void processFtpRequest(String url, String proxyAddress,
			String proxyPort, String username, String password, String path) {
		// TODO Auto-generated method stub
		try {
			URL URL = new URL(url);
			String server = URL.getHost();
			int port = 21;
			String user = username;
			String pass = password;

			FTPClient ftpClient = new FTPClient();
			// ftpClient.setProxy(null);
			// Connect to the FTP server
			ftpClient.connect(server, port);
			String[] replies = ftpClient.getReplyStrings();
			if (replies != null && replies.length > 0) {
				for (String aReply : replies) {
					System.out.println("SERVER: " + aReply);
				}
			}

			// Login to the server
			if (ftpClient.login(user, pass)) {
				System.out.println("Login successful.");

				// Enter local passive mode
				ftpClient.enterLocalPassiveMode();

				// Set file type to binary (if you plan on downloading binary
				// files)
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

				// Downloading the file
				String fileName = path
						+ url.substring(url.lastIndexOf("/") + 1);
				OutputStream outputStream = new FileOutputStream(fileName,
						false);
				boolean success = ftpClient.retrieveFile(URL.getFile(),
						outputStream);
				outputStream.close();

				if (success) {
					System.out
							.println("File has been downloaded successfully.");
				} else {
					System.out.println("File download failed.");
				}

				// List the files in the current directory
				// FTPFile[] files = ftpClient.listFiles();
				// System.out.println("Listing files:");
				// for (FTPFile file : files) {
				// System.out.println(file.getName());

				// }

				// Logout from the server
				ftpClient.logout();
				System.out.println("Logout successful.");
			} else {
				System.out.println("Login failed.");
			}

			ftpClient.disconnect();
			System.out.println("Disconnected.");

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	private static void processHttpRequest1(String URL, String PROXY_ADDRESS,
			String PROXY_PORT, String FILE_PATH) {
		// TODO Auto-generated method stub
		try {
			Socket socket = new Socket(PROXY_ADDRESS,
					Integer.valueOf(PROXY_PORT));
			System.out.println("Connected to proxy server at "
					+ socket.getInetAddress() + ":" + socket.getPort());

			OutputStream output = socket.getOutputStream();
			InputStream input = socket.getInputStream();

			System.out.println(
					"Sending request for " + URL + "\nWaiting for response...");

			output.write(URL.getBytes());

			if ((URL.substring(URL.lastIndexOf(".")) == "html"
					|| URL.substring(URL.lastIndexOf(".")) == "txt")
					&& FILE_PATH == null) {

				byte[] array = input.readAllBytes();

				String response = new String(array);

				System.out.println("Response:\n" + response);

				input.close();
				output.close();
				socket.close();
			} else if (FILE_PATH != null
					&& Files.isDirectory(Paths.get(FILE_PATH))
					&& Files.isWritable(Paths.get(FILE_PATH))) {
				String filePath = FILE_PATH + UUID.randomUUID()
						+ URL.substring(URL.lastIndexOf("/") + 1);
				File file = new File(filePath);

				try (FileOutputStream outputStream = new FileOutputStream(file,
						false)) {
					int read;
					byte[] bytes = new byte[8192];
					while ((read = input.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (URL.substring(URL.lastIndexOf(".")) != "html"
					&& URL.substring(URL.lastIndexOf(".")) != "txt") {
				String filePath = "./" + UUID.randomUUID()
						+ URL.substring(URL.lastIndexOf("/") + 1);
				File file = new File(filePath);

				try (FileOutputStream outputStream = new FileOutputStream(file,
						false)) {
					int read;
					byte[] bytes = new byte[8192];
					while ((read = input.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
					}
					System.out.println("File downloaded successfully: " + file);
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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
