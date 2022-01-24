package cn.technotes.pigeon.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.technotes.pigeon.util.IOUtils;

public class Banner {

	private static Logger logger = LoggerFactory.getLogger(Banner.class);

	private String bannerFile = "banner.txt";

	public Banner() {
	}

	public static Banner getInstance() {
		return new Banner();
	}

	public void print() {
		print(bannerFile);
	}

	public void print(String banner) {
		try (InputStream is = Banner.class.getClassLoader().getResourceAsStream(banner)) {
			if (null != is) {
				List<String> lines = IOUtils.readLines(is, Constants.DEFAULT_CHARSET);
				lines.forEach(System.out::println);
				System.out.println();
				return;
			}
		} catch (IOException e) {
			logger.warn("Load banner file {} fail {}", banner, e);
		}

		logger.warn("Banner file {}  not found", banner);
	}

}
