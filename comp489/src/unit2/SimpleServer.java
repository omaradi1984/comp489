package unit2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {

	protected int portNo = 3000;
	protected ServerSocket clientConnect;

	public SimpleServer(int port) throws IllegalArgumentException {
		if (port <= 0) throw new IllegalArgumentException(
				"Bad port number given to SimpleServer constructor.");

		System.out.println("Connecting server socket to port...");
		try {clientConnect = new ServerSocket(port);
		}catch (IOException e) {
		System.out.println("Failed to connect to port " + port);
		System.exit(1);
		}
		this.portNo = port;
	}

	public static void main(String argv[]) {
		// TODO Auto-generated method stub
		int port = 6000;
		if (argv.length > 0) {
			int tmp = port;
			try {
				tmp = Integer.parseInt(argv[0]);
			}catch (NumberFormatException e) {
		}
			port = tmp;
	}
		SimpleServer server = new SimpleServer(port);
		System.out.println("SimpleServer running on port " + port + "...");
		server.listen();
}
	public void listen() {
		try {
			System.out.println("Waiting for clients...");
			while (true) {
				Socket clientReq = clientConnect.accept();
				System.out.println("Got a client...");
				serviceClient(clientReq);
			}
		}catch (IOException e) {
			System.out.println("IO Exception while listening for clients.");
			System.exit(1);
		}
	}
	public void serviceClient(Socket clientConn) {
		SimpleCmdInputStream inStream = null;
		DataOutputStream outStream = null;
		try {
			inStream = new SimpleCmdInputStream(clientConn.getInputStream());
			outStream = new DataOutputStream(clientConn.getOutputStream());
		}catch (IOException e) {
			System.out.println("SimpleServer: Error getting I/O streams.");
		}

		SimpleCmd cmd = null;
		System.out.println("Attempting to read commands...");
		while (cmd == null || !(cmd instanceof SimpleCmd)) {
			try { cmd  = inStream.readCommand();
		}catch (IOException e) {
			System.out.println("SimpleServer: " + e);
			System.exit(1);
		}
			if (cmd != null) {
				String result = cmd.Do();
				try{outStream.writeBytes(result);
			}catch (IOException e) {
				System.out.println("SimpleServer: " + e);
				System.exit(1);
				}
			}
		}
	}
	@Override
	public synchronized void finalize() {
		System.out.println("Shutting down SimpleServer running on port " + portNo);
	}
}

