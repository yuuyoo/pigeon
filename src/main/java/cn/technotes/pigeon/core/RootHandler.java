package cn.technotes.pigeon.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import cn.technotes.pigeon.Constants;

public class RootHandler implements HttpHandler {

	public static final String RESOURCE_ROOT_DIRECTOR = new File("").getAbsolutePath();

	@Override
	public void handle(HttpExchange exchange) throws UnsupportedEncodingException {
		URI uri = exchange.getRequestURI();
		String path = uri.getPath();
		String resource = RESOURCE_ROOT_DIRECTOR + path;
		File file = new File(resource);

		if (file.isFile()) {
			fileHandler(exchange, file);
		}

		if (file.isDirectory()) {
			directoryHandler(exchange, path, file);
		}
	}

	private void directoryHandler(HttpExchange exchange, String path, File directory) {
		StringBuilder builder = new StringBuilder();
		builder.append("<!DOCTYPE html>");
		builder.append("<html>");
		builder.append("<head>");
		builder.append("<meta http-equiv=\"Content-Type\" content=\"text/html\"; charset=\"utf-8\">");
		builder.append("<title>Index of " + path + "</title>");
		builder.append("</head>");
		builder.append("<body bgcolor=\"white\">");
		builder.append("<h1>Index of " + path + "</h1><hr><pre><a href=\"../\">../</a></br>");

		try (OutputStream os = exchange.getResponseBody()) {
			File[] files = directory.getCanonicalFile().listFiles();
			for (File file : files) {
				String filePath = file.getCanonicalPath();
				String name = filePath.substring(filePath.lastIndexOf("\\") + 1);

				// 过滤隐藏文件
				if (name.startsWith(Constants.DOT)) {
					continue;
				}
				name = URLDecoder.decode(name, "UTF-8");

				builder.append("<a href=\"" + URLEncoder.encode(name, "UTF-8") + "\">" + name);
				if (file.isDirectory()) {
					builder.append(Constants.SLASH);
				}
				builder.append("</a></br>");
			}

			builder.append("</pre><hr></body>");
			builder.append("</html>");
			String response = builder.toString();
			exchange.getResponseHeaders().add("Content-Type:", "text/html;charset=utf-8");

			byte[] bs = response.getBytes();
			exchange.sendResponseHeaders(200, bs.length);
			os.write(bs);
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void fileHandler(HttpExchange exchange, File file) {
		try (FileInputStream inputStream = new FileInputStream(file); OutputStream os = exchange.getResponseBody();) {
			exchange.getResponseHeaders().add("Content-Disposition", "attachment;filename=" + file.getName());
			exchange.sendResponseHeaders(200, file.length());
			byte[] fileBytes = new byte[(int) file.length()];
			inputStream.read(fileBytes);
			os.write(fileBytes);
			return;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
