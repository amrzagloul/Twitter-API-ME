/**
 * 
 */
package com.twitterapime.rest;

import java.io.IOException;
import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.InvalidQueryException;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Tweet;
import com.twitterapime.test.TwitterAPIMETestCase;

/**
 * @author Main
 *
 */
public class TweetERTest extends TwitterAPIMETestCase {
	/**
	 * 
	 */
	public TweetERTest() {
		super("TweetERTest");
	}
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() {
		try {
			UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1, true);
			UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_2, true);
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	public void tearDown() {
		try {
			UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1, false);
			UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_2, false);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.TweetER#getInstance(com.twitterapime.rest.UserAccountManager)}.
	 */
	public void testGetInstanceUserAccountManager() {
		try {
			TweetER.getInstance(null);
			fail("test: 1");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			TweetER.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_3_NON_EXISTENT));
			fail("test: 3");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		try {
			TweetER t = TweetER.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
			assertNotNull("test: 5", t);
			assertSame("test: 6", t, TweetER.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1)));			
		} catch (Exception e) {
			fail("test: 7");
		}
		//
		assertNotSame("test: 8", TweetER.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1)), TweetER.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_2)));
	}

	/**
	 * Test method for {@link com.twitterapime.rest.TweetER#getInstance()}.
	 */
	public void testGetInstance() {
		TweetER t = TweetER.getInstance();
		assertNotNull("test: 1", t);
		assertSame("test: 2", t, TweetER.getInstance());
	}

	/**
	 * Test method for {@link com.twitterapime.rest.TweetER#findByID(java.lang.String)}.
	 */
	public void testFindByID() {
		TweetER t = TweetER.getInstance();
		//
		try {
			t.findByID(null);
			fail("test: 1");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			t.findByID("");
			fail("test: 3");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		try {
			t.findByID("7141196879");
			fail("test: 5");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 6");
		}
		//
		try {
			assertNull("test: 7", t.findByID("9890asdh9809"));
		} catch (Exception e) {
			fail("test: 8");
		}
		//
		try {
			Tweet tw = t.findByID("7041609437");
			assertNotNull("test: 9", tw);
			assertEquals("test: 10", "7041609437", tw.getString(MetadataSet.TWEET_ID));
		} catch (Exception e) {
			fail("test: 11");
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.TweetER#post(com.twitterapime.search.Tweet)}.
	 */
	public void testPost() {
		TweetER t = TweetER.getInstance();
		//
		try {
			t.post(null);
			fail("test: 1");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			t.post(new Tweet());
			fail("test: 3");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		try {
			t.post(new Tweet("Hello, Twitters!!!"));
			fail("test: 5");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 6");
		}
		//
		try {
			t = TweetER.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
			//
			String msg = "Test at " + System.currentTimeMillis() + " milis.";
			Tweet tw1 = new Tweet(msg);
			Tweet tw2 = t.post(tw1);
			//
			assertSame("test: 7", tw1, tw2);
			assertEquals("test: 8", msg, tw2.getString(MetadataSet.TWEET_CONTENT));
		} catch (IOException e) {
			fail("test: 9");
		} catch (LimitExceededException e) {
			fail("test: 10");
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.TweetER#repost(com.twitterapime.search.Tweet)}.
	 */
	public void testRepost() {
		TweetER t1 = TweetER.getInstance();
		//
		try {
			t1.repost(null);
			fail("test: 1");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			t1.repost(new Tweet());
			fail("test: 3");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		Hashtable ht = new Hashtable();
		ht.put(MetadataSet.TWEET_ID, "4646461316848");
		//
		try {
			t1.repost(new Tweet(ht));
			fail("test: 5");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 6");
		}
		//
		try {
			t1 = TweetER.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
			TweetER t2 = TweetER.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_2));
			//
			Tweet tw = new Tweet("Test at " + System.currentTimeMillis() + " milis.");
			tw = t1.post(tw);
			//
			Tweet tww = new Tweet();
			tww.setData(tw);
			//
			Tweet rtw = t2.repost(tww);
			//
			assertNotNull("test: 7", rtw.getRepostedTweet());
			assertTrue("test: 8", rtw.getRepostedTweet().size() > 0);
			assertTrue("test: 9", tw.equals(rtw.getRepostedTweet()));
		} catch (Exception e) {
			fail("test: 10");
		}
		//
		try {
			t1.repost(new Tweet(ht));
			fail("test: 11");
		} catch (InvalidQueryException e) {
		} catch (Exception e) {
			fail("test: 12");
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.TweetER#send(com.twitterapime.search.Tweet)}.
	 */
	public void testSend() {
		TweetER t = TweetER.getInstance();
		//
		try {
			t.send(null);
			fail("test: 1");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			t.send(new Tweet());
			fail("test: 3");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		try {
			t.send(new Tweet(null, "direct message"));
			fail("test: 5");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 6");
		}
		//
		try {
			t.send(new Tweet("1234567890", (String)null));
			fail("test: 7");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 8");
		}
		//
		try {
			t.send(new Tweet("1234567890", "direct message"));
			fail("test: 9");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 10");
		}
		//
		try {
			t = TweetER.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
			//
			String msg = "DM at " + System.currentTimeMillis() + " milis.";
			Tweet tw1 = new Tweet("twiterapimetst2", msg);
			Tweet tw2 = t.send(tw1);
			//
			assertSame("test: 11", tw1, tw2);
			assertEquals("test: 12", msg, tw2.getString(MetadataSet.TWEET_CONTENT));
			assertNotNull("test: 13", tw2.getRecipientAccount());
			assertTrue("test: 14", tw2.getRecipientAccount().size() > 0);
			assertEquals("test: 15", "twiterapimetst2", tw2.getRecipientAccount().getString(MetadataSet.USERACCOUNT_USER_NAME));
		} catch (IOException e) {
			fail("test: 16");
		} catch (LimitExceededException e) {
			fail("test: 17");
		}
	}
}