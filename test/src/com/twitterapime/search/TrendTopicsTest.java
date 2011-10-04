/**
 * 
 */
package com.twitterapime.search;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.test.TwitterAPIMETestCase;

/**
 * @author Ernandes Jr
 *
 */
public class TrendTopicsTest extends TwitterAPIMETestCase  {
	/**
	 * 
	 */
	public TrendTopicsTest() {
		super("TrendTopicsTest");
	}

	/**
	 * Test method for {@link com.twitterapime.search.TrendTopics#getInstance()}.
	 */
	public void testGetInstance() {
		TrendTopics tt = TrendTopics.getInstance();
		assertNotNull(tt);
		assertSame(tt, TrendTopics.getInstance());
	}

	/**
	 * Test method for {@link com.twitterapime.search.TrendTopics#searchNowTopics(com.twitterapime.search.Query)}.
	 */
	public void testSearchNowTopics() {
		TrendTopics tt = TrendTopics.getInstance();
		//
		try {
			Topic[] topics = tt.searchNowTopics(null);
			//
			assertNotNull(topics);
			assertTrue(topics.length > 0);
			//
			for (int i = 0; i < topics.length; i++) {
				assertNotNull(topics[i]);
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_DATE));
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_TEXT));
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_QUERY));
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_URL));
			}
			//
			topics = tt.searchNowTopics(QueryComposer.excludeHashtags());
			//
			assertNotNull(topics);
			assertTrue(topics.length > 0);
			//
			for (int i = 0; i < topics.length; i++) {
				assertNotNull(topics[i]);
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_DATE));
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_TEXT));
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_QUERY));
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_URL));
				assertEquals(-1, topics[i].getString(MetadataSet.TOPIC_TEXT).indexOf("#"));
			}
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.search.TrendTopics#searchDailyTopics(com.twitterapime.search.Query)}.
	 */
	public void testSearchDailyTopics() {
		TrendTopics tt = TrendTopics.getInstance();
		//
		try {
			Topic[] topics = tt.searchDailyTopics(null);
			//
			assertNotNull(topics);
			assertTrue(topics.length > 0);
			//
			for (int i = 0; i < topics.length; i++) {
				assertNotNull(topics[i]);
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_DATE));
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_TEXT));
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_QUERY));
			}
			//
			topics = tt.searchDailyTopics(QueryComposer.excludeHashtags());
			//
			assertNotNull(topics);
			assertTrue(topics.length > 0);
			//
			for (int i = 0; i < topics.length; i++) {
				assertNotNull(topics[i]);
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_DATE));
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_TEXT));
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_QUERY));
				assertEquals(-1, topics[i].getString(MetadataSet.TOPIC_TEXT).indexOf("#"));
			}
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.search.TrendTopics#searchWeeklyTopics(com.twitterapime.search.Query)}.
	 */
	public void testSearchWeeklyTopics() {
		TrendTopics tt = TrendTopics.getInstance();
		//
		try {
			Topic[] topics = tt.searchWeeklyTopics(null);
			//
			assertNotNull(topics);
			assertTrue(topics.length > 0);
			//
			for (int i = 0; i < topics.length; i++) {
				assertNotNull(topics[i]);
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_DATE));
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_TEXT));
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_QUERY));
			}
			//
			topics = tt.searchWeeklyTopics(QueryComposer.excludeHashtags());
			//
			assertNotNull(topics);
			assertTrue(topics.length > 0);
			//
			for (int i = 0; i < topics.length; i++) {
				assertNotNull(topics[i]);
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_DATE));
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_TEXT));
				assertNotNull(topics[i].getString(MetadataSet.TOPIC_QUERY));
				assertEquals(-1, topics[i].getString(MetadataSet.TOPIC_TEXT).indexOf("#"));
			}
		} catch (Exception e) {
			fail();
		}
	}

}
