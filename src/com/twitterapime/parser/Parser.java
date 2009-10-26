/*
 * Parser.java
 * 24/10/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * This interface defines the necessary methods of a parser in order to retrieve
 * the content of a document.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see ParserFactory
 * @see FeedParser
 * @see ErrorMessageParser
 */
public interface Parser {
	/**
	 * <p>
	 * Parse the content from the stream in order to retrieve the information
	 * of a given document.
	 * </p>
	 * @param stream The document stream object.
	 * @exception ParserException If there is an error in the document format.
	 * @exception IOException If an I/O error occurs.
	 */
	public abstract void parse(InputStream stream) throws IOException,
		ParserException;
}