/**
 * 
 */
package com.twitterapime.rest;

import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.Tweet;
import com.twitterapime.test.TwitterAPIMETestCase;

/**
 * @author Main
 *
 */
public class UserAccountTest extends TwitterAPIMETestCase {
	/**
	 * 
	 */
	public UserAccountTest() {
		super("UserAccountTest");
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccount#getLastTweet()}.
	 */
	public void testGetLastTweet() {
		Hashtable sample = new Hashtable();
		Tweet t = new Tweet("Hello!!!");
		sample.put(MetadataSet.USERACCOUNT_LAST_TWEET, t);
		UserAccount ua = new UserAccount(sample);
		assertSame(t, ua.getLastTweet());
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.UserAccount#UserAccount(String)}.
	 */
	public void testUserAccountString() {
		try {
			new UserAccount((String)null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new UserAccount("");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Hashtable data = new Hashtable();
		data.put(MetadataSet.USERACCOUNT_ID, "123456789");
		data.put(MetadataSet.USERACCOUNT_USER_NAME, "123456789");
		//
		assertTrue(new UserAccount("123456789").equals(new UserAccount(data)));
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.UserAccount#getUserNameOrID()}.
	 */
	public void testGetUserNameOrID() {
		UserAccount ua = new UserAccount("username");
		assertEquals("username", ua.getUserNameOrID());
		//
		Hashtable data = new Hashtable();
		data.put(MetadataSet.USERACCOUNT_USER_NAME, "twiterapimetest");
		ua = new UserAccount(data);
		//
		assertEquals("twiterapimetest", ua.getUserNameOrID());
		//
		data = new Hashtable();
		data.put(MetadataSet.USERACCOUNT_ID, "twapime");
		ua = new UserAccount(data);
		//
		assertEquals("twapime", ua.getUserNameOrID());
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.UserAccount#validateUserNameOrID()}.
	 */
	public void testValidateUserNameOrID() {
		try {
			new UserAccount().validateUserNameOrID();
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new UserAccount("twapime").validateUserNameOrID();
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccount#getUserNameOrIDParamValue()}.
	 */
	public void testGetUserNameOrIDParamValue() {
		Hashtable data = new Hashtable();
		data.put(MetadataSet.USERACCOUNT_USER_NAME, "twiterapimetest");
		UserAccount ua = new UserAccount(data);
		String[] pv = ua.getUserNameOrIDParamValue();
		//
		assertNotNull(pv);
		assertEquals(2, pv.length);
		assertEquals("screen_name", pv[0]);
		assertEquals("twiterapimetest", pv[1]);
		//
		data = new Hashtable();
		data.put(MetadataSet.USERACCOUNT_ID, "9876543210");
		ua = new UserAccount(data);
		pv = ua.getUserNameOrIDParamValue();
		//
		assertNotNull(pv);
		assertEquals(2, pv.length);
		assertEquals("user_id", pv[0]);
		assertEquals("9876543210", pv[1]);
		//
		ua = new UserAccount();
		pv = ua.getUserNameOrIDParamValue();
		//
		assertNotNull(pv);
		assertEquals(2, pv.length);
		assertNull(pv[0]);
		assertNull(pv[1]);
	}
}