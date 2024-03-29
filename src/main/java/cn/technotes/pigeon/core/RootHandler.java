package cn.technotes.pigeon.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RootHandler implements HttpHandler {

	private static Logger logger = LoggerFactory.getLogger(RootHandler.class);

	@Override
	public void handle(HttpExchange exchange) throws UnsupportedEncodingException {

		InetSocketAddress address = exchange.getLocalAddress();
		InetAddress inetAddress = address.getAddress();
		String ip = inetAddress.getHostAddress();
		String method = exchange.getRequestMethod();
		URI uri = exchange.getRequestURI();
		String path = URLDecoder.decode(uri.getRawPath(), Constants.DEFAULT_CHARSET);
		logger.info("client ip: {} method: {} path: {}", ip, method, path);

		String resource = Config.getInstance().getBaseDir() + path;
		File file = new File(resource);

		if (file.isFile()) {
			fileHandler(exchange, file);
			return;
		}

		if (file.isDirectory()) {
			directoryHandler(exchange, path, file);
			return;
		}

		notfoundHandler(exchange, resource);
	}

	private void notfoundHandler(HttpExchange exchange, String resource) {
		logger.info("resource {} not exist", resource);
		try (OutputStream os = exchange.getResponseBody()) {
			StringBuilder builder = new StringBuilder();
			builder.append("<!DOCTYPE html>");
			builder.append("<html>");
			builder.append("<head>");
			builder.append("<meta http-equiv=\"Content-Type\" content=\"text/html\"; charset=\"utf-8\">");
			builder.append("<title>Not Found</title>");
			builder.append("</head>");
			builder.append("<body bgcolor=\"white\">");
			builder.append("<h1>Not Found</h1>");
			builder.append("</body>");
			builder.append("</html>");
			String response = builder.toString();
			exchange.getResponseHeaders().add("Content-Type:", "text/html;charset=utf-8");
			byte[] bs = response.getBytes();
			exchange.sendResponseHeaders(404, bs.length);
			os.write(bs);
		} catch (IOException e) {
			logger.error("reponse 404 page error: {}", e);
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
		builder.append("<h1>Index of " + path + "</h1><hr>");
		String parentPath = path.substring(0, path.lastIndexOf(Constants.SLASH));
		if (null == parentPath || "".equals(parentPath)) {
			parentPath = Constants.SLASH;
		}
		builder.append("<pre><a href=\"" + parentPath + "\">../</a></br>");

		try (OutputStream os = exchange.getResponseBody()) {
			File[] files = directory.getCanonicalFile().listFiles();
			for (File file : files) {
				String filePath = file.getCanonicalPath();
				String name = filePath.substring(filePath.lastIndexOf("\\") + 1);

				// 过滤隐藏文件
				if (name.startsWith(Constants.DOT)) {
					continue;
				}
				name = URLDecoder.decode(name, Constants.DEFAULT_CHARSET);

				builder.append("<a href=\"");
				if (!path.equals(Constants.SLASH)) {
					builder.append(path + Constants.SLASH);
				}
				builder.append(URLEncoder.encode(name, Constants.DEFAULT_CHARSET) + "\">" + name);
				if (file.isDirectory()) {
					builder.append(Constants.SLASH);
				}
				builder.append("</a></br>");
			}

			builder.append("</pre><hr></body>");
			builder.append("</html>");
			String response = builder.toString();
			exchange.getResponseHeaders().add("Content-Type:", "text/html;charset=utf-8");

			byte[] bs = response.getBytes(Constants.DEFAULT_CHARSET);
			exchange.sendResponseHeaders(200, bs.length);
			os.write(bs);
		} catch (IOException e) {
			logger.error("directory {} handle error: {}", path, e);
		}
	}

	private void fileHandler(HttpExchange exchange, File file) {
		try (FileInputStream inputStream = new FileInputStream(file); OutputStream os = exchange.getResponseBody();) {
			String fileName = URLEncoder.encode(file.getName(), Constants.DEFAULT_CHARSET);
			exchange.getResponseHeaders().add("Content-Disposition", "attachment;filename=" + fileName);
			exchange.sendResponseHeaders(200, file.length());
			byte[] fileBytes = new byte[(int) file.length()];
			inputStream.read(fileBytes);
			os.write(fileBytes);
		} catch (FileNotFoundException e) {
			logger.error("file {} not exist", file.getName());

		} catch (IOException e) {
			logger.error("file {} handle error: {}", file.getName(), e);
		}
	}

}
