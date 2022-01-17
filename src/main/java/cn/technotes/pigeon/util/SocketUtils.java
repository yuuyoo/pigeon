package cn.technotes.pigeon.util;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author duanjiliang
 * @date 2022年1月14日
 * @version 1.0
 */
public class SocketUtils {

	private static Logger logger = LoggerFactory.getLogger(SocketUtils.class);

	/**
	 * 随机端口最新值
	 */
	public static final int PORT_RANGE_MIN = 1024;

	/**
	 * 随机端口最大值
	 */
	public static final int PORT_RANGE_MAX = 65535;

	private static final Random random = new Random(System.currentTimeMillis());

	/**
	 * 检查指定的端口是否已使用
	 * 
	 * @param port
	 * @return
	 * @author duanjiliang
	 * @date 2022年1月14日
	 */
	public static boolean isBind(int port) {
		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return false;
		} catch (IOException e) {
			logger.error("check bind port {} error {}", port, e);
		} finally {
			if (ds != null) {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					logger.error("close ServerSocket port {} error {}", port, e);
				}
			}
		}

		return true;
	}

	/**
	 * 查找一个可用的端口
	 * 
	 * @return
	 * @author duanjiliang
	 * @date 2022年1月14日
	 */
	public static int findAvaliablePort() {
		return findAvailablePort(PORT_RANGE_MIN, PORT_RANGE_MAX);
	}

	private static int findAvailablePort(int minPort, int maxPort) {
		int portRange = maxPort - minPort;
		int candidatePort;
		int searchCounter = 0;
		do {
			if (searchCounter > portRange) {
				throw new IllegalStateException(String.format("Could not find an available %s port in the range [%d, %d] after %d attempts", "",
						minPort, maxPort, searchCounter));
			}
			candidatePort = randomPort(minPort, maxPort);
			searchCounter++;
		} while (isBind(candidatePort));

		return candidatePort;
	}

	private static int randomPort(int minPort, int maxPort) {
		int portRange = maxPort - minPort;
		return minPort + random.nextInt(portRange + 1);
	}
}
