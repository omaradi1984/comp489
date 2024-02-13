package poc.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new MyHandler());

        // Set the executor to a ThreadPoolExecutor to handle multiple requests
        server.setExecutor(Executors.newFixedThreadPool(10)); // create a pool of threads

        server.start();

        System.out.println("Server started on port 8000 with multithreading enabled");
	}
}

class MyHandler implements HttpHandler {
    private static final String ROOT = "./resources"; // Set your file directory path here

    @Override
    public void handle(HttpExchange t) throws IOException {
        // Extract the requested file name from the URI
        String filePath = t.getRequestURI().getPath();
        System.out.println(filePath);
        File file = new File(ROOT + filePath).getCanonicalFile();
        System.out.println("requested file: " + file.getCanonicalPath().toString());

        // Check if the file exists and is not a directory
        if (!file.isFile()) {
            // Object does not exist or is not a file: reply with 404 error.
            String response = "404 (File Not Found)\n";
            t.sendResponseHeaders(404, response.length());
            try (OutputStream os = t.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else {
            // Object exists and is a file: reply with 200 OK
            t.sendResponseHeaders(200, file.length());
            try (OutputStream os = t.getResponseBody(); FileInputStream fs = new FileInputStream(file)) {
                final byte[] buffer = new byte[0x10000];
                int count;
                while ((count = fs.read(buffer)) >= 0) {
                    os.write(buffer, 0, count);
                }
            }
        }
    }
}
