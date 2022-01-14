package cn.technotes.pigeon;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import cn.technotes.pigeon.core.RootHandler;

public class App {

	public static void main(String[] args) {
		int port = 9090;
		InetSocketAddress address = new InetSocketAddress(port);
		// 允许排队的最大TCP连接数，如果该值小于或等于零，则使用系统默认值
		int num = 0;
		try {
			HttpServer server = HttpServer.create(address, num);
			server.createContext("/", new RootHandler());
			server.setExecutor(null);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
