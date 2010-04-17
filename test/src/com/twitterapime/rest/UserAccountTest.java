/**
 * 
 */
package com.twitterapime.rest;

import java.util.Hashtable;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.Tweet;

/**
 * @author Main
 *
 */
public class UserAccountTest extends TestCase {
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
		data.put(MetadataSet.USERACCOUNT_ID, "twiterapimetest");
		data.put(MetadataSet.USERACCOUNT_USER_NAME, "twiterapimetest");
		//
		assertTrue(new UserAccount("twiterapimetest").equals(new UserAccount(data)));
	}
}