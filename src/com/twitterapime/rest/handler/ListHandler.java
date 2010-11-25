/*
 * ListHandler.java
 * 23/11/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;
import java.util.Vector;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Attributes;
import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.parser.ParserException;
import com.twitterapime.rest.List;
import com.twitterapime.rest.UserAccount;

/**
 * <p>
 * Handler class for parsing the XML Lists from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.6
 */
public final class ListHandler extends DefaultXMLHandler {
	/**
	 * <p>
	 * User Account XML handler object.
	 * </p>
	 */
	private UserAccountHandler uaHandler = new UserAccountHandler();
	
	/**
	 * <p>
	 * List of lists.
	 * </p>
	 */
	private Vector lists = new Vector(10);
	
	/**
	 * <p>
	 * Hash with user account values.
	 * </p>
	 */
	private Hashtable uaValues;

	/**
	 * <p>
	 * Hash with list values.
	 * </p>
	 */
	private Hashtable listValues;
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String, com.twitterapime.parser.Attributes)
	 */
	public void startElement(String namespaceURI, String localName,
		String qName, Attributes attrs) throws ParserException {
		super.startElement(namespaceURI, localName, qName, attrs);
		//
		if (localName.toLowerCase().equals("list")) {
			listValues = new Hashtable(10);
			uaValues = new Hashtable(25);
			//
			listValues.put(
				MetadataSet.LIST_USER_ACCOUNT, new UserAccount(uaValues));
			//
			lists.addElement(new List(listValues));
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#text(java.lang.String)
	 */
	public void text(String text) throws ParserException {
		text = text.trim();
		//
		if (xmlPath.indexOf("/list/user/") != -1) {
			uaHandler.populate(uaValues, xmlPath, text);
		} else if (xmlPath.indexOf("/list/id") != -1) {
			listValues.put(MetadataSet.LIST_ID, text);
		} else if (xmlPath.indexOf("/list/name") != -1) {
			listValues.put(MetadataSet.LIST_NAME, text);
		} else if (xmlPath.indexOf("/list/full_name") != -1) {
			listValues.put(MetadataSet.LIST_FULL_NAME, text);
		} else if (xmlPath.indexOf("/list/slug") != -1) {
			listValues.put(MetadataSet.LIST_SLUG, text);
		} else if (xmlPath.indexOf("/list/description") != -1) {
			listValues.put(MetadataSet.LIST_DESCRIPTION, text);
		} else if (xmlPath.indexOf("/list/subscriber_count") != -1) {
			listValues.put(MetadataSet.LIST_SUBSCRIBER_COUNT, text);
		} else if (xmlPath.indexOf("/list/member_count") != -1) {
			listValues.put(MetadataSet.LIST_MEMBER_COUNT, text);
		} else if (xmlPath.indexOf("/list/uri") != -1) {
			listValues.put(MetadataSet.LIST_URI, text);
		} else if (xmlPath.indexOf("/list/following") != -1) {
			listValues.put(MetadataSet.LIST_FOLLOWING, text);
		} else if (xmlPath.indexOf("/list/mode") != -1) {
			listValues.put(MetadataSet.LIST_MODE, text);
		}
	}
	
	/**
	 * <p>
	 * Return the parsed lists.
	 * </p>
	 * @return Array of lists.
	 */
	public List[] getParsedLists() {
		List[] ts = new List[lists.size()];
		lists.copyInto(ts);
		//
		return ts;
	}
}