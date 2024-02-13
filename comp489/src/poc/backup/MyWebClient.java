/**
 * title: MyWebClient.java
 * description: an HTTP client application.
 * date: December 25, 2023
 * @author Omar Zohouradi
 * @version 1.0
 *
 *I declare that this assignment is my own work and that all material previously written or published in any source by any other person has been duly acknowledged in the assignment. I have not submitted this work, or a significant part thereof, previously as part of any academic program. In submitting this assignment I give permission to copy it for assessment purposes only.
 */
/**
* DOCUMENTATION...
*/
/**
 *
 *<H1>Web Client</H1>
 *
 *<H3>Purpose and Description</H3>
 *
 *<P>
 * An application that connects to an http server to request a resource.
 *</P>
 *<P>
 * This program uses the http protocol to interact with an http server and request resources using GET method.
 * For html pages, the application will parse on the console the html code of the resource. For multimedia resources, the application will download the resource
 * and save it in the directory path provided as an argument when running the application.
 * To run the application:
 * java MyWebClient.java http://<<IP>>:<<port>>/<<resource>> (optional) <<directory path>>
 * Example: java MyWebClient.java http://127.0.0.1:8000/testfile.html ./ (this will save the resource in the same folder as the application)
 *</P>
 *<DL>
 *<DT> Compiling and running instructions</DT>
 *<DT> Assuming SDK 1.8 (or later) and the CLASSPATH are set up properly.</DT>
 *<DT> Change to the directory containing the source code.</DT>
 *<DD> Compile:    javac MyWebClient.java</DD>
 *<DD> Run:        java MyWebClient http://<<IP>>:<<port>>/<<resource>> (optional) <<directory path>></DD>
 *<DD> Document:   javadoc MyWebClient.java</DD>
 *</DL>
 */

/**
 * CODE...
 */

/** Java core packages */
package poc.backup;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class MyWebClient {
	/**
	 * @param args
	 *            args[0] = port, args[1] = proxyAddress, args[2] = proxyPort
	 *            args[3] = resource (host server), args[4] = filePath
	 *            (optional)
	 */
	protected static int port, proxyPort;
	protected static String proxyAddress, remoteResource, filePath = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 4) {
			System.out.println(
					"Please provide the minimum required arguments\nClient port, Proxy server address, "
					+ "proxy server port, and URL to desired resource"
					+ " in the host server.\n(Optional) path to the directory where you want the resource to be saved.\n"
					+ "For example: java MyWebClient.java <port> <proxy address> <proxy port> <resource URL> (optional) <directory path>");
			System.exit(1);
		}

		port = Integer.parseInt(args[0]);
		proxyAddress = args[1];
		proxyPort = Integer.parseInt(args[2]);
		remoteResource = args[3];

		/*System.setProperty("ftp.proxyHost", proxyAddress);
        System.setProperty("ftp.proxyPort", String.valueOf(proxyPort));
        System.setProperty("http.proxyHost", proxyAddress);
        System.setProperty("http.proxyPort", String.valueOf(proxyPort));*/

		try {
			URL url = new URL(remoteResource);
			String protocolType = url.getProtocol();
			if(protocolType == "ftp") {
				 SocksSocket sock;
			}
			SocketAddress addr = new InetSocketAddress(proxyAddress, proxyPort);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);


			System.out.println("Protocol: " + url.getProtocol().toString());
			// Openning a connection



			URLConnection connection = url.openConnection();
			InputStream input;
			System.out.println("requested resource: " + url.toString());
			if (args.length == 5) {
				filePath = args[4];
				int contentLength = connection.getContentLength();
				try (InputStream raw = connection.getInputStream()) {
					input = new BufferedInputStream(raw);
					byte[] data = new byte[contentLength];
					int offset = 0;
					int oldProgress = 0;
					int currentProgress = 0;
					while (offset < contentLength) {
						int bytesRead = input.read(data, offset,
								data.length - offset);
						if (bytesRead == -1)
							break;
						offset += bytesRead;
						oldProgress = (int) ((((double) offset)
								/ ((double) contentLength)) * 100d);
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
			}

			int num = 0;
			input = connection.getInputStream();
			byte[] bArry = new byte[1024];
			num = input.read(bArry);
			String response = new String(bArry, 0, num);
			input.close();
			System.out.println("The HTTP server response is:\n" + response);

		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}