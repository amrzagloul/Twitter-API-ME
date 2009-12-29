/**
 * 
 */
package com.twitterapime.search;

import java.util.Calendar;

import com.sonyericsson.junit.framework.TestCase;

/**
 * @author Main
 *
 */
public class QueryComposerTest extends TestCase {
	/**
	 * 
	 */
	public QueryComposerTest() {
		super("QueryComposerTest");
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#append(com.twitterapime.search.Query, com.twitterapime.search.Query)}.
	 */
	public void testAppend() {
		try {
			QueryComposer.append(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			QueryComposer.append(new Query("q"), null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			QueryComposer.append(null, new Query("q"));
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			QueryComposer.append(new Query("q1"), new Query("q2"));
		} catch (Exception e) {
			fail();
		}
		//
		assertEquals(new Query("q1q2"), QueryComposer.append(new Query("q1"), new Query("q2")));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#containAll(java.lang.String)}.
	 */
	public void testContainAll() {
		assertEquals(new Query("&ands="), QueryComposer.containAll(""));
		assertEquals(new Query("&ands=Twitter API ME"), QueryComposer.containAll("Twitter API ME"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#containExact(java.lang.String)}.
	 */
	public void testContainExact() {
		assertEquals(new Query("&phrase="), QueryComposer.containExact(""));
		assertEquals(new Query("&phrase=Twitter API ME"), QueryComposer.containExact("Twitter API ME"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#containAny(java.lang.String)}.
	 */
	public void testContainAny() {
		assertEquals(new Query("&ors="), QueryComposer.containAny(""));
		assertEquals(new Query("&ors=Twitter API ME"), QueryComposer.containAny("Twitter API ME"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#containNone(java.lang.String)}.
	 */
	public void testContainNone() {
		assertEquals(new Query("&nots="), QueryComposer.containNone(""));
		assertEquals(new Query("&nots=Twitter API ME"), QueryComposer.containNone("Twitter API ME"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#containHashtag(java.lang.String)}.
	 */
	public void testContainHashtag() {
		assertEquals(new Query("&tag="), QueryComposer.containHashtag(""));
		assertEquals(new Query("&tag=#twitterapime"), QueryComposer.containHashtag("#twitterapime"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#from(java.lang.String)}.
	 */
	public void testFrom() {
		assertEquals(new Query("&from="), QueryComposer.from(""));
		assertEquals(new Query("&from=twitterapime"), QueryComposer.from("twitterapime"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#reference(java.lang.String)}.
	 */
	public void testReference() {
		assertEquals(new Query("&ref="), QueryComposer.reference(""));
		assertEquals(new Query("&ref=twitterapime"), QueryComposer.reference("twitterapime"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#to(java.lang.String)}.
	 */
	public void testTo() {
		assertEquals(new Query("&to="), QueryComposer.to(""));
		assertEquals(new Query("&to=twitterapime"), QueryComposer.to("twitterapime"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#since(java.util.Date)}.
	 */
	public void testSince() {
		try {
			QueryComposer.since(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2009);
		c.set(Calendar.MONTH, 11);
		c.set(Calendar.DAY_OF_MONTH, 23);
		assertEquals(new Query("&since=2009-12-23"), QueryComposer.since(c.getTime()));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#until(java.util.Date)}.
	 */
	public void testUntil() {
		try {
			QueryComposer.until(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2009);
		c.set(Calendar.MONTH, 11);
		c.set(Calendar.DAY_OF_MONTH, 23);
		assertEquals(new Query("&until=2009-12-23"), QueryComposer.until(c.getTime()));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#sinceID(java.lang.String)}.
	 */
	public void testSinceID() {
		assertEquals(new Query("&since_id="), QueryComposer.sinceID(""));
		assertEquals(new Query("&since_id=1234567890"), QueryComposer.sinceID("1234567890"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#lang(java.lang.String)}.
	 */
	public void testLang() {
		assertEquals(new Query("&lang="), QueryComposer.lang(""));
		assertEquals(new Query("&lang=en"), QueryComposer.lang("en"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#source(java.lang.String)}.
	 */
	public void testSource() {
		assertEquals(new Query("&source="), QueryComposer.source(""));
		assertEquals(new Query("&source=web"), QueryComposer.source("web"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#resultCount(int)}.
	 */
	public void testResultCount() {
		assertEquals(new Query("&rpp=6"), QueryComposer.resultCount(6));
		assertEquals(new Query("&rpp=7"), QueryComposer.resultCount(7));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#page(int)}.
	 */
	public void testPage() {
		assertEquals(new Query("&page=6"), QueryComposer.page(6));
		assertEquals(new Query("&page=7"), QueryComposer.page(7));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#paginate(int, int)}.
	 */
	public void testPaginate() {
		assertEquals(new Query("&rpp=6&page=15"), QueryComposer.paginate(6, 15));
		assertEquals(new Query("&rpp=7&page=20"), QueryComposer.paginate(7, 20));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#geocode(java.lang.String, java.lang.String, int, java.lang.String)}.
	 */
	public void testGeocode() {
		assertEquals(new Query("&geocode=75.56,36.98,10km"), QueryComposer.geocode("75.56", "36.98", 10, "km"));
		assertEquals(new Query("&geocode=23.98,67.17,100mi"), QueryComposer.geocode("23.98", "67.17", 100, "mi"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#positiveAttitude()}.
	 */
	public void testPositiveAttitude() {
		assertEquals(new Query("&tude[]=:)"), QueryComposer.positiveAttitude());
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#negativeAttitude()}.
	 */
	public void testNegativeAttitude() {
		assertEquals(new Query("&tude[]=:("), QueryComposer.negativeAttitude());
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#containLink()}.
	 */
	public void testContainLink() {
		assertEquals(new Query("&filter=links"), QueryComposer.containLink());
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#containQuestion()}.
	 */
	public void testContainQuestion() {
		assertEquals(new Query("&tude[]=?"), QueryComposer.containQuestion());
	}
}