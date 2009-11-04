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
 * This class is responsible for handling the Http response-codes from Twitter
 * API, in order to check the status of a request: success or failure.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see LimitExceededException
 * @see InvalidQueryException
 */
public final class HttpResponseCodeHandler {
	/**
	 * <p>
	 * Handle a given HttpConnection object's response-code in order to analyze
	 * whether the requests to Twitter Search API went well. Otherwise, an
	 * exception is thrown describing the problem.
	 * </p>
	 * @param conn HttpConnection object to be analyzed.
	 * @throws IOException If an I/O or service error occurs.
	 * @throws LimitExceededException If a request limit exceeded error occurs.
	 * @throws InvalidQueryException If an invalid query error occurs.
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
			if (isInvalidQueryError(respCode)) {
				throw new InvalidQueryException(errorMsg);
			} else if (isLimitExceededError(respCode)) {
				throw new LimitExceededException(errorMsg);
			} else {
				throw new IOException(errorMsg);
			}
		}
	}
	
	/**
	 * <p>
	 * Check if the response-code reports a request limit exceeded error.
	 * </p>
	 * @param code The response-code.
	 * @return true if the response-code represents a limit exceeded error.
	 */
	static boolean isLimitExceededError(int code) {
		return code == HttpConnection.HTTP_BAD_REQUEST
				|| code == HttpConnection.HTTP_UNAVAILABLE
				|| code == HttpConnection.HTTP_FORBIDDEN;
	}
	
	/**
	 * <p>
	 * Check if the response-code reports an invalid query error.
	 * </p>
	 * @param code The response-code.
	 * @return true if the response-code represents an invalid query error.
	 */
	static boolean isInvalidQueryError(int code) {
		return code == HttpConnection.HTTP_NOT_FOUND
				|| code == HttpConnection.HTTP_NOT_ACCEPTABLE
				|| code == HttpConnection.HTTP_FORBIDDEN;
	}
	
	/**
	 * <p>
	 * Check if the response-code reports a service error.
	 * </p>
	 * @param code The response-code.
	 * @return true if the response-code represents a service error.
	 */
	static boolean isServiceError(int code) {
		return code == HttpConnection.HTTP_BAD_GATEWAY
				|| code == HttpConnection.HTTP_INTERNAL_ERROR;
	}
	
	/**
	 * <p>
	 * Package-protected constructor to avoid object instantiation.
	 * </p>
	 */
	HttpResponseCodeHandler() {
	}
}