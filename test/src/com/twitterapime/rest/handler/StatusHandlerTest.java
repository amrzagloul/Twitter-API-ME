/**
 * 
 */
package com.twitterapime.rest.handler;

import java.io.InputStream;
import java.util.Hashtable;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.Tweet;
import com.twitterapime.util.StringUtil;

/**
 * @author Main
 *
 */
public class StatusHandlerTest extends TestCase {
	/**
	 * 
	 */
	private StatusHandler handler;

	/**
	 * 
	 */
	public StatusHandlerTest() {
		super("StatusHandlerTest");
	}
	
	/**
	 * @see com.sonyericsson.junit.framework.TestCase#setUp()
	 */
	public void setUp() throws Throwable {
		InputStream errorXML = null;
		handler = new StatusHandler();
		//
		try {
			errorXML = getClass().getResourceAsStream("/xml/twitterapi-tweet-response.xml");
			ParserFactory.getDefaultParser().parse(errorXML, handler);
		} catch (Exception e) {
			fail();
		} finally {
			if (errorXML != null) {
				errorXML.close();
			}
		}
	}

	/**
	 * Test method for {@link com.com.twitterapime.rest.handler.StatusHandler#getParsedTweet()}.
	 */
	public void testGetParsedTweet() {
		Hashtable sample = new Hashtable();
		sample.put(MetadataSet.TWEET_CONTENT, "Hello, everybody!!! How are you guys doing?");
		sample.put(MetadataSet.TWEET_FAVOURITE, "false");
		sample.put(MetadataSet.TWEET_ID, "1472669360");
		sample.put(MetadataSet.TWEET_PUBLISH_DATE, Long.toString(StringUtil.convertTweetDateToLong("Tue Apr 07 22:52:51 +0000 2009")));
		sample.put(MetadataSet.TWEET_SOURCE, "TweetDeck");
		Hashtable userSample = new Hashtable();
		userSample.put(MetadataSet.USERACCOUNT_CREATE_DATE, Long.toString(StringUtil.convertTweetDateToLong("Sun Mar 18 06:42:26 +0000 2007")));
		userSample.put(MetadataSet.USERACCOUNT_DESCRIPTION, "Twitter API for mobile devices.");
		userSample.put(MetadataSet.USERACCOUNT_FAVOURITES_COUNT, "0");
		userSample.put(MetadataSet.USERACCOUNT_FOLLOWERS_COUNT, "1027");
		userSample.put(MetadataSet.USERACCOUNT_FRIENDS_COUNT, "293");
		userSample.put(MetadataSet.USERACCOUNT_ID, "1401881");
		userSample.put(MetadataSet.USERACCOUNT_LOCATION, "San Francisco, CA");
		userSample.put(MetadataSet.USERACCOUNT_NAME, "Twitter API ME");
		userSample.put(MetadataSet.USERACCOUNT_NOTIFICATIONS, "false");
		userSample.put(MetadataSet.USERACCOUNT_PICTURE_URI, "http://s3.amazonaws.com/twitter_production/profile_images/59648642/avatar_normal.png");
		userSample.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_COLOR, "9ae4e8");
		userSample.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI, "http://s3.amazonaws.com/twitter_production/profile_background_images/2752608/twitter_bg_grass.jpg");
		userSample.put(MetadataSet.USERACCOUNT_PROFILE_LINK_COLOR, "0000ff");
		userSample.put(MetadataSet.USERACCOUNT_PROFILE_TEXT_COLOR, "000000");
		userSample.put(MetadataSet.USERACCOUNT_PROTECTED, "false");
		userSample.put(MetadataSet.USERACCOUNT_TIME_ZONE, "Eastern Time (US and Canada)");
		userSample.put(MetadataSet.USERACCOUNT_TWEETS_COUNT, "3390");
		userSample.put(MetadataSet.USERACCOUNT_URL, "http://www.twitterapime.com");
		userSample.put(MetadataSet.USERACCOUNT_USER_NAME, "twiterapime");
		userSample.put(MetadataSet.USERACCOUNT_UTC_OFFSET, "-18000");
		userSample.put(MetadataSet.USERACCOUNT_VERIFIED, "true");
		UserAccount ua = new UserAccount(userSample);
		sample.put(MetadataSet.TWEET_USER_ACCOUNT, ua);
		//
		assertEquals(new Tweet(sample), handler.getParsedTweet());
		assertEquals(0, new StatusHandler().getParsedTweet().size());
	}

	/**
	 * Test method for {@link com.com.twitterapime.rest.handler.StatusHandler#loadParsedTweet(com.twitterapime.search.Tweet)}.
	 */
	public void testLoadParsedTweet() {
		Tweet tweet = new Tweet();
		handler.loadParsedTweet(tweet);
		assertEquals(tweet, handler.getParsedTweet());
	}
}