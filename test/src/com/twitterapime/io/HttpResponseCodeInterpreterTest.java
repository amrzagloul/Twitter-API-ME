/**
 * 
 */
package com.twitterapime.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.search.InvalidQueryException;
import com.twitterapime.search.LimitExceededException;

/**
 * @author Main
 *
 */
public class HttpResponseCodeInterpreterTest extends TestCase {
	/**
	 * 
	 */
	public HttpResponseCodeInterpreterTest() {
		super("HttpResponseCodeInterpreterTest");
	}
	
	/**
	 * Test method for {@link com.twitterapime.io.HttpResponseCodeInterpreter#perform(com.twitterapime.io.HttpConnection)}.
	 */
	public void testPerform() {
		try {
			HttpResponseCodeInterpreter.perform(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		HttpConnMock conn = new HttpConnMock();
		//
		try {
			conn.setResponseCode(HttpConnection.HTTP_OK);
			HttpResponseCodeInterpreter.perform(new HttpResponse(conn));
		} catch (Exception e) {
			fail();
		}
		try {
			conn.setResponseCode(HttpConnection.HTTP_NOT_MODIFIED);
			HttpResponseCodeInterpreter.perform(new HttpResponse(conn));
		} catch (Exception e) {
			fail();
		}
		//
		int[] codes = new int[] {HttpConnection.HTTP_NOT_FOUND, HttpConnection.HTTP_NOT_ACCEPTABLE};
		for (int i = 0; i < codes.length; i++) {
			try {
				conn.setResponseCode(codes[i]);
				HttpResponseCodeInterpreter.perform(new HttpResponse(conn));
			} catch (InvalidQueryException e) {
			} catch (Exception e) {
				fail();
			}
		}
		//
		codes = new int[] {HttpConnection.HTTP_BAD_REQUEST, HttpConnection.HTTP_FORBIDDEN, HttpResponseCodeInterpreter.CUSTOM_HTTP_CODE_ENHANCE_YOUR_CALM};
		for (int i = 0; i < codes.length; i++) {
			try {
				conn.setResponseCode(codes[i]);
				HttpResponseCodeInterpreter.perform(new HttpResponse(conn));
			} catch (LimitExceededException e) {
			} catch (Exception e) {
				fail();
			}
		}
		//
		try {
			conn.setResponseCode(HttpConnection.HTTP_UNAUTHORIZED);
			HttpResponseCodeInterpreter.perform(new HttpResponse(conn));
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		codes = new int[] {HttpConnection.HTTP_BAD_GATEWAY, HttpConnection.HTTP_INTERNAL_ERROR, HttpConnection.HTTP_UNAVAILABLE};
		for (int i = 0; i < codes.length; i++) {
			try {
				conn.setResponseCode(codes[i]);
				HttpResponseCodeInterpreter.perform(new HttpResponse(conn));
			} catch (IOException e) {
			} catch (Exception e) {
				fail();
			}
		}
	}

	/**
	 * Test method for {@link com.twitterapime.io.HttpResponseCodeInterpreter#isLimitExceededError(int)}.
	 */
	public void testIsLimitExceededError() {
		assertFalse(HttpResponseCodeInterpreter.isLimitExceededError(HttpConnection.HTTP_UNAUTHORIZED));
		assertFalse(HttpResponseCodeInterpreter.isLimitExceededError(HttpConnection.HTTP_BAD_GATEWAY));
		assertTrue(HttpResponseCodeInterpreter.isLimitExceededError(HttpConnection.HTTP_BAD_REQUEST));
		assertTrue(HttpResponseCodeInterpreter.isLimitExceededError(HttpConnection.HTTP_FORBIDDEN));
		assertFalse(HttpResponseCodeInterpreter.isLimitExceededError(HttpConnection.HTTP_INTERNAL_ERROR));
		assertFalse(HttpResponseCodeInterpreter.isLimitExceededError(HttpConnection.HTTP_NOT_ACCEPTABLE));
		assertFalse(HttpResponseCodeInterpreter.isLimitExceededError(HttpConnection.HTTP_NOT_FOUND));
		assertFalse(HttpResponseCodeInterpreter.isLimitExceededError(HttpConnection.HTTP_NOT_MODIFIED));
		assertFalse(HttpResponseCodeInterpreter.isLimitExceededError(HttpConnection.HTTP_OK));
		assertFalse(HttpResponseCodeInterpreter.isLimitExceededError(HttpConnection.HTTP_UNAVAILABLE));
		assertTrue(HttpResponseCodeInterpreter.isLimitExceededError(HttpResponseCodeInterpreter.CUSTOM_HTTP_CODE_ENHANCE_YOUR_CALM));
	}

	/**
	 * Test method for {@link com.twitterapime.io.HttpResponseCodeInterpreter#isInvalidQueryError(int)}.
	 */
	public void testIsInvalidQueryError() {
		assertFalse(HttpResponseCodeInterpreter.isInvalidQueryError(HttpConnection.HTTP_UNAUTHORIZED));
		assertFalse(HttpResponseCodeInterpreter.isInvalidQueryError(HttpConnection.HTTP_BAD_GATEWAY));
		assertFalse(HttpResponseCodeInterpreter.isInvalidQueryError(HttpConnection.HTTP_BAD_REQUEST));
		assertFalse(HttpResponseCodeInterpreter.isInvalidQueryError(HttpConnection.HTTP_FORBIDDEN));
		assertFalse(HttpResponseCodeInterpreter.isInvalidQueryError(HttpConnection.HTTP_INTERNAL_ERROR));
		assertTrue(HttpResponseCodeInterpreter.isInvalidQueryError(HttpConnection.HTTP_NOT_ACCEPTABLE));
		assertTrue(HttpResponseCodeInterpreter.isInvalidQueryError(HttpConnection.HTTP_NOT_FOUND));
		assertFalse(HttpResponseCodeInterpreter.isInvalidQueryError(HttpConnection.HTTP_NOT_MODIFIED));
		assertFalse(HttpResponseCodeInterpreter.isInvalidQueryError(HttpConnection.HTTP_OK));
		assertFalse(HttpResponseCodeInterpreter.isInvalidQueryError(HttpConnection.HTTP_UNAVAILABLE));
		assertFalse(HttpResponseCodeInterpreter.isInvalidQueryError(HttpResponseCodeInterpreter.CUSTOM_HTTP_CODE_ENHANCE_YOUR_CALM));
	}

	/**
	 * Test method for {@link com.twitterapime.io.HttpResponseCodeInterpreter#isServiceError(int)}.
	 */
	public void testIsServiceError() {
		assertFalse(HttpResponseCodeInterpreter.isServiceError(HttpConnection.HTTP_UNAUTHORIZED));
		assertTrue(HttpResponseCodeInterpreter.isServiceError(HttpConnection.HTTP_BAD_GATEWAY));
		assertFalse(HttpResponseCodeInterpreter.isServiceError(HttpConnection.HTTP_BAD_REQUEST));
		assertFalse(HttpResponseCodeInterpreter.isServiceError(HttpConnection.HTTP_FORBIDDEN));
		assertTrue(HttpResponseCodeInterpreter.isServiceError(HttpConnection.HTTP_INTERNAL_ERROR));
		assertFalse(HttpResponseCodeInterpreter.isServiceError(HttpConnection.HTTP_NOT_ACCEPTABLE));
		assertFalse(HttpResponseCodeInterpreter.isServiceError(HttpConnection.HTTP_NOT_FOUND));
		assertFalse(HttpResponseCodeInterpreter.isServiceError(HttpConnection.HTTP_NOT_MODIFIED));
		assertFalse(HttpResponseCodeInterpreter.isServiceError(HttpConnection.HTTP_OK));
		assertTrue(HttpResponseCodeInterpreter.isServiceError(HttpConnection.HTTP_UNAVAILABLE));
		assertFalse(HttpResponseCodeInterpreter.isServiceError(HttpResponseCodeInterpreter.CUSTOM_HTTP_CODE_ENHANCE_YOUR_CALM));
	}

	/**
	 * Test method for {@link com.twitterapime.io.HttpResponseCodeInterpreter#isSecurityError(int)}.
	 */
	public void testIsSecurityError() {
		assertTrue(HttpResponseCodeInterpreter.isSecurityError(HttpConnection.HTTP_UNAUTHORIZED));
		assertFalse(HttpResponseCodeInterpreter.isSecurityError(HttpConnection.HTTP_BAD_GATEWAY));
		assertFalse(HttpResponseCodeInterpreter.isSecurityError(HttpConnection.HTTP_BAD_REQUEST));
		assertFalse(HttpResponseCodeInterpreter.isSecurityError(HttpConnection.HTTP_FORBIDDEN));
		assertFalse(HttpResponseCodeInterpreter.isSecurityError(HttpConnection.HTTP_INTERNAL_ERROR));
		assertFalse(HttpResponseCodeInterpreter.isSecurityError(HttpConnection.HTTP_NOT_ACCEPTABLE));
		assertFalse(HttpResponseCodeInterpreter.isSecurityError(HttpConnection.HTTP_NOT_FOUND));
		assertFalse(HttpResponseCodeInterpreter.isSecurityError(HttpConnection.HTTP_NOT_MODIFIED));
		assertFalse(HttpResponseCodeInterpreter.isSecurityError(HttpConnection.HTTP_OK));
		assertFalse(HttpResponseCodeInterpreter.isSecurityError(HttpConnection.HTTP_UNAVAILABLE));
		assertFalse(HttpResponseCodeInterpreter.isSecurityError(HttpResponseCodeInterpreter.CUSTOM_HTTP_CODE_ENHANCE_YOUR_CALM));
	}
	
	private class HttpConnMock implements HttpConnection {
		private int responseCode;
		
		public void setResponseCode(int code) {
			responseCode = code;
		}
		public void close() throws IOException {
		}

		public String getHeaderField(String name) throws IOException {
			return null;
		}

		public int getResponseCode() throws IOException {
			return responseCode;
		}

		public void open(String url) throws IOException {
		}

		public InputStream openInputStream() throws IOException {
			return getClass().getResourceAsStream("/xml/twitterapi-error-message.xml");
		}

		public OutputStream openOutputStream() throws IOException {
			return null;
		}

		public void setRequestMethod(String method) throws IOException {
		}

		public void setRequestProperty(String key, String value)
			throws IOException {
		}

		public String getRequestProperty(String key) throws IOException {
			return null;
		}
	}
}