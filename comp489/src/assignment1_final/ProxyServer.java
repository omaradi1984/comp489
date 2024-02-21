/**
 * title: ProxyServer.java
 * description: a proxy server application.
 * date: December 25, 2023
 * @author Omar Zohouradi
 * @version 1.0
 */
/**
* DOCUMENTATION...
*/
/**
 *
 *<H1>Proxy Server</H1>
 *
 *<H2>Purpose and Description</H2>
 *
 *<P>
 * An application server that redirects incoming requests for resources.
 *</P>
 *<P>
 * This proxy server redirects client requests if it is http, https or ftp, otherwise it returns a not supported protocol! error.
 * The user will need to provide the port number when starting the server application in the following way:
 * java ProxyServer.java <<port>>
 * Example: java ProxyServer.java 8000
 *</P>
 *
 *<DL>
 *<DT> Compiling and running instructions</DT>
 *<DT> Assuming SDK 1.8 (or later) and the CLASSPATH are set up properly.</DT>
 *<DT> Change to the directory containing the source code.</DT>
 *<DD> Compile:    javac ProxyServer.java</DD>
 *<DD> Run:        java ProxyServer  <<port>> <<IP address to listen to>></DD>
 *<DD> Document:   javadoc ProxyServer.java</DD>
 *</DL>
 */
/**
*
* <H3>Classes</H3>
*
*<P>
* public class ProxyServer {<BR>
* This is the main public class for this http server. It includes several java libraries and private static methods to process incoming requests.
* example libraries:
* import java.io.File;
* import java.io.FileInputStream;
* import java.io.IOException;
* import java.io.OutputStream;
* import java.net.InetSocketAddress;
* import java.util.HashMap;
* import java.util.Map;
*</P>
*
*<H2>Parameter arguments</H2>
*<P>
* protected static int PORT;
* protected static int IP_ADDRESS;
*</P>
*
*<H2>Methods</H2>
*<P>
* public void handle(HttpExchange t) {<BR>
* This method will handle the http request coming to the server. It will determine the type of of content-type request. Pass the content type to the client
* and writes the resource using the outputstream. If the resource doesn't exist, the method will return 404 not found error.
*</P>
*<P>
* public static void logAccess(HttpExchange t, File file,String filePath) {<BR>
* This method logs access to resource requests. Example: 127.0.0.1 - - [07/Feb./2024:23:15:10 -0500] "GET /testfile.html HTTP/1.1" 200 562.
* The method will create a log file, if it doesn't already exist, or append an existing one. The log file (access_log) will reside in the application folder.
*</P>
<P>
* public static void logError(HttpExchange t, String filePath, Exception e) {<BR>
* This method logs errors generated for different reasons. Example: WARNING 127.0.0.1 - - [08/Feb/2024:13:44:09 -0500] "GET /testfile.mp4 HTTP/1.1" 200 || An established connection was aborted by the software in your host machine.
* The method will create a log file, if it doesn't already exist, or append an existing one. The log file (error_log) will reside in the application folder.
*</P>
*<P>
  public static void main(String args[]) {<BR>
  This method is used to execute the application
*</P>
/**
 *
 * <H3>Test Plan</H3>
 *
 *<P>
 * Test case 1: Run the application and pass an available port and optionally an IP address to listen to.
 * EXPECTED:
 * If the user passed available port and a number of threads, the application runs as expected.
 * Test case 2: Using the web client application, send an http, https or ftp request
 * EXPECTED:
 * The application returns the expected resource.
 *</P>
 */
/**
 * CODE...
 */
/** Java core packages */
package assignment1_final;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * @author ozohouradi
 *
 */
public class ProxyServer {

	/**
	 * @param args
	 *            int PORT, String IP_ADDRESS
	 */
	private static int PORT = 0;
	private static String IP_ADDRESS = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args.length == 0) {
			System.out.println(
					"Please provide a valid port number (mandatory) and if applicable IP address to run the proxy server, "
							+ "e.g. java proxyserver.java (mandatory) 6000 (optional) 192.168.0.2");
			System.exit(1);
		} else if (args.length == 1) {
			PORT = Integer.valueOf(args[0]);
			try (ServerSocket serverSocket = new ServerSocket(PORT);) {
				System.out.println("Server started and listening on "
						+ serverSocket.getLocalSocketAddress());
				while (true) {
					Socket clientSocket = serverSocket.accept();
					// Handle client connection in a separate thread
					new Thread(new ClientHandler(clientSocket)).start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(
						"Oops, Something happened! Couldn't start the proxy server, please try again.\n");
				System.out.println("Error message: " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}

		} else if (args.length == 2) {
			PORT = Integer.valueOf(args[0]);
			IP_ADDRESS = args[1];
			try (ServerSocket serverSocket = new ServerSocket(PORT, 100,
					InetAddress.getByAddress(IP_ADDRESS.getBytes()));) {
				System.out.println("Server started and listening on "
						+ serverSocket.getLocalSocketAddress());
				while (true) {
					Socket clientSocket = serverSocket.accept();
					// Handle client connection in a separate thread
					new Thread(new ClientHandler(clientSocket)).start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(
						"Oops, Something happened! Couldn't start the proxy server, please try again.\n");
				System.out.println("Error message: " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
		}

	}

	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		private boolean cFlag = false;

		/**
		 * @param clientSocket
		 */
		public ClientHandler(Socket clientSocket) {
			super();
			this.clientSocket = clientSocket;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String request = "";
			Object[] array = new Object[4];
			try {
				array = (Object[]) readRequest(clientSocket);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {

				String destinationHost;
				int destinationPort;
				if (array[0] != null) {
					request = (String) array[3];
					if (request.startsWith("GET") && request.contains("http")) {
						System.out.println("Request: " + request);
						InputStream in = (InputStream) array[0];
						destinationHost = (String) array[1];
						destinationPort = (Integer) array[2];

						try (Socket serverSocket = new Socket(destinationHost,
								destinationPort);
								InputStream fromClient = in;
								OutputStream toClient = clientSocket
										.getOutputStream();
								InputStream fromServer = serverSocket
										.getInputStream();
								OutputStream toServer = serverSocket
										.getOutputStream();) {

							// Forward client to server
							Thread clientToServerThread = new Thread(
									() -> forwardData(fromClient, toServer));
							clientToServerThread.start();

							// Forward server to client
							forwardData(fromServer, toClient);

						} catch (Exception e) {
							System.out
									.println("Error processing HTTP request.");
							e.printStackTrace();
						}
					}
					if (request.contains("ftp")) {
						System.out.println("Request: " + request);
						InputStream in = (InputStream) array[0];
						destinationHost = (String) array[1];
						destinationPort = (Integer) array[2];

						try {
							URL url = new URL(request.split(" ")[1]);
							URLConnection connection = (URLConnection) url
									.openConnection();
							connection.connect();

							int contentLength = connection.getContentLength();
							byte[] buffer = new byte[contentLength];
							int bytesRead;

							InputStream fromServer = connection
									.getInputStream();
							OutputStream toClient = clientSocket
									.getOutputStream();
							// fromServer.transferTo(toClient);
							while ((bytesRead = fromServer.read()) != -1) {
								toClient.write(buffer, 0, bytesRead);
							}

						} catch (Exception ex) {
							ex.printStackTrace();
						}

					}
				}

				else if (array[0] == null) {
					request = (String) array[3];
					destinationHost = request.split(" ")[1].split(":")[0];
					destinationPort = Integer
							.parseInt(request.split(" ")[1].split(":")[1]);

					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
							clientSocket.getOutputStream(), "ISO-8859-1");

					if (destinationHost != null && destinationPort == 443) {
						try (final Socket destinationSocket = new Socket(
								destinationHost, destinationPort);) {
							System.out.println(
									"Successfully connected to destination host: "
											+ destinationSocket.getInetAddress()
											+ ":"
											+ destinationSocket.getPort());
							outputStreamWriter.write(
									"HTTP/1.1 200 Connection established\r\n");
							outputStreamWriter
									.write("Proxy-agent: Simple/0.1\r\n");
							outputStreamWriter.write("\r\n");
							outputStreamWriter.flush();

							Thread clientToRemote = new Thread() {
								@Override
								public void run() {
									relayData(destinationSocket, clientSocket);
								}
							};
							clientToRemote.start();
							try {
								if (cFlag) {
									int read = clientSocket.getInputStream()
											.read();
									if (read != -1) {
										if (read != '\n') {
											destinationSocket.getOutputStream()
													.write(read);
										}
										relayData(clientSocket,
												destinationSocket);
									} else {
										if (!destinationSocket
												.isOutputShutdown()) {
											destinationSocket.shutdownOutput();
										}
										if (!clientSocket.isInputShutdown()) {
											clientSocket.shutdownInput();
										}
									}
								} else {
									relayData(clientSocket, destinationSocket);
								}
							} finally {
								try {
									clientToRemote.join();
									clientSocket.close();
									destinationSocket.close();
								} catch (InterruptedException e) {
									System.out.println("This is the error!");
									e.printStackTrace(); // TODO: implement
															// catch
								}
							}
						} catch (IOException ex) {
							System.out
									.println("Error processing HTTPS request.");
							ex.getStackTrace();
							outputStreamWriter
									.write("HTTP/1.1 502 Bad Gateway\r\n");
							outputStreamWriter
									.write("Proxy-agent: Simple/0.1\r\n");
							outputStreamWriter.write("\r\n");
							outputStreamWriter.flush();
							return;
						}
					}
				} else {
					System.out.println("Didn't capture host and port!");
				}
			} catch (IOException e) {
				e.printStackTrace(); // TODO: implement catch
			} finally {
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace(); // TODO: implement catch
				}
			}
		}

		private void forwardData(InputStream input, OutputStream output) {

			byte[] buffer = new byte[4096];
			int read;

			try {
				while ((read = input.read(buffer)) != -1) {
					output.write(buffer, 0, read);
					output.flush();
				}
			} catch (Exception e) {
				// Connection might be closed, ignore error
			}
		}

		private Object readRequest(Socket clientSocket) throws IOException {
			// TODO Auto-generated method stub
			Object[] objectArray = new Object[4];
			InputStream input = clientSocket.getInputStream();

			byte[] array = new byte[1024];
			int bytesRead = input.read(array);

			String request = new String(array, 0, bytesRead);

			InputStream stream = new ByteArrayInputStream(
					request.getBytes(Charset.forName("ISO-8859-1")));
			if (request.startsWith("GET") && request.contains("http")) {
				String host = "";
				URL url = null;
				int port = 0;
				String firstLine = request.split(" ")[1];
				try {

					url = new URL(firstLine);
					host = url.getHost();
					if (url.getPort() != -1) {
						port = url.getPort();
					} else {
						port = 80;
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					OutputStream out = clientSocket.getOutputStream();
					out.write("Unknown protocol".getBytes());
					e.printStackTrace();
				}
				stream = new ByteArrayInputStream(
						request.getBytes(Charset.forName("ISO-8859-1")));

				objectArray[0] = stream;
				objectArray[1] = host;
				objectArray[2] = port;
				objectArray[3] = request.split("\n")[0];

			} else if (request.startsWith("CONNECT")) {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				int next;
				loop : while ((next = (stream.read())) != -1) {
					if (cFlag && next == '\n') {
						cFlag = false;
						continue;
					}
					cFlag = false;
					switch (next) {
						case '\r' :
							cFlag = true;
							break loop;
						case '\n' :
							break loop;
						default :
							byteArrayOutputStream.write(next);
							break;
					}
				}
				objectArray[3] = byteArrayOutputStream.toString("ISO-8859-1");

			} else if (request.startsWith("GET") && request.contains("ftp")) {
				String host = "";
				URL url = null;
				int port = 0;
				String firstLine = request.split(" ")[1];
				try {

					url = new URL(firstLine);
					host = url.getHost();
					if (url.getPort() != -1) {
						port = url.getPort();
					} else {
						port = 21;
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					OutputStream out = clientSocket.getOutputStream();
					out.write("Unknown protocol".getBytes());
					e.printStackTrace();
				}
				stream = new ByteArrayInputStream(
						request.getBytes(Charset.forName("ISO-8859-1")));

				objectArray[0] = stream;
				objectArray[1] = host;
				objectArray[2] = port;
				objectArray[3] = request.split("\n")[0];
			}
			return objectArray;
		}

		protected static void relayData(Socket input, Socket output) {
			// TODO Auto-generated method stub
			try {
				InputStream inputStream = input.getInputStream();
				try {
					OutputStream outputStream = output.getOutputStream();
					try {
						byte[] buffer = new byte[4096];
						int read;
						do {
							read = inputStream.read(buffer);
							if (read > 0) {
								outputStream.write(buffer, 0, read);
								if (inputStream.available() < 1) {
									outputStream.flush();
								}
							}
						} while (read >= 0);
					} finally {
						if (!output.isOutputShutdown()) {
							output.shutdownOutput();
						}
					}
				} finally {
					if (!input.isInputShutdown()) {
						input.shutdownInput();
					}
				}
			} catch (IOException e) {
				e.printStackTrace(); // TODO: implement catch
			}
		}
	}
}
