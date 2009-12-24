/**
 * 
 */
package com.twitterapime.search;

import java.util.Hashtable;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.rest.UserAccount;

/**
 * @author Main
 *
 */
public class TweetTest extends TestCase {
	/**
	 * 
	 */
	public TweetTest() {
		super("TweetTest");
	}

	/**
	 * Test method for {@link com.twitterapime.search.Tweet#Tweet(java.lang.String)}.
	 */
	public void testTweetString() {
		try {
			new Tweet((String)null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Tweet("");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Tweet("aaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBB");
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Tweet("aaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBaaaaaaaaaaBBBBBBBBBBX");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Tweet t = new Tweet("Hello, Twitter API ME!!!");
		assertEquals("Hello, Twitter API ME!!!", t.getString(MetadataSet.TWEET_CONTENT));
	}

	/**
	 * Test method for {@link com.twitterapime.search.Tweet#getUserAccount()}.
	 */
	public void testGetUserAccount() {
		UserAccount ua = new UserAccount();
		Hashtable sample = new Hashtable();
		sample.put(MetadataSet.TWEET_USER_ACCOUNT, ua);
		Tweet t = new Tweet(sample);
		assertSame(ua, t.getUserAccount());
	}
}