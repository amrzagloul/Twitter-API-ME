/**
 * 
 */
package com.twitterapime.search.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;

/**
 * @author Main
 *
 */
public class SearchResultHandlerTest extends TestCase implements SearchDeviceListener {
	/**
	 * 
	 */
	private int tweetFoundCount;

	/**
	 * 
	 */
	public SearchResultHandlerTest() {
		super("SearchResultHandlerTest");
	}

	/**
	 * Test method for {@link com.twitterapime.search.handler.SearchResultHandler#getParsedTweets()}.
	 */
	public void testGetParsedTweets() {
		InputStream errorXML = null;
		SearchResultHandler handler = new SearchResultHandler();
		handler.setSearchDeviceListener(this);
		//
		try {
			errorXML = getClass().getResourceAsStream("/xml/twitterapi-search-result.xml");
			ParserFactory.getDefaultParser().parse(errorXML, handler);
			//
			assertEquals(10, tweetFoundCount);
		} catch (Exception e) {
			fail();
		} finally {
			if (errorXML != null) {
				try {
					errorXML.close();
				} catch (IOException e) {
				}
			}
		}
		//
		Tweet[] ts = handler.getParsedTweets();
		assertEquals(10, ts.length);
		//
		Hashtable sample = new Hashtable();
		sample.put(MetadataSet.TWEET_AUTHOR_NAME, "Twitter API ME");
		sample.put(MetadataSet.TWEET_AUTHOR_USERNAME, "twapime");
		sample.put(MetadataSet.TWEET_AUTHOR_URI, "http://twitter.com/twapime");
		sample.put(MetadataSet.TWEET_LANG, "en");
		sample.put(MetadataSet.TWEET_SOURCE, "TweetDeck");
		sample.put(MetadataSet.TWEET_AUTHOR_PICTURE_URI, "http://a3.twimg.com/profile_images/45684621/pic_normal.JPG");
		sample.put(MetadataSet.TWEET_PUBLISH_DATE, "1259630700000");
		//
		for (int i = 0; i < ts.length; i++) {
			sample.put(MetadataSet.TWEET_ID, Integer.toString(i +1));
			sample.put(MetadataSet.TWEET_CONTENT, "Tweet message " + Integer.toString(i +1));
			sample.put(MetadataSet.TWEET_URI, "http://twitter.com/twapime/statuses/" + Integer.toString(i +1));
			//
			assertTrue(new Tweet(sample).equals(ts[i]));
		}
		//
		assertEquals(0, new SearchResultHandler().getParsedTweets().length);
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#searchCompleted()
	 */
	public void searchCompleted() {
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#searchFailed(java.lang.Throwable)
	 */
	public void searchFailed(Throwable cause) {
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#tweetFound(com.twitterapime.search.Tweet)
	 */
	public void tweetFound(Tweet tweet) {
		tweetFoundCount++;
	}
}