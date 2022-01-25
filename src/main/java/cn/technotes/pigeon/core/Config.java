package cn.technotes.pigeon.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

	private static Logger logger = LoggerFactory.getLogger(Config.class);

	private static final String DEFAULT_BASE_DIR = new File("").getAbsolutePath();
	private static final int DEFAULT_PORT = 9090;
	private static final String CONFIG_FILE = "config.properties";
	private String baseDir;
	private int port;
	private static Config INSTANCE;

	private Config(String baseDir, int port) {
		this.baseDir = baseDir;
		this.port = port;
	}

	public static synchronized Config getInstance() {
		if (null != INSTANCE) {
			return INSTANCE;
		}

		try (InputStream is = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
			if (null != is) {
				Properties prop = new Properties();
				prop.load(is);
				String baseDir = prop.getProperty("base.dir");
				if ("".equals(baseDir) || ".".equals(baseDir)) {
					baseDir = DEFAULT_BASE_DIR;
				}
				String port = prop.getProperty("port");
				INSTANCE = new Config(baseDir, Integer.valueOf(port).intValue());
			}
		} catch (IOException e) {
			logger.warn("Load config file {} fail {}", CONFIG_FILE, e);
			INSTANCE = new Config(DEFAULT_BASE_DIR, DEFAULT_PORT);
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
