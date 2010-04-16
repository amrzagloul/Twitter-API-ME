/**
 * 
 */
package com.twitterapime.rest.handler;

import java.io.InputStream;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.parser.ParserFactory;

/**
 * @author Main
 *
 */
public class TweetHandlerTest extends TestCase {
	/**
	 * 
	 */
	private TweetHandler handler;

	/**
	 * 
	 */
	public TweetHandlerTest() {
		super("TweetHandlerTest");
	}
	
	/**
	 * @see com.sonyericsson.junit.framework.TestCase#setUp()
	 */
	public void setUp() throws Throwable {
		InputStream errorXML = null;
		handler = new TweetHandler();
		//
		try {
			errorXML = getClass().getResourceAsStream("/xml/twitterapi-tweet-response.xml");
			ParserFactory.getDefaultParser().parse(errorXML, handler);
		} catch (Exception e) {
			fail();
		} finally {
			if (errorXML != null) {
				errorXML.close();
			}
		}
	}
}