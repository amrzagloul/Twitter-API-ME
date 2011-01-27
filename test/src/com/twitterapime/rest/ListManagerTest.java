/**
 * 
 */
package com.twitterapime.rest;

import com.twitterapime.model.MetadataSet;


/**
 * @author 82177082315
 *
 */
public class ListManagerTest extends com.sonyericsson.junit.framework.TestCase {
	/**
	 * 
	 */
	private UserAccountManager userMngr1;

	/**
	 * 
	 */
	public ListManagerTest() {
		super("ListManagerTest");
	}
	
	/**
	 * @see com.sonyericsson.junit.framework.TestCase#setUp()
	 */
	public void setUp() throws Throwable {
		String conKey = UserAccountManagerTest.CONSUMER_KEY;
		String conSec = UserAccountManagerTest.CONSUMER_SECRET;
		//
		Credential c =
			new Credential("twiterapimetest", "f00bar", conKey, conSec);
		//
		userMngr1 = UserAccountManager.getInstance(c);
		//
		if (!userMngr1.verifyCredential()) {
			throw new IllegalStateException("ListManagerTest: Login failed!");
		}
	}
	
	/**
	 * @see com.sonyericsson.junit.framework.TestCase#tearDown()
	 */
	public void tearDown() throws Throwable {
		if (userMngr1 != null) {
			userMngr1.signOut();
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#getInstance(UserAccountManager)}.
	 */
	public void testGetInstanceUserAccountManager() {
		try {
			ListManager.getInstance(null);
			fail("test: 1");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			ListManager.getInstance(userMngr1);
			fail("test: 3");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		ListManager t = ListManager.getInstance(userMngr1);
		assertNotNull("test: 5", t);
		assertSame("test: 6", t, ListManager.getInstance(userMngr1));
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#getInstance()}.
	 */
	public void testGetInstance() {
		ListManager t = ListManager.getInstance();
		assertNotNull("test: 1", t);
		assertSame("test: 2", t, ListManager.getInstance());
	}

	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#getLists()}.
	 */
	public void testGetList() {
		ListManager lm = ListManager.getInstance();
		//
		try {
			lm.getLists();
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		lm = ListManager.getInstance(userMngr1);
		//
		try {
			List l[] = lm.getLists();
			//
			assertNotNull(l);
			assertTrue(l.length > 0);
			//
			boolean publicFound = false;
			boolean privateFound = false;
			//
			for (int i = 0; i < l.length; i++) {
				UserAccount ua = l[i].getUserAccount();
				//
				assertEquals("twiterapimetest", ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
				//
				publicFound = publicFound || "public".equals(l[i].getString(MetadataSet.LIST_MODE));
				privateFound = privateFound || "private".equals(l[i].getString(MetadataSet.LIST_MODE));
			}
			//
			assertTrue(publicFound);
			assertTrue(privateFound);
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#getLists(UserAccount)}.
	 */
	public void testGetListUserAccount() {
		ListManager lm = ListManager.getInstance();
		//
		try {
			lm.getLists(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			lm.getLists(new UserAccount());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			List l[] = lm.getLists(new UserAccount("twiterapimetest"));
			//
			assertNotNull(l);
			assertTrue(l.length > 0);
			//
			for (int i = 0; i < l.length; i++) {
				UserAccount ua = l[i].getUserAccount();
				//
				assertEquals("twiterapimetest", ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
				assertEquals("public", l[i].getString(MetadataSet.LIST_MODE));
			}
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#create(List)}.
	 */
	public void testCreate() {
		ListManager lm = ListManager.getInstance();
		//
		try {
			lm.create(new List());
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		lm = ListManager.getInstance(userMngr1);
		//
		try {
			lm.create(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			lm.create(new List());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		List l = null;
		//
		try {
			String name = "Name " + System.currentTimeMillis();
			l = lm.create(new List(name, true, "Description"));
			//
			assertNotNull(l);
			assertEquals(name, l.getString(MetadataSet.LIST_NAME));
			assertEquals("public", l.getString(MetadataSet.LIST_MODE));
			assertEquals("Description", l.getString(MetadataSet.LIST_DESCRIPTION));
		} catch (Exception e) {
			fail();
		} finally {
			if (l != null) {
				try {
					lm.delete(l);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#delete(List)}.
	 */
	public void testDelete() {
		ListManager lm = ListManager.getInstance();
		//
		try {
			lm.delete(new List());
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		lm = ListManager.getInstance(userMngr1);
		//
		try {
			lm.delete(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			lm.delete(new List());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			List nl = lm.create(new List("Name " + System.currentTimeMillis(), true, "Description"));
			List dl = lm.delete(nl);
			//
			assertNotNull(nl);
			assertNotNull(dl);
			assertEquals(nl, dl);
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#update(List)}.
	 */
	public void testUpdate() {
		ListManager lm = ListManager.getInstance();
		//
		try {
			lm.update(new List());
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		lm = ListManager.getInstance(userMngr1);
		//
		try {
			lm.update(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			lm.update(new List());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#getMemberships()}.
	 */
	public void testGetMemberships() {
		ListManager lm = ListManager.getInstance();
		//
		try {
			lm.getMemberships();
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		lm = ListManager.getInstance(userMngr1);
		//
		try {
			lm.getMemberships(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			lm.getMemberships(new UserAccount());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			List l[] = lm.getMemberships();
			//
			assertNotNull(l);
			assertTrue(l.length > 0);
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#getSubscriptions()}.
	 */
	public void testGetSubscriptions() {
		ListManager lm = ListManager.getInstance();
		//
		try {
			lm.getSubscriptions();
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		lm = ListManager.getInstance(userMngr1);
		//
		try {
			lm.getSubscriptions(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			lm.getSubscriptions(new UserAccount());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			List l[] = lm.getSubscriptions();
			//
			assertNotNull(l);
			assertTrue(l.length > 0);
		} catch (Exception e) {
			fail();
		}
	}
}
