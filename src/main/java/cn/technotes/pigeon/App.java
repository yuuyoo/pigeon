package cn.technotes.pigeon;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpServer;

import cn.technotes.pigeon.core.Banner;
import cn.technotes.pigeon.core.Config;
import cn.technotes.pigeon.core.Constants;
import cn.technotes.pigeon.core.RootHandler;
import cn.technotes.pigeon.util.SocketUtils;

public class App {

	private static Logger logger = LoggerFactory.getLogger(App.class);

	private static String CONTEXT_PATH = Constants.SLASH;

	/**
	 * 允许排队的最大TCP连接数，如果该值小于或等于零，则使用系统默认值
	 */
	private static int TCP_CONNECTION_MAX = 0;

	public static void main(String[] args) {
		// 输出 banner
		Banner.getInstance().print();

		Config config = Config.getInstance();

		int port = config.getPort();
		boolean isBind = SocketUtils.isBind(port);
		if (isBind) {
			logger.error("Server starting fail port {} was already in use.", port);
			System.exit(-1);
		}

		InetSocketAddress address = new InetSocketAddress(port);

		try {
			HttpServer server = HttpServer.create(address, TCP_CONNECTION_MAX);
			server.createContext(CONTEXT_PATH, new RootHandler());
			server.setExecutor(null);
			server.start();

			logger.info("Server started on port: {} (http) with context path {}", port, CONTEXT_PATH);
			logger.info("Server file resource root director: {}", config.getBaseDir());

		} catch (IOException e) {
			logger.error("Server starting error {}", e);
			System.exit(-1);
		}
	}

}
