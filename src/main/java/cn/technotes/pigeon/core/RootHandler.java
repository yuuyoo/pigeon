package cn.technotes.pigeon.core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RootHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {

		File directory = new File("");
		String path = directory.getCanonicalPath();
		String pathName = path.substring(path.lastIndexOf("\\") + 1);

		StringBuilder builder = new StringBuilder();
		builder.append("<!DOCTYPE html>");
		builder.append("<html>");
		builder.append("<head><title>Index of /" + pathName + "/</title></head>");
		builder.append("<body bgcolor=\"white\">");
		builder.append("<h1>Index of /" + pathName + "/</h1><hr><pre><a href=\"../\">../</a></br>");

		File[] files = directory.getCanonicalFile().listFiles();
		for (File file : files) {
			String filePath = file.getCanonicalPath();
			String name = filePath.substring(filePath.lastIndexOf("\\") + 1);
			builder.append("<a href=\"" + name + "/\">" + name + "/</a></br>");
		}

		builder.append("</pre><hr></body>");
		builder.append("</html>");
		String response = builder.toString();
		exchange.getResponseHeaders().add("Content-Type:", "text/html;charset=utf-8");
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
