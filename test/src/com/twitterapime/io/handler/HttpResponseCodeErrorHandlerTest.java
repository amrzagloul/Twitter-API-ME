/**
 * 
 */
package com.twitterapime.io.handler;

import java.io.IOException;
import java.io.InputStream;

import com.twitterapime.parser.ParserFactory;
import com.twitterapime.test.TwitterAPIMETestCase;

/**
 * @author Main
 *
 */
public class HttpResponseCodeErrorHandlerTest extends TwitterAPIMETestCase {
	/**
	 * 
	 */
	private HttpResponseCodeErrorHandler handler;

	/**
	 * 
	 */
	public HttpResponseCodeErrorHandlerTest() {
		super("HttpResponseCodeErrorHandlerTest");
	}
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() {
		InputStream errorXML = null;
		handler = new HttpResponseCodeErrorHandler();
		//
		try {
			errorXML = getClass().getResourceAsStream("/xml/twitterapi-error-message.xml");
			ParserFactory.getDefaultParser().parse(errorXML, handler);
		} catch (Exception e) {
			fail();
		} finally {
			if (errorXML != null) {
				try {
					errorXML.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Test method for {@link com.twitterapime.io.handler.HttpResponseCodeErrorHandler#getParsedErrorMessage()}.
	 */
	public void testGetParsedErrorMessage() {
		assertEquals("Error message.", handler.getParsedErrorMessage());
	}

	/**
	 * Test method for {@link com.twitterapime.io.handler.HttpResponseCodeErrorHandler#getParsedRequestMessage()}.
	 */
	public void testGetParsedRequestMessage() {
		assertEquals("Request message.", handler.getParsedRequestMessage());
	}
}