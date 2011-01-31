package com.twitterapime.rest;

import com.twitterapime.search.QueryComposer;
import com.twitterapime.test.TwitterAPIMETestCase;
import com.twitterapime.xauth.Token;

/**
 * @author Ernandes Jr
 *
 */
public class FriendshipManagerTest extends TwitterAPIMETestCase {
	/**
	 * 
	 */
	public FriendshipManagerTest() {
		super("FriendshipManagerTest");
	}

	/**
	 * 
	 */
	public void testGetInstanceUserAccountManager() {
		UserAccountManager uam = UserAccountManager.getInstance(new Credential("a", "b", "c", new Token("a", "b")));
		//
		try {
			FriendshipManager.getInstance(uam);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			uam.verifyCredential();
			FriendshipManager.getInstance(uam);
		} catch (Exception e) {
			fail();
		}
		//
		assertSame(FriendshipManager.getInstance(uam), FriendshipManager.getInstance(uam));
	}

	/**
	 * 
	 */
	public void testGetInstance() {
		assertNotNull(FriendshipManager.getInstance());
		assertSame(FriendshipManager.getInstance(), FriendshipManager.getInstance());
	}

	/**
	 * 
	 */
	public void testGetFriendsIDQuery() {
		try {
			FriendshipManager.getInstance().getFriendsID(null);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		UserAccountManager uam = UserAccountManager.getInstance(new Credential("1", "2", "3", new Token("a", "b")));
		//
		try {
			FriendshipManager.getInstance(uam).getFriendsID(null);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * 
	 */
	public void testGetFriendsIDUserAccountQuery() {
		try {
			FriendshipManager.getInstance().getFriendsID(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			FriendshipManager f = FriendshipManager.getInstance();
			String[] ids = f.getFriendsID(new UserAccount("ernandesmjr"), null);
			//
			assertNotNull(ids);
			assertTrue(ids.length > 0);
			//
			ids = f.getFriendsID(new UserAccount("ernandesmjr"), QueryComposer.count(10));
			assertNotNull(ids);
			assertEquals(10, ids.length);
		} catch (Exception e) {
			fail();
		}
		//
		try {
			FriendshipManager.getInstance().getFriendsID(new UserAccount(), null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * 
	 */
	public void testGetFollowersIDQuery() {
		try {
			FriendshipManager.getInstance().getFollowersID(null);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		UserAccountManager uam = UserAccountManager.getInstance(new Credential("x", "y", "z", new Token("a", "b")));
		//
		try {
			FriendshipManager.getInstance(uam).getFollowersID(null);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * 
	 */
	public void testGetFollowersIDUserAccountQuery() {
		try {
			FriendshipManager.getInstance().getFollowersID(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			FriendshipManager f = FriendshipManager.getInstance();
			String[] ids = f.getFollowersID(new UserAccount("ernandesmjr"), null);
			//
			assertNotNull(ids);
			assertTrue(ids.length > 0);
			//
			ids = f.getFollowersID(new UserAccount("ernandesmjr"), QueryComposer.count(10));
			assertNotNull(ids);
			assertEquals(10, ids.length);
		} catch (Exception e) {
			fail();
		}
		//
		try {
			FriendshipManager.getInstance().getFollowersID(new UserAccount(), null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}
}