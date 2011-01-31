/**
 * 
 */
package com.twitterapime.search;

import com.twitterapime.test.TwitterAPIMETestCase;

/**
 * @author Main
 *
 */
public class QueryTest extends TwitterAPIMETestCase {
	/**
	 * 
	 */
	public QueryTest() {
		super("QueryTest");
	}

	/**
	 * Test method for {@link com.twitterapime.search.Query#hashCode()}.
	 */
	public void testHashCode() {
		assertEquals("query".hashCode(), new Query("query").hashCode());
	}

	/**
	 * Test method for {@link com.twitterapime.search.Query#Query(java.lang.String)}.
	 */
	public void testQuery() {
		try {
			new Query(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Query("");
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Query("query");
		} catch (Exception e) {
			fail();
		}
		//
//		try {
//			new Query(new String(new byte[] {-61})); //invalid utf-8 char.
//			fail();
//		} catch (RuntimeException e) {
//		} catch (Exception e) {
//			fail();
//		}
	}

	/**
	 * Test method for {@link com.twitterapime.search.Query#equals(java.lang.Object)}.
	 */
	public void testEqualsObject() {
		Query q = new Query("query");
		assertTrue(q.equals(q));
		assertTrue(q.equals(new Query("query")));
		assertFalse(q.equals(null));
		assertFalse(q.equals("query"));
		assertFalse(q.equals(new Query("other query")));
	}

	/**
	 * Test method for {@link com.twitterapime.search.Query#toString()}.
	 */
	public void testToString() {
		assertEquals("query", new Query("query").toString());
		assertEquals("", new Query("").toString());
	}
}