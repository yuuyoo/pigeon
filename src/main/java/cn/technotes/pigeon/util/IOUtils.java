package cn.technotes.pigeon.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import cn.technotes.pigeon.core.Charsets;

public class IOUtils {

	public static List<String> readLines(final InputStream input, final String charsetName) throws IOException {
		return readLines(input, Charsets.toCharset(charsetName));
	}

	public static List<String> readLines(final InputStream input, final Charset charset) throws IOException {
		final InputStreamReader reader = new InputStreamReader(input, Charsets.toCharset(charset));
		return readLines(reader);
	}

	public static List<String> readLines(final Reader reader) throws IOException {
		final BufferedReader bufReader = toBufferedReader(reader);
		final List<String> list = new ArrayList<>();
		String line;
		while ((line = bufReader.readLine()) != null) {
			list.add(line);
		}
		return list;
	}

	public static BufferedReader toBufferedReader(final Reader reader) {
		return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
	}

}
