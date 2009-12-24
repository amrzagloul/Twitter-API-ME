/**
 * 
 */
package com.twitterapime.io;

import impl.javame.com.twitterapime.io.HttpConnectionImpl;

import java.io.IOException;

import com.sonyericsson.junit.framework.TestCase;

/**
 * @author Main
 *
 */
public class HttpConnectorTest extends TestCase {
	/**
	 * 
	 */
	public HttpConnectorTest() {
		super("HttpConnectorTest");
	}

	/**
	 * Test method for {@link com.twitterapime.io.HttpConnector#open(java.lang.String)}.
	 */
	public void testOpen() {
		try {
			HttpConnector.open(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			HttpConnector.open("");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			HttpConnection conn = HttpConnector.open("http://www.twitterapime.com");
			//
			if (!(conn instanceof HttpConnectionImpl)) {
				fail();
			}
		} catch (IOException e) {
		}
	}

	/**
	 * Test method for {@link com.twitterapime.io.HttpConnector#encodeURL(java.lang.String)}.
	 */
	public void testEncodeURL() {
		try {
			HttpConnector.encodeURL(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		assertEquals("", HttpConnector.encodeURL(""));
		assertEquals("http://www.twitterapime.com", HttpConnector.encodeURL("http://www.twitterapime.com"));
		assertEquals("http://www.twitterapime.com?q=%21%2A%22%27%28%29%3B%40%2B%24%2C%25%23%5B%5D%20", HttpConnector.encodeURL("http://www.twitterapime.com?q=!*\"\'();@+$,%#[] "));
		assertEquals("http://www.twitterapime.com?q=twitter%20api%20me", HttpConnector.encodeURL("http://www.twitterapime.com?q=twitter api me"));
		assertEquals("http://www.twitterapime.com?q=twitter&param1=twitter%20api&param2=me", HttpConnector.encodeURL("http://www.twitterapime.com?q=twitter&param1=twitter api&param2=me"));
	}

	/**
	 * Test method for {@link com.twitterapime.io.HttpConnector#encodeBase64(java.lang.String)}.
	 */
	public void testEncodeBase64() {
		try {
			HttpConnector.encodeBase64(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		assertEquals("", HttpConnector.encodeBase64(""));
		assertEquals("dHdpdHRlcmFwaW1lOnR3aXR0ZXJhcGltZQ==", HttpConnector.encodeBase64("twitterapime:twitterapime"));
		assertEquals("amF2YW1pY3JvZWRpdGlvbjphbmRyb2lk", HttpConnector.encodeBase64("javamicroedition:android"));
	}
}