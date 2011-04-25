/*
 * Cursor.java
 * 21/04/2011
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.model;

import java.util.Enumeration;

/**
 * <p>
 * This class implements a cursor to navigate through a paginated content.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.7
 */
public class Cursor implements Enumeration {
	/**
	 * <p>
	 * Elements.
	 * </p> 
	 */
	private Object[] elements;
	
	/**
	 * <p>
	 * Previous page's index.
	 * </p> 
	 */
	private long prevPageIndex;

	/**
	 * <p>
	 * Next page's index.
	 * </p> 
	 */
	private long nextPageIndex;
	
	/**
	 * <p>
	 * Reading index.
	 * </p> 
	 */
	private int elementIndex;

	/**
	 * <p>
	 * Create an instance of Cursor class.
	 * </p>
	 * @param elements Elements.
	 * @param prevPageIndex Previous page's index.
	 * @param nextPageIndex Next page's index.
	 */
	public Cursor(Object[] elements, long prevPageIndex, long nextPageIndex) {
		this.elements = elements;
		this.prevPageIndex = prevPageIndex;
		this.nextPageIndex = nextPageIndex;
	}

	/**
	 * @see java.util.Enumeration#hasMoreElements()
	 */
	public boolean hasMoreElements() {
		return elementIndex < elements.length;
	}

	/**
	 * @see java.util.Enumeration#nextElement()
	 */
	public Object nextElement() {
		return elements[elementIndex++];
	}
	
	/**
	 * <p>
	 * Next page's index.
	 * </p>
	 * @return Index.
	 */
	public long getNextPageIndex() {
		return nextPageIndex;
	}

	/**
	 * <p>
	 * Previous page's index.
	 * </p>
	 * @return Index.
	 */
	public long getPreviousPageIndex() {
		return prevPageIndex;
	}
	
	/**
	 * <p>
	 * Verify if there is more pages that can be read.
	 * </p>
	 * @return There is (true).
	 */
	public boolean hasMorePages() {
		return nextPageIndex > 0;
	}
}
