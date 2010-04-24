/**
 * 
 */
package com.twitterapime.search;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.rest.RateLimitStatus;

/**
 * @author Main
 *
 */
public class SearchDeviceTest extends TestCase implements SearchDeviceListener {
	/**
	 * 
	 */
	private boolean finished;
	
	/**
	 * 
	 */
	private int tweetsFoundCount;
	
	/**
	 * 
	 */
	private int tweetsFromUserCount;

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
		Query q = QueryComposer.from("ernandesmjr");
		//
		try {
			Tweet[] ts = s.searchTweets(q);
			//
			assertNotNull(ts);
			assertTrue(ts.length > 0);
			//
			for (int i = 0; i < ts.length; i++) {
				assertEquals("ernandesmjr", ts[i].getString(MetadataSet.TWEET_AUTHOR_USERNAME));
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
		Query q = QueryComposer.from("ernandesmjr");
		//
		try {
			Tweet[] ts = s.searchTweets("q=" + q.toString());
			//
			assertNotNull(ts);
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
			tweetsFoundCount = 0;
			tweetsFromUserCount = 0;
			//
			s.startSearchTweets(q, this);
			//
			waitFor(10000);
			//
			assertTrue(tweetsFoundCount > 0);
			assertEquals(tweetsFoundCount, tweetsFromUserCount);
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
			tweetsFoundCount = 0;
			tweetsFromUserCount = 0;
			//
			s.startSearchTweets(q.toString(), this);
			//
			waitFor(10000);
			//
			assertTrue(tweetsFoundCount > 0);
			assertEquals(tweetsFoundCount, tweetsFromUserCount);
		} catch (Exception e) {
			fail();
		}
		//
		try {
			tweetsFoundCount = 0;
			//
			s.startSearchTweets("?", this);
			//
			waitFor(10000);
			//
			assertEquals(-1, tweetsFoundCount);
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
	 * Test method for {@link com.twitterapime.search.SearchDevice#getRateLimitStatus()}.
	 */
	public void testGetRateLimitStatus() {
		try {
			SearchDevice sd = SearchDevice.getInstance();
			//
			RateLimitStatus rls = sd.getRateLimitStatus();
			assertNotNull(rls);
			assertTrue(rls.size() > 0);
			//
			assertTrue(rls.getString(MetadataSet.RATELIMITSTATUS_HOURLY_LIMIT).length() > 0);
			assertTrue(rls.getString(MetadataSet.RATELIMITSTATUS_REMAINING_HITS).length() > 0);
			assertTrue(rls.getString(MetadataSet.RATELIMITSTATUS_RESET_TIME).length() > 0);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#searchCompleted()
	 */
	public void searchCompleted() {
		finished = true;
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#searchFailed(java.lang.Throwable)
	 */
	public void searchFailed(Throwable cause) {
		if (cause instanceof InvalidQueryException) {
			tweetsFoundCount = -1;
		}
		//
		finished = true;
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#tweetFound(com.twitterapime.search.Tweet)
	 */
	public void tweetFound(Tweet tweet) {
		tweetsFoundCount++;
		if ("twiterapime".equals(tweet.getString(MetadataSet.TWEET_AUTHOR_USERNAME))) {
			tweetsFromUserCount++;
		}
	}
	
	/**
	 * @param maxTime
	 */
	public void waitFor(long maxTime) {
		finished = false;
		//
		int st = (int)(maxTime / 1000);
		//
		while (st > 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			st--;
			//
			if (finished) {
				break;
			}
		}
	}
}