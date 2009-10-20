/*
 * XMLErrorMessageParserImpl.java
 * 03/10/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package impl.javame.com.twitterapime.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.twitterapime.parser.ErrorMessageParser;
import com.twitterapime.parser.ParserException;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class XMLErrorMessageParserImpl extends ErrorMessageParser {
	/**
	 * @see com.twitterapime.parser.FeedParser#parse(java.io.InputStream)
	 */
	public void parse(InputStream xmlStream)
	  throws IOException,ParserException{
		KXmlParser parser = new KXmlParser();
		//
		try {
			parser.setInput(new InputStreamReader(xmlStream));
			//
			int etype;
			String tag;
			String xmlPath = "";
			error = null;
			request = null;
			//
			while (true) {
				etype = parser.next();
				//
				if (etype == XmlPullParser.START_TAG) {
					tag = parser.getName().toLowerCase();
					//
					xmlPath += '/' + tag;
				} else if (etype == XmlPullParser.END_TAG) {
					tag = parser.getName().toLowerCase();
					//
					xmlPath = xmlPath.substring(0, xmlPath.lastIndexOf('/'));
				} else if (etype == XmlPullParser.TEXT) {
					final String text = parser.getText().trim();
					//
					if (xmlPath.equals("/hash/request")) {
						request = text;
					} else if (xmlPath.equals("/hash/error")) {
						error = text;
					}
				} else if (etype == XmlPullParser.END_DOCUMENT) {
					break;
				}
			}
		} catch (XmlPullParserException e) {
			throw new ParserException(e.getMessage());
		}
	}
	
//	public static void main(String[] args) {
//		XMLErrorMessageParserImpl p = new XMLErrorMessageParserImpl();
//		try {
//			p.parse(p.getClass().getResourceAsStream("/atom-teste-error.xml"));
//			//
//			System.out.println(p.getRequest());
//			System.out.println(p.getError());
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (FeedParserException e) {
//			e.printStackTrace();
//		}
//	}
}