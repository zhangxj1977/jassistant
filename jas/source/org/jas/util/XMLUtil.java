package org.jas.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMSource;

/**
 * jdom xml utils
 *
 */
public class XMLUtil {

	/**
	 * get dom from xml file input stream
	 *
	 */
	public static Document getDocument(InputStream in) {
		// Create an instance of the tester and test
		try {
			DOMBuilder builder = new DOMBuilder();
			Document doc = builder.build(in);

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * get dom from xml file
	 *
	 */
	public static Document getDocument(File file) {
		// Create an instance of the tester and test
		try {
			DOMBuilder builder = new DOMBuilder();
			Document doc = builder.build(file);

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * get String from document
	 *
	 */
	public static String getDocumentString(Document doc) {
		try {
			XMLOutputter outputter = new XMLOutputter();
			StringWriter out = new StringWriter();
			outputter.output(doc, out);

			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * get String from Element
	 *
	 */
	public static String getElementString(Element ele) {
		try {
			XMLOutputter outputter = new XMLOutputter();
			StringWriter out = new StringWriter();
			outputter.output(ele, out);

			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * transform xml and stylesheet to a text string
	 *
	 * @param in
	 * @param stylesheet
	 */
	public static String transform(Document in, InputStream stylesheet, String encoding)
								throws JDOMException {
		try {
			Transformer transformer = TransformerFactory.newInstance()
											.newTransformer(new StreamSource(stylesheet));

	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "text");
	        transformer.setOutputProperty(OutputKeys.ENCODING, encoding);

			StringWriter out = new StringWriter();
			transformer.transform(new JDOMSource(in), new StreamResult(out));

			return out.toString();
		} catch (TransformerException e) {
			throw new JDOMException("XSLT Transformation failed", e);
		}
	}

	/**
	 * transform xml and stylesheet to a text string
	 *
	 * @param in
	 * @param stylesheet
	 */
	public static String transform(Document in, File stylesheet, String encoding)
								throws JDOMException, IOException {
		return transform(in, new FileInputStream(stylesheet), encoding);
	}


	/**
	 * test
	 *
	 */
	public static void main(String[] args) throws Exception {
		Document doc = getDocument(new File(args[0]));
		String result = transform(doc, new File(args[1]), "Shift_JIS");

		System.out.println(result);
	}
}