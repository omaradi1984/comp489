package unit2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleClient {

	protected Socket serverConn;

	protected SimpleCmdInputStream inStream;

	public SimpleClient(String host, int port) throws IllegalArgumentException{
		try {
			System.out.println("Trying to connect to " + host + " " + port);
			serverConn = new Socket(host, port);
		}catch (UnknownHostException e) {
			throw new IllegalArgumentException("Bad host name given.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("SimpleClient: " + e);
			System.exit(1);
		}

		System.out.println("Made server connection.");
	}

	public static void main(String argv[]) {
		// TODO Auto-generated method stub
		if (argv.length < 2) {
			System.out.println("Usage: java SimpleClient 127.0.0.1 6000");
			System.exit(1);
		}

		String host = argv[0];
		int port = 6000;
		try {
			port = Integer.parseInt(argv[1]);
		}catch (NumberFormatException e) {}

		SimpleClient client = new SimpleClient(host, port);
		client.sendCommands();
	}

	public void sendCommands() {
		try {
			OutputStreamWriter wout = new OutputStreamWriter(serverConn.getOutputStream());
			BufferedReader rin = new BufferedReader(new InputStreamReader(serverConn.getInputStream()));

			wout.write("GET goodies ");
			String result = rin.readLine();
			System.out.println("Server says: \"" + result + "\"");

			wout.write("POST goodies ");
			result = rin.readLine();
			System.out.println("Server says: \"" + result + "\"");

			wout.write("DONE ");
			result = rin.readLine();
			System.out.println("Server says: \"" + result + "\"");
		}catch (IOException e) {
			System.out.println("SimpleClient: " + e);
			System.exit(1);
		}
	}
	@Override
	public synchronized void finalize() {
		System.out.println("Closing down SimpleClient...");
		try {
			serverConn.close();}
		catch (IOException e) {
			System.out.println("SimpleClient: " + e);
			System.exit(1);
		}
	}

}
