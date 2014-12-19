package org.jas.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * File Manager
 *
 *
 * @author 張　学軍
 * @version 1.0
 */

public class FileManager {

	/**
	 * read file content and convert to StringBuffer
	 *
	 * @param fileName the file full name
	 * @return StringBuffer
	 *
	 * @exception IOException
	 */
	public static synchronized StringBuffer readInputStream(String fileName)
							throws IOException {
		FileInputStream fis = new FileInputStream(fileName);
		StringBuffer sb = readInputStream(fis);
		fis.close();

		return sb;
	}

	/**
	 * read inputstream and convert to StringBuffer
	 *
	 * @param is InputStream
	 * @return StringBuffer
	 * @exception IOException
	 */
	public static synchronized StringBuffer readInputStream(InputStream is)
							throws IOException {
		StringBuffer sb = new StringBuffer();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);

		String line = reader.readLine();
		while(line != null) {
			sb.append(line + "\n");
			line = reader.readLine();
		}

		return sb;
	}

	/**
	 * read encoded file content from class path
	 *
	 */
	public static synchronized ArrayList readInputStream(String classPath, String encode)
							throws IOException {
		URL url = getResourcePath(classPath);
		InputStream in = url.openStream();
		InputStreamReader isr = new InputStreamReader(in, encode);
		BufferedReader reader = new BufferedReader(isr);
		ArrayList list = new ArrayList();

		String line = reader.readLine();
		while (line != null) {
			list.add(line);
			line = reader.readLine();
		}

		return list;
	}

	/**
	 * write string to specified file
	 *
	 * @param string file content
	 */
	public static synchronized void writeFile(String str, File file) throws IOException {
		BufferedWriter buw = new BufferedWriter(new FileWriter(file));

		buw.write(str);
		buw.flush();
		buw.close();
	}

	/**
	 * get the URL path of one class
	 *
	 * @param path the class relative path
	 * @param URL
	 */
	public static URL getResourcePath(String path) {
		return FileManager.class.getResource(path);
	}

	/**
	 * read object from file
	 *
	 */
	public static Object readObjectFromFile(File file) throws Exception {
		FileInputStream istream = new FileInputStream(file);
		ObjectInputStream ip = new ObjectInputStream(istream);

		return ip.readObject();
	}

	/**
	 * print object to file
	 *
	 */
	public static void printObjectToFile(File file, Object obj) throws Exception {
		FileOutputStream fs = new FileOutputStream(file);
		ObjectOutputStream op = new ObjectOutputStream(fs);

		op.writeObject(obj);

		op.flush();
		fs.close();
	}

	/**
	 * read inputstream and print to outputstream
	 */
	public static void pipeStream(InputStream is, OutputStream os) throws Exception {
		if (is == null) {
			return;
		}

		BufferedOutputStream bos = new BufferedOutputStream(os);

		int ch;
		while ((ch = is.read()) != -1) {
			bos.write(ch);
		}
		is.close();
		bos.flush();
		bos.close();
		os.close();
	}

	/**
	 * テスト用
	 */
	public static void main(String[] args) {
	}
}