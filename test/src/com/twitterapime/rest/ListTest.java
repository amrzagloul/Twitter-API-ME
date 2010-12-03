/**
 * 
 */
package com.twitterapime.rest;

import java.util.Hashtable;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;

/**
 * @author 82177082315
 *
 */
public class ListTest extends TestCase {
	/**
	 * 
	 */
	public ListTest() {
		super("ListTest");
	}

	/**
	 * Test method for {@link com.twitterapime.rest.List#List(java.lang.String, java.lang.String, boolean, java.lang.String)}.
	 */
	public void testListStringStringBooleanString() {
		List l = new List("1", "name", true, "description");
		//
		assertEquals("1", l.getString(MetadataSet.LIST_ID));
		assertEquals("name", l.getString(MetadataSet.LIST_NAME));
		assertEquals("public", l.getString(MetadataSet.LIST_MODE));
		assertEquals("description", l.getString(MetadataSet.LIST_DESCRIPTION));
		//
		l = new List(null, null, false, null);
		//
		assertNull(l.getString(MetadataSet.LIST_ID));
		assertNull(l.getString(MetadataSet.LIST_NAME));
		assertEquals("private", l.getString(MetadataSet.LIST_MODE));
		assertNull(l.getString(MetadataSet.LIST_DESCRIPTION));
	}

	/**
	 * Test method for {@link com.twitterapime.rest.List#getUserAccount()}.
	 */
	public void testGetUserAccount() {
		Hashtable t = new Hashtable();
		UserAccount ua = new UserAccount();
		//
		t.put(MetadataSet.LIST_USER_ACCOUNT, ua);
		//
		assertSame(ua, new List(t).getUserAccount());
	}
}
