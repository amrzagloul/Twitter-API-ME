/**
 * 
 */
package com.twitterapime.io.handler;

import java.io.InputStream;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.parser.ParserFactory;

/**
 * @author Main
 *
 */
public class HttpResponseCodeErrorHandlerTest extends TestCase {
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
	 * @see com.sonyericsson.junit.framework.TestCase#setUp()
	 */
	public void setUp() throws Throwable {
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
				errorXML.close();
			}
		}
	}

	/**
	 * Test method for {@link com.twitterapime.io.handler.HttpResponseCodeErrorHandler#text(java.lang.String)}.
	 */
	public void testText() {
		HttpResponseCodeErrorHandler h = new HttpResponseCodeErrorHandler();
		//
		try {
			h.startElement(null, "hash", null, null);
			h.startElement(null, "error", null, null);
			h.text("Error message.");
			assertEquals("Error message.", h.getParsedErrorMessage());
			h.endElement(null, null, null);
			h.endElement(null, null, null);
			//
			h.startElement(null, "hash", null, null);
			h.startElement(null, "request", null, null);
			h.text("Request message.");
			assertEquals("Request message.", h.getParsedRequestMessage());
		} catch (Exception e) {
			fail();
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