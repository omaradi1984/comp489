/**
 * title: WebServer.java
 * description: a web server application.
 * date: December 25, 2023
 * @author Omar Zohouradi
 * @version 1.0
 */
/**
* DOCUMENTATION...
*/
/**
 *
 *<H1>Web Server</H1>
 *
 *<H2>Purpose and Description</H2>
 *
 *<P>
 * An application server that processes incoming requests for resources.
 *</P>
 *<P>
 * This http server provides the client with the requested resource if it is available in the root directory, otherwise it returns a 404 file not found! error.
 * The user will need to provide the port number and number of threads when starting the server application in the following way:
 * java MyWebServer.java <<port>> <<number of threads>>
 * Example: java MyWebServer.java 8000 80
 *</P>
 *
 *<DL>
 *<DT> Compiling and running instructions</DT>
 *<DT> Assuming SDK 1.8 (or later) and the CLASSPATH are set up properly.</DT>
 *<DT> Change to the directory containing the source code.</DT>
 *<DD> Compile:    javac MyWebServer.java</DD>
 *<DD> Run:        java MyWebServer  <<port>> <<number of threads>></DD>
 *<DD> Document:   javadoc MyWebServer.java</DD>
 *</DL>
 */
/**
*
* <H3>Classes</H3>
*
*<P>
* public class MyWebServer {<BR>
* This is the main public class for this http server. It includes several java libraries and private static methods to process incoming requests.
* example libraries:
* import java.io.File;
* import java.io.FileInputStream;
* import java.io.IOException;
* import java.io.OutputStream;
* import java.net.InetSocketAddress;
* import java.util.HashMap;
* import java.util.Map;
* import java.util.concurrent.Executors;
* import java.util.logging.Level;
* import java.util.logging.Logger;
* import com.sun.net.httpserver.HttpServer;
* import com.sun.net.httpserver.HttpHandler;
* import com.sun.net.httpserver.Headers;
* import com.sun.net.httpserver.HttpExchange;
*</P>
*
*<H2>Parameter arguments</H2>
*<P>
* protected static int PORT;
* protected static int THREAD_NUMBER;
*</P>
*<H3>Other static arguments</H3>
*<P>
* private static final Map<String, String> extensionToContentType = new HashMap<>();

* static {
* // Map common file extensions to MIME types
* extensionToContentType.put("html", "text/html; charset=UTF-8");
* extensionToContentType.put("txt", "text/plain; charset=UTF-8");
* extensionToContentType.put("jpg", "image/jpeg");
* extensionToContentType.put("png", "image/png");
* extensionToContentType.put("mp3", "audio/mpeg3");
* extensionToContentType.put("gif", "image/gif");
* extensionToContentType.put("mp4", "video/mp4");
* extensionToContentType.put("pdf", "application/pdf");
* }
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
 * Test case 1: Run the application and pass an available port and a number of threads.
 * EXPECTED:
 * If the user passed available port and a number of threads, the application runs as expected.
 * Test case 2: Using a browser, request any of the resources available in the resources folder:
 * Example: IP address:port/resource || 127.0.0.1:8000/testfile.html
 * EXPECTED:
 * The application returns the expected resource.
 * Test case 3: Using a browser, request a resource that isn't available in the resources folder:
 * Example: IP address:port/resource || 127.0.0.1:8000/testfile.wmv
 * EXPECTED:
 * The application returns a 404 file not found error message.
 *</P>
 */
/**
 * CODE...
 */
/** Java core packages */
package assignment_final;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * @author ozohouradi
 *
 */
public class WebServer {

	/**
	 * @param args
	 */
	protected static int PORT;
	protected static int THREAD_NUMBER;

	private static final Map<String, String> extensionToContentType = new HashMap<>();

	static {
		// Map common file extensions to MIME types
		extensionToContentType.put("html", "text/html; charset=UTF-8");
		extensionToContentType.put("txt", "text/plain; charset=UTF-8");
		extensionToContentType.put("jpg", "image/jpeg");
		extensionToContentType.put("png", "image/png");
		extensionToContentType.put("mp3", "audio/mpeg3");
		extensionToContentType.put("gif", "image/gif");
		extensionToContentType.put("mp4", "video/mp4");
		extensionToContentType.put("pdf", "application/pdf");

	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if (args.length == 2) {
			PORT = Integer.parseInt(args[0]);
			THREAD_NUMBER = Integer.parseInt(args[1]);

			HttpServer server = HttpServer.create(new InetSocketAddress(PORT),
					0);
			server.createContext("/", new MyHandler());

			// Set the executor to a ThreadPoolExecutor to handle multiple
			// requests
			server.setExecutor(Executors.newFixedThreadPool(THREAD_NUMBER)); // create
																				// a
																				// pool
																				// of
																				// threads

			server.start();

			System.out.println("Server started on port " + PORT
					+ " with multithreading enabled (" + THREAD_NUMBER
					+ " threads).");
		} else {
			System.out.println("Provide a port number and number of threads!");
			System.exit(1);
		}

	}

	static class MyHandler implements HttpHandler {
		private static final String ROOT = "./resources"; // Set your file
															// directory path
															// here

		@Override
		public void handle(HttpExchange t) throws IOException {
			// Extract the requested file name from the URI
			String filePath = t.getRequestURI().getPath();
			File file = new File(ROOT + filePath).getCanonicalFile();

			// Check if the file exists and is not a directory
			if (!file.isFile()) {
				// Object does not exist or is not a file: reply with 404 error.
				String response = "404 (File Not Found)\n";
				t.sendResponseHeaders(404, response.length());
				try (OutputStream os = t.getResponseBody()) {
					os.write(response.getBytes());
					// Logging request
					LogWriter.logAccess(t, file, filePath);
				} catch (IOException ex) {
					LogWriter.logError(t, filePath, ex);
				} catch (RuntimeException ex) {
					LogWriter.logError(t, filePath, ex);
				}
			} else {
				// Object exists and is a file: reply with 200 OK
				String fileExtension = filePath
						.substring(filePath.lastIndexOf('.') + 1);
				// Determine the Content-Type based on the file extension
				String contentType = extensionToContentType.getOrDefault(
						fileExtension, "application/octet-stream");
				// Set the Content-Type header in the response
				Headers responseHeaders = t.getResponseHeaders();
				responseHeaders.set("Content-Type", contentType);
				t.sendResponseHeaders(200, file.length());
				try (OutputStream os = t.getResponseBody();
						FileInputStream fs = new FileInputStream(file)) {
					final byte[] buffer = new byte[1024];
					int count;
					while ((count = fs.read(buffer)) >= 0) {
						os.write(buffer, 0, count);
					}
					// Logging request
					LogWriter.logAccess(t, file, filePath);
				} catch (IOException e) {
					LogWriter.logError(t, filePath, e);
				}
			}
		}
	}
	static class LogWriter {
		private static final String ACCESS_LOG_FILE_NAME = "access_log";
		private static final String ERROR_LOG_FILE_NAME = "error_log";
		private static final Logger accessLogger = Logger
				.getLogger(WebServer.class.getName() + ".accessLogger");
		private static final Logger errorLogger = Logger
				.getLogger(WebServer.class.getName() + ".errorLogger");
		private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd/MMM/yyyy:HH:mm:ss");
		private static FileHandler fh = null;

		public static void logAccess(HttpExchange t, File file,
				String filePath) {
			String timeStamp = dateFormat.format(new Date()).replace(".", "");
			Instant instant = Instant.now();
			String timezone = ZoneId.systemDefault().getRules()
					.getOffset(instant).toString().replace(":", "");
			String remoteAddress = t.getRemoteAddress().getHostString();
			String method = t.getRequestMethod();
			String protocol = t.getProtocol();
			int responseCode = t.getResponseCode();
			long fileSize = file.length();
			String message = remoteAddress + " - - " + "[" + timeStamp + " "
					+ timezone + "] \"" + method + " " + filePath + " "
					+ protocol + "\" " + responseCode + " " + fileSize;
			try {
				fh = new FileHandler(ACCESS_LOG_FILE_NAME, true);
				fh.setFormatter(new Formatter() {
					@Override
					public String format(LogRecord record) {
						return record.getMessage() + "\n";
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			accessLogger.setLevel(Level.INFO);
			// auditLogger.setUseParentHandlers(false);
			accessLogger.addHandler(fh);
			accessLogger.info(message);
			fh.flush();
			fh.close();
		}

		public static void logError(HttpExchange t, String filePath, Exception e) {
			String timeStamp = dateFormat.format(new Date()).replace(".", "");
			Instant instant = Instant.now();
			String timezone = ZoneId.systemDefault().getRules()
					.getOffset(instant).toString().replace(":", "");
			String remoteAddress = t.getRemoteAddress().getHostString();
			String method = t.getRequestMethod();
			String protocol = t.getProtocol();
			int responseCode = t.getResponseCode();
			String message = remoteAddress + " - - " + "[" + timeStamp + " "
					+ timezone + "] \"" + method + " " + filePath + " "
							+ protocol + "\" "
					+ responseCode+ " || " + e.getMessage();
			try {
				fh = new FileHandler(ERROR_LOG_FILE_NAME, true);
				fh.setFormatter(new Formatter() {
					@Override
					public String format(LogRecord record) {
						return record.getLevel() + " " + record.getMessage()
								+ "\n";
					}
				});
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			errorLogger.setLevel(Level.INFO);
			errorLogger.addHandler(fh);
			errorLogger.warning(message);
			fh.flush();
			fh.close();
		}
	}
}