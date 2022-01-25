package cn.technotes.pigeon.core;

import java.io.File;

public class Config {
	private static final String DEFAULT_BASE_DIR = new File("").getAbsolutePath();
	private static final int DEFAULT_PORT = 9090;
	private String baseDir;
	private int port;
	private static Config INSTANCE;

	private Config() {

	}

	private Config(String baseDir, int port) {
		this.baseDir = baseDir;
		this.port = port;
	}

	public static synchronized Config getInstance() {
		return getInstance(DEFAULT_BASE_DIR, DEFAULT_PORT);
	}

	public static synchronized Config getInstance(String baseDir, int port) {
		if (null == INSTANCE) {
			if (".".equals(baseDir) || "".equals(baseDir)) {
				baseDir = DEFAULT_BASE_DIR;
			}
			INSTANCE = new Config(baseDir, port);
		}
		return INSTANCE;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public int getPort() {
		return port;
	}

}
