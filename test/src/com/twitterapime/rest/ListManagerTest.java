/**
 * 
 */
package com.twitterapime.rest;

import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.test.TwitterAPIMETestCase;


/**
 * @author 82177082315
 *
 */
public class ListManagerTest extends TwitterAPIMETestCase {
	/**
	 * 
	 */
	public ListManagerTest() {
		super("ListManagerTest");
	}
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() {
		try {
			UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1, true);
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
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#getInstance(UserAccountManager)}.
	 */
	public void testGetInstanceUserAccountManager() {
		try {
			ListManager.getInstance(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			ListManager.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_3_NON_EXISTENT));
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		ListManager t = ListManager.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
		assertNotNull(t);
		assertSame(t, ListManager.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1)));
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
		lm = ListManager.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
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
		lm = ListManager.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
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
		lm = ListManager.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
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
		lm = ListManager.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
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
		//
		try {
			List l = lm.getLists()[0];
			String id = l.getString(MetadataSet.LIST_ID);
			String name = ("name-" + System.currentTimeMillis()).substring(0, 5);
			String desc = ("desc-" + System.currentTimeMillis()).substring(0, 5);
			//
			List nl = lm.update(new List(id, name, l.getString(MetadataSet.LIST_MODE).equals("public"), desc));
			//
			assertNotNull(nl);
			assertEquals(id, nl.getString(MetadataSet.LIST_ID));
			assertEquals(name, nl.getString(MetadataSet.LIST_NAME));
			assertEquals("public", nl.getString(MetadataSet.LIST_MODE));
			assertEquals(desc, nl.getString(MetadataSet.LIST_DESCRIPTION));
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
		lm = ListManager.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
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
			List l[] = lm.getMemberships(new UserAccount("twapime"));
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
		lm = ListManager.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
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
			List l[] = lm.getSubscriptions(new UserAccount("twapime"));
			//
			assertNotNull(l);
			assertTrue(l.length > 0);
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#subscribe(List)}.
	 */
	public void testSubscribe() {
		ListManager lm = ListManager.getInstance();
		//
		try {
			lm.subscribe(null);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		lm = ListManager.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
		//
		try {
			lm.subscribe(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			lm.subscribe(new List());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Hashtable data = new Hashtable();
		data.put(MetadataSet.LIST_ID, "213123123");
		//
		try {
			lm.subscribe(new List(data));
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		data.put(MetadataSet.LIST_USER_ACCOUNT, new UserAccount());
		//
		try {
			lm.subscribe(new List(data));
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		data.put(MetadataSet.LIST_USER_ACCOUNT, new UserAccount("twiterapimetest"));
		//
		try {
			lm.subscribe(new List(data));
		} catch (IllegalArgumentException e) {
			fail();
		} catch (Exception e) {
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#unsubscribe(List)}.
	 */
	public void testUnsubscribe() {
		ListManager lm = ListManager.getInstance();
		//
		try {
			lm.unsubscribe(null);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		lm = ListManager.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
		//
		try {
			lm.unsubscribe(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			lm.unsubscribe(new List());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Hashtable data = new Hashtable();
		data.put(MetadataSet.LIST_ID, "213123123");
		//
		try {
			lm.unsubscribe(new List(data));
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		data.put(MetadataSet.LIST_USER_ACCOUNT, new UserAccount());
		//
		try {
			lm.unsubscribe(new List(data));
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		data.put(MetadataSet.LIST_USER_ACCOUNT, new UserAccount("twiterapimetest"));
		//
		try {
			lm.unsubscribe(new List(data));
		} catch (IllegalArgumentException e) {
			fail();
		} catch (Exception e) {
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#addMember(List, UserAccount)}.
	 */
	public void testAddMember() {
		ListManager lm = ListManager.getInstance();
		//
		try {
			lm.addMember(null, null);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		lm = ListManager.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
		//
		try {
			lm.addMember(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			lm.addMember(new List(), new UserAccount());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Hashtable data = new Hashtable();
		data.put(MetadataSet.LIST_ID, "213123123");
		//
		try {
			lm.addMember(new List(data), new UserAccount());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		UserAccount ua = null;
		//
		try {
			ua = UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1).getUserAccount(new UserAccount("twitterapimetest"));
			//
			lm.addMember(new List(data), ua);
		} catch (IllegalArgumentException e) {
			fail();
		} catch (Exception e) {
		} finally {
			if (ua != null) {
				try {
					lm.removeMember(new List(data), ua);
				} catch (Exception e) {
				}
			}
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#removeMember(List, UserAccount)}.
	 */
	public void testRemoveMember() {
		ListManager lm = ListManager.getInstance();
		//
		try {
			lm.removeMember(null, null);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		lm = ListManager.getInstance(UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1));
		//
		try {
			lm.removeMember(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			lm.removeMember(new List(), new UserAccount());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Hashtable data = new Hashtable();
		data.put(MetadataSet.LIST_ID, "213123123");
		//
		try {
			lm.removeMember(new List(data), new UserAccount());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		UserAccount ua = null;
		//
		try {
			ua = UserAccountManagerTest.getUserAccountManager(UserAccountManagerTest.TEST_USER_1).getUserAccount(new UserAccount("twitterapimetest"));
			//
			lm.addMember(new List(data), ua);
			lm.removeMember(new List(data), ua);
		} catch (IllegalArgumentException e) {
			fail();
		} catch (Exception e) {
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.ListManager#getMembers(List)}.
	 * Test method for {@link com.twitterapime.rest.ListManager#getSubscribers(List)}.
	 */
	public void testProcessMembersSubscribersRequest() {
		ListManager lm = ListManager.getInstance();
		//
		try {
			lm.getMembers(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			lm.getMembers(new List());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Hashtable data = new Hashtable();
		data.put(MetadataSet.LIST_ID, "213123123");
		//
		try {
			lm.getMembers(new List(data));
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		data.put(MetadataSet.LIST_USER_ACCOUNT, new UserAccount());
		//
		try {
			lm.getMembers(new List(data));
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		data.put(MetadataSet.LIST_USER_ACCOUNT, new UserAccount("twiterapimetest"));
		//
		try {
			lm.getMembers(new List(data));
		} catch (IllegalArgumentException e) {
			fail();
		} catch (Exception e) {
		}
	}
}
