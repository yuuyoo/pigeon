package cn.technotes.pigeon.core;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RootHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String response = "<h1>hello, pigeon</h1>";
		exchange.sendResponseHeaders(200, response.length());
		OutputStream os = null;
		try {
			os = exchange.getResponseBody();
			os.write(response.getBytes());
		} catch (IOException e) {
			System.out.println("root handler wirte reponse error:" + e.getMessage());
		} finally {
			os.close();
		}
	}

}
