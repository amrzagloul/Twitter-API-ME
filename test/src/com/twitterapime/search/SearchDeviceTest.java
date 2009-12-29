/**
 * 
 */
package com.twitterapime.search;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;

/**
 * @author Main
 *
 */
public class SearchDeviceTest extends TestCase implements SearchDeviceListener {
	/**
	 * 
	 */
	private int tweetsFoundCount;

	/**
	 * 
	 */
	public SearchDeviceTest() {
		super("SearchDeviceTest");
	}

	/**
	 * Test method for {@link com.twitterapime.search.SearchDevice#getInstance()}.
	 */
	public void testGetInstance() {
		SearchDevice s = SearchDevice.getInstance();
		assertNotNull(s);
		assertSame(s, SearchDevice.getInstance());
	}

	/**
	 * Test method for {@link com.twitterapime.search.SearchDevice#searchTweets(com.twitterapime.search.Query)}.
	 */
	public void testSearchTweetsQuery() {
		SearchDevice s = SearchDevice.getInstance();
		//
		try {
			s.searchTweets((Query)null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Query q = QueryComposer.from("twiterapime");
		//
		try {
			Tweet[] ts = s.searchTweets(q);
			//
			assertNotNull(ts);
			assertTrue(ts.length > 0);
			//
			for (int i = 0; i < ts.length; i++) {
				assertEquals("twiterapime", ts[i].getString(MetadataSet.TWEET_AUTHOR_USERNAME));
			}
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.search.SearchDevice#searchTweets(java.lang.String)}.
	 */
	public void testSearchTweetsString() {
		SearchDevice s = SearchDevice.getInstance();
		//
		try {
			s.searchTweets((String)null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			s.searchTweets("");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Query q = QueryComposer.from("twiterapime");
		//
		try {
			Tweet[] ts = s.searchTweets("q=" + q.toString());
			//
			assertTrue(ts.length > 0);
		} catch (Exception e) {
			fail();
		}
		//
		try {
			s.searchTweets("?blablabla");
			fail();
		} catch (InvalidQueryException e) {
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.search.SearchDevice#startSearchTweets(com.twitterapime.search.Query, com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartSearchTweetsQuerySearchDeviceListener() {
		SearchDevice s = SearchDevice.getInstance();
		//
		try {
			s.startSearchTweets((Query)null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Query q = QueryComposer.from("twiterapime");
		//
		try {
			s.startSearchTweets(q, this);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.search.SearchDevice#startSearchTweets(java.lang.String, com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartSearchTweetsStringSearchDeviceListener() {
		SearchDevice s = SearchDevice.getInstance();
		//
		try {
			s.startSearchTweets((String)null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			s.startSearchTweets("", null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Query q = QueryComposer.from("twiterapime");
		//
		try {
			s.startSearchTweets(q.toString(), this);
		} catch (Exception e) {
			fail();
		}
		//
		try {
			s.startSearchTweets("?", this);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.search.SearchDevice#getAPICallsCount()}.
	 */
	public void testGetAPICallsCount() {
		SearchDevice s = SearchDevice.getInstance();
		int apiCount = s.getAPICallsCount();
		//
		try {
			s.searchTweets(QueryComposer.from("twiterapime"));
			//
			assertEquals(apiCount +1, s.getAPICallsCount());
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.search.SearchDevice#getLastAPICallTime()}.
	 */
	public void testGetLastAPICallTime() {
		SearchDevice s = SearchDevice.getInstance();
		long now = s.getLastAPICallTime();
		//
		try {
			s.searchTweets(QueryComposer.from("twiterapime"));
			//
			assertTrue(s.getLastAPICallTime() > now);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#searchCompleted()
	 */
	public void searchCompleted() {
		if (tweetsFoundCount == 0) {
			fail();
		}
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#searchFailed(java.lang.Throwable)
	 */
	public void searchFailed(Throwable cause) {
		assertTrue(cause instanceof InvalidQueryException);
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#tweetFound(com.twitterapime.search.Tweet)
	 */
	public void tweetFound(Tweet tweet) {
		assertEquals("twiterapime", tweet.getString(MetadataSet.TWEET_AUTHOR_USERNAME));
	}
}