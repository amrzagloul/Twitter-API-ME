/*
 * HttpResponseCodeHandler.java
 * 03/10/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.io;

import java.io.IOException;

import com.twitterapime.parser.ErrorMessageParser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.InvalidQueryException;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class HttpResponseCodeHandler {
	/**
	 * @param conn
	 * @throws IOException
	 * @throws LimitExceededException
	 */
	public static void handleSearchAPICodes(HttpConnection conn)
			throws IOException, LimitExceededException {
		final int respCode = conn.getResponseCode();
		//
		if (respCode != HttpConnection.HTTP_OK
				&& respCode != HttpConnection.HTTP_NOT_MODIFIED) {
			String errorMsg = null;
			ErrorMessageParser p = ParserFactory.getDefaultErrorMessageParser();
			//
			try {
				p.parse(conn.openInputStream());
				//
				errorMsg = p.getError();
			} catch (ParserException e) {
				errorMsg = "HTTP ERROR CODE: " + respCode;
			}
			//
			if (isLimitExceededError(respCode)) {
				throw new LimitExceededException(errorMsg);
			} else if (isInvalidQueryError(respCode)) {
				throw new InvalidQueryException(errorMsg);
			} else {
				throw new IOException(errorMsg);
			}
		}
	}
	
	/**
	 * @param code
	 */
	public static boolean isLimitExceededError(int code) {
		return code == HttpConnection.HTTP_BAD_REQUEST
				|| code == HttpConnection.HTTP_UNAVAILABLE
				|| code == HttpConnection.HTTP_FORBIDDEN;
	}
	
	/**
	 * @param code
	 */
	public static boolean isInvalidQueryError(int code) {
		return code == HttpConnection.HTTP_NOT_FOUND
				|| code == HttpConnection.HTTP_NOT_ACCEPTABLE;
	}
	
	/**
	 * @param code
	 */
	public static boolean isServiceError(int code) {
		return code == HttpConnection.HTTP_BAD_GATEWAY
				|| code == HttpConnection.HTTP_INTERNAL_ERROR;
	}
	
	/**
	 * 
	 */
	HttpResponseCodeHandler() {
	}
}