package com.itar.soa.biwan.support.jaxrsFastjson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {

	/**
	 * read a String from an InputStream object.
	 * @param in InputStream
	 * @return String
	 * @throws Exception
	 */
	public static String inputStreamToString(InputStream in) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuffer buffer = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}
}
