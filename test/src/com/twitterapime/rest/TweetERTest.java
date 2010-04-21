/**
 * 
 */
package com.twitterapime.rest;

import java.io.IOException;
import java.util.Hashtable;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.InvalidQueryException;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Tweet;

/**
 * @author Main
 *
 */
public class TweetERTest extends TestCase {
	/**
	 * 
	 */
	public TweetERTest() {
		super("TweetERTest");
	}

	/**
	 * Test method for {@link com.twitterapime.rest.TweetER#getInstance(com.twitterapime.rest.UserAccountManager)}.
	 */
	public void testGetInstanceUserAccountManager() {
		try {
			TweetER.getInstance(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			TweetER.getInstance(uam);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			assertTrue(uam.verifyCredential());
			//
			TweetER t = TweetER.getInstance(uam);
			assertNotNull(t);
			assertSame(t, TweetER.getInstance(uam));			
		} catch (IOException e) {
			fail();
		}
		//
		try {
			uam.signOut();
		} catch (Exception e) {
			fail();
		}
		//
		UserAccountManager uam2 = UserAccountManager.getInstance(c);
		try {
			uam2.verifyCredential();
		} catch (Exception e) {
			fail();
		}
		//
		assertNotSame(uam, TweetER.getInstance(uam2));
		//
		try {
			uam2.signOut();
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.TweetER#getInstance()}.
	 */
	public void testGetInstance() {
		TweetER t = TweetER.getInstance();
		assertNotNull(t);
		assertSame(t, TweetER.getInstance());
	}

	/**
	 * Test method for {@link com.twitterapime.rest.TweetER#findByID(java.lang.String)}.
	 */
	public void testFindByID() {
		TweetER t = TweetER.getInstance();
		//
		try {
			t.findByID(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.findByID("");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.findByID("7141196879");
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			assertNull(t.findByID("9890asdh9809"));
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Tweet tw = t.findByID("7041609437");
			assertNotNull(tw);
			assertEquals("7041609437", tw.getString(MetadataSet.TWEET_ID));
		} catch (Exception e) {
			fail();
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
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.post(new Tweet());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.post(new Tweet("Hello, Twitters!!!"));
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			uam.verifyCredential();
			//
			t = TweetER.getInstance(uam);
			//
			String msg = "Test at " + System.currentTimeMillis() + " milis.";
			Tweet tw1 = new Tweet(msg);
			Tweet tw2 = t.post(tw1);
			//
			assertSame(tw1, tw2);
			assertEquals(msg, tw2.getString(MetadataSet.TWEET_CONTENT));
		} catch (IOException e) {
			fail();
		} catch (LimitExceededException e) {
			fail();
		}
		//
		try {
			uam.signOut();
		} catch (Exception e) {
			fail();
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
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t1.repost(new Tweet());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Hashtable ht = new Hashtable();
		ht.put(MetadataSet.TWEET_ID, "4646461316848");
		//
		try {
			t1.repost(new Tweet(ht));
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Credential c1 = new Credential("twiterapimetst2", "f00bar");
		UserAccountManager uam1 = UserAccountManager.getInstance(c1);
		Credential c2 = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam2 = UserAccountManager.getInstance(c2);
		//
		try {
			assertTrue(uam1.verifyCredential());
			assertTrue(uam2.verifyCredential());
			//
			t1 = TweetER.getInstance(uam1);
			TweetER t2 = TweetER.getInstance(uam2);
			//
			Tweet tw = new Tweet("Test at " + System.currentTimeMillis() + " milis.");
			tw = t1.post(tw);
			//
			Tweet tww = new Tweet();
			tww.setData(tw);
			//
			Tweet rtw = t2.repost(tww);
			//
			assertNotNull(rtw.getRepostedTweet());
			assertTrue(rtw.getRepostedTweet().size() > 0);
			assertTrue(tw.equals(rtw.getRepostedTweet()));
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t1.repost(new Tweet(ht));
			fail();
		} catch (InvalidQueryException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			uam1.signOut();
		} catch (Exception e) {
			fail();
		}
		try {
			uam2.signOut();
		} catch (Exception e) {
			fail();
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
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.send(new Tweet());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.send(new Tweet(null, "direct message"));
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.send(new Tweet("1234567890", null));
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.send(new Tweet("1234567890", "direct message"));
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			assertTrue(uam.verifyCredential());
			//
			t = TweetER.getInstance(uam);
			//
			String msg = "DM at " + System.currentTimeMillis() + " milis.";
			Tweet tw1 = new Tweet("twiterapimetst2", msg);
			Tweet tw2 = t.send(tw1);
			//
			assertSame(tw1, tw2);
			assertEquals(msg, tw2.getString(MetadataSet.TWEET_CONTENT));
			assertNotNull(tw2.getRecipientAccount());
			assertTrue(tw2.getRecipientAccount().size() > 0);
			assertEquals("twiterapimetst2", tw2.getRecipientAccount().getString(MetadataSet.USERACCOUNT_USER_NAME));
		} catch (IOException e) {
			fail();
		} catch (LimitExceededException e) {
			fail();
		}
		//
		try {
			uam.signOut();
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method related to {@link com.twitterapime.rest.UserAccountManager#signOut()}.
	 */
	public void testSignOut() {
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager u = UserAccountManager.getInstance(c);
		//
		try {
			assertTrue(u.verifyCredential());
			assertTrue(u.isVerified());
			//
			TweetER tr = TweetER.getInstance(u);
			//
			u.signOut();
			//
			u = UserAccountManager.getInstance(c);
			assertTrue(u.verifyCredential());
			assertTrue(u.isVerified());
			//
			assertNotSame(tr, TweetER.getInstance(u));
			//
			try {
				tr.post(new Tweet("text"));
				fail();
			} catch (IllegalStateException e) {
			}
			//
			u.signOut();
		} catch (Exception e) {
			fail();
		}
	}
}