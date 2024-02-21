package assignment1;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FtpServer {

    private static final int proxyPort = 6000; // Port for the proxy server control connection
    private static final String targetFTPServer = "ftp.dlptest.com"; // Target FTP server
    private static final int targetFTPPort = 21; // FTP server port

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(proxyPort)) {
            System.out.println("FTP Proxy Server started on port " + proxyPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            Socket serverSocket = new Socket(targetFTPServer, targetFTPPort);
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "ISO-8859-1"));
            BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "ISO-8859-1"));
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream(), "ISO-8859-1"));
            BufferedWriter serverWriter = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream(), "ISO-8859-1"));
        ) {
            // Start a thread to relay server responses to the client
            new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = serverReader.readLine()) != null) {
                        // Intercept and modify PASV response
                        if (serverResponse.startsWith("227")) {
                            serverResponse = handlePASVResponse(serverResponse, clientSocket);
                        }
                        clientWriter.write(serverResponse + "\r\n");
                        clientWriter.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Relay client commands to the server
            String clientCommand;
            while ((clientCommand = clientReader.readLine()) != null) {
                serverWriter.write(clientCommand + "\r\n");
                serverWriter.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String handlePASVResponse(String pasvResponse, Socket clientSocket) throws IOException {
        // Extract the IP address and port from the PASV response
        Pattern p = Pattern.compile("\\((\\d+,\\d+,\\d+,\\d+),(\\d+),(\\d+)\\)");
        Matcher m = p.matcher(pasvResponse);
        if (!m.find()) return pasvResponse; // If the pattern does not match, return the original response

        // Start a server socket to listen for the data connection
        ServerSocket dataServerSocket = new ServerSocket(0); // Listen on any available port
        String newHost = clientSocket.getLocalAddress().getHostAddress().replace(".", ",");
        int newPort = dataServerSocket.getLocalPort();
        int p1 = newPort / 256;
        int p2 = newPort % 256;

        // Replace the IP address and port in the PASV response
        String modifiedResponse = "227 Entering Passive Mode (" + newHost + "," + p1 + "," + p2 + ")";

        // Start a thread to relay data between the client and the FTP server
        new Thread(() -> relayDataConnection(dataServerSocket)).start();

        return modifiedResponse;
    }

    private static void relayDataConnection(ServerSocket dataServerSocket) {
        try (Socket clientDataSocket = dataServerSocket.accept();
             Socket serverDataSocket = new Socket(targetFTPServer, dataServerSocket.getLocalPort())) {
            InputStream clientIn = clientDataSocket.getInputStream();
            OutputStream clientOut = clientDataSocket.getOutputStream();
            InputStream serverIn = serverDataSocket.getInputStream();
            OutputStream serverOut = serverDataSocket.getOutputStream();

            // Relay data from client to server
            new Thread(() -> relayData(clientIn, serverOut)).start();
            // Relay data from server to client
            relayData(serverIn, clientOut);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void relayData(InputStream in, OutputStream out) {
        byte[] buffer = new byte[4096];
        int bytesRead;
        try {
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
