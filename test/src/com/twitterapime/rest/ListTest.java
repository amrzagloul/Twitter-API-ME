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
		List l = new List("name", true, "description");
		//
		assertEquals("name", l.getString(MetadataSet.LIST_NAME));
		assertEquals("public", l.getString(MetadataSet.LIST_MODE));
		assertEquals("description", l.getString(MetadataSet.LIST_DESCRIPTION));
		//
		l = new List("name", false, null);
		//
		assertEquals("private", l.getString(MetadataSet.LIST_MODE));
		assertNull(l.getString(MetadataSet.LIST_DESCRIPTION));
		//
		try {
			l = new List(null, true, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			l = new List("", true, "description");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
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
