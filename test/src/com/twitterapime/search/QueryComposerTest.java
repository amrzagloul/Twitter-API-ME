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
		assertEquals(new Query("q1&q2"), QueryComposer.append(new Query("q1"), new Query("q2")));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#containAll(java.lang.String)}.
	 */
	public void testContainAll() {
		assertEquals(new Query(QueryComposer.PM_CONTAIN_ALL), QueryComposer.containAll(""));
		assertEquals(new Query(QueryComposer.PM_CONTAIN_ALL + "Twitter API ME"), QueryComposer.containAll("Twitter API ME"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#containExact(java.lang.String)}.
	 */
	public void testContainExact() {
		assertEquals(new Query(QueryComposer.PM_CONTAIN_EXACT), QueryComposer.containExact(""));
		assertEquals(new Query(QueryComposer.PM_CONTAIN_EXACT + "Twitter API ME"), QueryComposer.containExact("Twitter API ME"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#containAny(java.lang.String)}.
	 */
	public void testContainAny() {
		assertEquals(new Query(QueryComposer.PM_CONTAIN_ANY), QueryComposer.containAny(""));
		assertEquals(new Query(QueryComposer.PM_CONTAIN_ANY + "Twitter API ME"), QueryComposer.containAny("Twitter API ME"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#containNone(java.lang.String)}.
	 */
	public void testContainNone() {
		assertEquals(new Query(QueryComposer.PM_CONTAIN_NONE), QueryComposer.containNone(""));
		assertEquals(new Query(QueryComposer.PM_CONTAIN_NONE + "Twitter API ME"), QueryComposer.containNone("Twitter API ME"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#containHashtag(java.lang.String)}.
	 */
	public void testContainHashtag() {
		assertEquals(new Query(QueryComposer.PM_CONTAIN_HASHTAG), QueryComposer.containHashtag(""));
		assertEquals(new Query(QueryComposer.PM_CONTAIN_HASHTAG + "#twitterapime"), QueryComposer.containHashtag("#twitterapime"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#from(java.lang.String)}.
	 */
	public void testFrom() {
		assertEquals(new Query(QueryComposer.PM_FROM), QueryComposer.from(""));
		assertEquals(new Query(QueryComposer.PM_FROM + "twitterapime"), QueryComposer.from("twitterapime"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#reference(java.lang.String)}.
	 */
	public void testReference() {
		assertEquals(new Query(QueryComposer.PM_REFERENCE), QueryComposer.reference(""));
		assertEquals(new Query(QueryComposer.PM_REFERENCE + "twitterapime"), QueryComposer.reference("twitterapime"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#to(java.lang.String)}.
	 */
	public void testTo() {
		assertEquals(new Query(QueryComposer.PM_TO), QueryComposer.to(""));
		assertEquals(new Query(QueryComposer.PM_TO + "twitterapime"), QueryComposer.to("twitterapime"));
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
		assertEquals(new Query(QueryComposer.PM_SINCE + "2009-12-23"), QueryComposer.since(c.getTime()));
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
		assertEquals(new Query(QueryComposer.PM_UNTIL + "2009-12-23"), QueryComposer.until(c.getTime()));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#sinceID(java.lang.String)}.
	 */
	public void testSinceID() {
		assertEquals(new Query(QueryComposer.PM_SINCE_ID), QueryComposer.sinceID(""));
		assertEquals(new Query(QueryComposer.PM_SINCE_ID + "1234567890"), QueryComposer.sinceID("1234567890"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#lang(java.lang.String)}.
	 */
	public void testLang() {
		assertEquals(new Query(QueryComposer.PM_LANG), QueryComposer.lang(""));
		assertEquals(new Query(QueryComposer.PM_LANG + "en"), QueryComposer.lang("en"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#source(java.lang.String)}.
	 */
	public void testSource() {
		assertEquals(new Query(QueryComposer.PM_SOURCE), QueryComposer.source(""));
		assertEquals(new Query(QueryComposer.PM_SOURCE + "web"), QueryComposer.source("web"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#resultCount(int)}.
	 */
	public void testResultCount() {
		assertEquals(new Query(QueryComposer.PM_RPP + "6"), QueryComposer.resultCount(6));
		assertEquals(new Query(QueryComposer.PM_RPP + "7"), QueryComposer.resultCount(7));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#page(int)}.
	 */
	public void testPage() {
		assertEquals(new Query(QueryComposer.PM_PAGE + "6"), QueryComposer.page(6));
		assertEquals(new Query(QueryComposer.PM_PAGE + "7"), QueryComposer.page(7));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#paginate(int, int)}.
	 */
	public void testPaginate() {
		assertEquals(new Query(QueryComposer.PM_RPP + "6&" + QueryComposer.PM_PAGE + "15"), QueryComposer.paginate(6, 15));
		assertEquals(new Query(QueryComposer.PM_RPP + "7&" + QueryComposer.PM_PAGE + "20"), QueryComposer.paginate(7, 20));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#geocode(java.lang.String, java.lang.String, int, java.lang.String)}.
	 */
	public void testGeocode() {
		assertEquals(new Query(QueryComposer.PM_GEOCODE + "75.56,36.98,10km"), QueryComposer.geocode("75.56", "36.98", 10, "km"));
		assertEquals(new Query(QueryComposer.PM_GEOCODE + "23.98,67.17,100mi"), QueryComposer.geocode("23.98", "67.17", 100, "mi"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#positiveAttitude()}.
	 */
	public void testPositiveAttitude() {
		assertEquals(new Query(QueryComposer.PM_POSITIVE_ATTITUDE), QueryComposer.positiveAttitude());
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#negativeAttitude()}.
	 */
	public void testNegativeAttitude() {
		assertEquals(new Query(QueryComposer.PM_NEGATIVE_ATTITUDE), QueryComposer.negativeAttitude());
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#containLink()}.
	 */
	public void testContainLink() {
		assertEquals(new Query(QueryComposer.PM_FILTER_LINKS), QueryComposer.containLink());
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#containQuestion()}.
	 */
	public void testContainQuestion() {
		assertEquals(new Query(QueryComposer.PM_ASKING_QUESTION), QueryComposer.containQuestion());
	}
	
	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#count(int)}.
	 */
	public void testCount() {
		assertEquals(new Query(QueryComposer.PM_COUNT + "6"), QueryComposer.count(6));
		assertEquals(new Query(QueryComposer.PM_COUNT + "7"), QueryComposer.count(7));
	}
	
	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#maxID(String)}.
	 */
	public void testMaxID() {
		assertEquals(new Query(QueryComposer.PM_MAX_ID + "654321"), QueryComposer.maxID("654321"));
		assertEquals(new Query(QueryComposer.PM_MAX_ID + "7890"), QueryComposer.maxID("7890"));
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#date(java.util.Date)}.
	 */
	public void testDate() {
		try {
			QueryComposer.date(null);
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
		assertEquals(new Query(QueryComposer.PM_DATE + "2009-12-23"), QueryComposer.date(c.getTime()));
	}
	
	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#excludeHashtags()}.
	 */
	public void testExcludeHashtags() {
		assertEquals(new Query(QueryComposer.PM_EXCLUDE_HASHTAGS), QueryComposer.excludeHashtags());
	}

	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#includeEntities()}.
	 */
	public void testIncludeEntities() {
		assertEquals(new Query(QueryComposer.PM_INCLUDE_ENTITIES), QueryComposer.includeEntities());
	}
	
	/**
	 * Test method for {@link com.twitterapime.search.QueryComposer#perPage(int)}.
	 */
	public void testPerPage() {
		assertEquals(new Query(QueryComposer.PM_PER_PAGE + "6"), QueryComposer.perPage(6));
		assertEquals(new Query(QueryComposer.PM_PER_PAGE + "7"), QueryComposer.perPage(7));
	}
}