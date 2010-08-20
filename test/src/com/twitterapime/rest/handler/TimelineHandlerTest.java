/**
 * 
 */
package com.twitterapime.rest.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;
import com.twitterapime.util.StringUtil;

/**
 * @author ernandes
 *
 */
public class TimelineHandlerTest extends TestCase implements SearchDeviceListener {
	/**
	 * 
	 */
	private int tweetFoundCount;
	
	/**
	 * 
	 */
	public TimelineHandlerTest() {
		super("TimelineHandlerTest");
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.handler.TimelineHandler#getParsedTweets()}.
	 */
	public void testGetParsedTweets() {
		final int TWEETS_COUNT = 5;
		//
		InputStream errorXML = null;
		TimelineHandler handler = new TimelineHandler();
		handler.setSearchDeviceListener(this);
		//
		try {
			errorXML = getClass().getResourceAsStream("/xml/twitterapi-timeline.xml");
			ParserFactory.getDefaultParser().parse(errorXML, handler);
			//
			assertEquals(TWEETS_COUNT, tweetFoundCount);
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
		assertEquals(TWEETS_COUNT, ts.length);
		//
		for (int i = 0; i < ts.length; i++) {
			final String id = Integer.toString(i +1);
			//
			Hashtable sample = new Hashtable();
			sample.put(MetadataSet.TWEET_CONTENT, "text_" + id);
			sample.put(MetadataSet.TWEET_FAVOURITE, "false");
			sample.put(MetadataSet.TWEET_ID, "id_" + id);
			sample.put(MetadataSet.TWEET_PUBLISH_DATE, Long.toString(StringUtil.convertTweetDateToLong("Fri Apr 16 00:52:32 +0000 2010")));
			sample.put(MetadataSet.TWEET_SOURCE, "source_" + id);
			//
			Hashtable userSample = new Hashtable();
			userSample.put(MetadataSet.USERACCOUNT_CREATE_DATE, Long.toString(StringUtil.convertTweetDateToLong("Sat May 09 18:57:42 +0000 2009")));
			userSample.put(MetadataSet.USERACCOUNT_DESCRIPTION, "description_" + id);
			userSample.put(MetadataSet.USERACCOUNT_FAVOURITES_COUNT, "favourites_count_" + id);
			userSample.put(MetadataSet.USERACCOUNT_FOLLOWERS_COUNT, "followers_count_" + id);
			userSample.put(MetadataSet.USERACCOUNT_FRIENDS_COUNT, "friends_count_" + id);
			userSample.put(MetadataSet.USERACCOUNT_ID, "userid_" + id);
			userSample.put(MetadataSet.USERACCOUNT_LOCATION, "location_" + id);
			userSample.put(MetadataSet.USERACCOUNT_NAME, "name_" + id);
			userSample.put(MetadataSet.USERACCOUNT_NOTIFICATIONS, "false");
			userSample.put(MetadataSet.USERACCOUNT_PICTURE_URI, "profile_image_url_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_COLOR, "bgcolor_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI, "bgimage_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROFILE_LINK_COLOR, "linkcolor_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROFILE_TEXT_COLOR, "textcolor_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROTECTED, "false");
			userSample.put(MetadataSet.USERACCOUNT_TIME_ZONE, "time_zone_" + id);
			userSample.put(MetadataSet.USERACCOUNT_TWEETS_COUNT, "statuses_count_" + id);
			userSample.put(MetadataSet.USERACCOUNT_URL, "url_" + id);
			userSample.put(MetadataSet.USERACCOUNT_USER_NAME, "screen_name_" + id);
			userSample.put(MetadataSet.USERACCOUNT_UTC_OFFSET, "offset_" + id);
			userSample.put(MetadataSet.USERACCOUNT_VERIFIED, "false");
			userSample.put(MetadataSet.USERACCOUNT_GEO_ENABLED, "false");
			//
			sample.put(MetadataSet.TWEET_USER_ACCOUNT, new UserAccount(userSample));
			//
			assertTrue(new Tweet(sample).equals(ts[i]));
		}
		//
		assertEquals(0, new TimelineHandler().getParsedTweets().length);
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