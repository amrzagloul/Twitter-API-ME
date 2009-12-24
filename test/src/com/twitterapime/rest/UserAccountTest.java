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
}