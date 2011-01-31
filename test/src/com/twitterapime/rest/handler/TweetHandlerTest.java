/**
 * 
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.Tweet;
import com.twitterapime.test.TwitterAPIMETestCase;
import com.twitterapime.util.StringUtil;

/**
 * @author Main
 *
 */
public class TweetHandlerTest extends TwitterAPIMETestCase {
	/**
	 * 
	 */
	public TweetHandlerTest() {
		super("TweetHandlerTest");
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.handler.TweetHandler#populate(java.util.Hashtable, String, String)}.
	 */
	public void testPopulate() {
		Hashtable sampleRef = new Hashtable();
		sampleRef.put(MetadataSet.TWEET_CONTENT, "Content");
		sampleRef.put(MetadataSet.TWEET_FAVOURITE, "true");
		sampleRef.put(MetadataSet.TWEET_ID, "1472669360");
		sampleRef.put(MetadataSet.TWEET_PUBLISH_DATE, Long.toString(StringUtil.convertTweetDateToLong("Tue Apr 07 22:52:51 +0000 2009")));
		sampleRef.put(MetadataSet.TWEET_SOURCE, "TwitterAPIME");
		//
		TweetHandler h = new TweetHandler();
		//
		Hashtable sampleTest = new Hashtable();
		h.populate(sampleTest, "/text", "Content");
		h.populate(sampleTest, "/favorited", "true");
		h.populate(sampleTest, "/id", "1472669360");
		h.populate(sampleTest, "/created_at", "Tue Apr 07 22:52:51 +0000 2009");
		h.populate(sampleTest, "/source", "TwitterAPIME");
		//
		assertTrue(new Tweet(sampleRef).equals(new Tweet(sampleTest)));
	}
}