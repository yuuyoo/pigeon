package cn.technotes.pigeon;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import cn.technotes.pigeon.core.RootHandler;
import cn.technotes.pigeon.util.SocketUtils;

public class App {

	public static void main(String[] args) {
		int port = 9090;

		boolean isBind = SocketUtils.isBind(port);
		if (isBind) {
			System.out.println("端口 port : " + port + " 已占用");
			port = SocketUtils.findAvaliablePort();
		}

		InetSocketAddress address = new InetSocketAddress(port);
		// 允许排队的最大TCP连接数，如果该值小于或等于零，则使用系统默认值
		int num = 0;
		try {
			HttpServer server = HttpServer.create(address, num);
			server.createContext("/", new RootHandler());
			server.setExecutor(null);
			server.start();

			System.out.println("服务已启动，监听端口：" + port);
			System.out.println("资源根目录：" + RootHandler.RESOURCE_ROOT_DIRECTOR);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
