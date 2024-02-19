package assignment_final;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 */
public class FtpServer {
	private static Socket clientSocket;
    private static boolean previousWasR = false;

    public static void main(String[] args) {
        
    	int port = 6000;
    	String server = "ftp.dlptest.com";
    	int destinationPort = 21;
    	
    	
    	try(ServerSocket serverSocket = new ServerSocket(port);) {
			System.out.println("Started FTP proxy server at " + serverSocket.getInetAddress());
    		run:
    		while(true) {
    			clientSocket = serverSocket.accept();
    			
    			String request = readLine(clientSocket);
                System.out.println(request);

                    String header;
                    do {
                        header = readLine(clientSocket);
                    } while (!"".equals(header));
                    try {
						OutputStreamWriter outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream(),
						                                                               "ISO-8859-1");
						
						outputStreamWriter.write("HTTP/1.1 220 Connection established\r\n");
		                outputStreamWriter.write("Proxy-agent: Simple/0.1\r\n");
		                outputStreamWriter.write("\r\n");
		                outputStreamWriter.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

    			
    			Socket destinationSocket = new Socket(server, destinationPort);
    			System.out.println(destinationSocket);
    			
    			
    			
    			forwardData(destinationSocket, clientSocket);
    			
    			try {
                    if (previousWasR) {
                        int read = clientSocket.getInputStream().read();
                        if (read != -1) {
                            if (read != '\n') {
                                destinationSocket.getOutputStream().write(read);
                            }
                            forwardData(clientSocket, destinationSocket);
                        } else {
                            if (!destinationSocket.isOutputShutdown()) {
                                destinationSocket.shutdownOutput();
                            }
                            if (!clientSocket.isInputShutdown()) {
                                clientSocket.shutdownInput();
                            }
                        }
                    } else {
                        forwardData(clientSocket, destinationSocket);
                    }
    		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    	} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    private static void forwardData(Socket inputSocket, Socket outputSocket) {
        try {
            InputStream inputStream = inputSocket.getInputStream();
            try {
                OutputStream outputStream = outputSocket.getOutputStream();
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
                    if (!outputSocket.isOutputShutdown()) {
                        outputSocket.shutdownOutput();
                    }
                }
            } finally {
                if (!inputSocket.isInputShutdown()) {
                    inputSocket.shutdownInput();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // TODO: implement catch
        }
    }

    private static String readLine(Socket socket) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int next;
        loop:
        while ((next = socket.getInputStream().read()) != -1) {
            if (previousWasR && next == '\n') {
                previousWasR = false;
                continue;
            }
            previousWasR = false;
            switch (next) {
                case '\r':
                    previousWasR = true;
                    break loop;
                case '\n':
                    break loop;
                default:
                    byteArrayOutputStream.write(next);
                    break;
            }
        }
        return byteArrayOutputStream.toString("ISO-8859-1");
    }
}