/**
 * 
 */
package com.twitterapime.io;

import impl.android.com.twitterapime.io.HttpConnectionImpl;

import java.io.IOException;

import com.twitterapime.test.TwitterAPIMETestCase;

/**
 * @author Main
 *
 */
public class HttpConnectorTest extends TwitterAPIMETestCase {
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
}