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
 * 
 * @author ernandes
 *
 */
public class DirectMessageHandlerTest extends TestCase implements SearchDeviceListener {
	/**
	 * 
	 */
	private int tweetFoundCount;
	
	/**
	 * 
	 */
	public DirectMessageHandlerTest() {
		super("DirectMessageHandlerTest");
	}

	/**
	 * Test method for {@link com.twitterapime.rest.handler.DirectMessageHandler#getParsedTweets()}.
	 */
	public void testGetParsedTweets() {
		final int TWEETS_COUNT = 5;
		//
		InputStream errorXML = null;
		DirectMessageHandler handler = new DirectMessageHandler();
		handler.setSearchDeviceListener(this);
		//
		try {
			tweetFoundCount = 0;
			//
			errorXML = getClass().getResourceAsStream("/xml/twitterapi-direct-messages.xml");
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
			sample.put(MetadataSet.TWEET_ID, "id_" + id);
			sample.put(MetadataSet.TWEET_PUBLISH_DATE, Long.toString(StringUtil.convertTweetDateToLong("Sat Apr 10 16:45:26 +0000 2010")));
			//
			Hashtable userSample = new Hashtable();
			userSample.put(MetadataSet.USERACCOUNT_CREATE_DATE, Long.toString(StringUtil.convertTweetDateToLong("Sat Nov 07 21:30:03 +0000 2009")));
			userSample.put(MetadataSet.USERACCOUNT_DESCRIPTION, "sender_description_" + id);
			userSample.put(MetadataSet.USERACCOUNT_FAVOURITES_COUNT, "sender_favourites_count_" + id);
			userSample.put(MetadataSet.USERACCOUNT_FOLLOWERS_COUNT, "sender_followers_count_" + id);
			userSample.put(MetadataSet.USERACCOUNT_FRIENDS_COUNT, "sender_friends_count_" + id);
			userSample.put(MetadataSet.USERACCOUNT_ID, "sender_id_" + id);
			userSample.put(MetadataSet.USERACCOUNT_LOCATION, "sender_location_" + id);
			userSample.put(MetadataSet.USERACCOUNT_NAME, "sender_name_" + id);
			userSample.put(MetadataSet.USERACCOUNT_NOTIFICATIONS, "sender_notifications_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PICTURE_URI, "sender_profile_image_url_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_COLOR, "sender_profile_background_color_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI, "sender_profile_background_image_url_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROFILE_LINK_COLOR, "sender_profile_link_color_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROFILE_TEXT_COLOR, "sender_profile_text_color_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROTECTED, "sender_protected_" + id);
			userSample.put(MetadataSet.USERACCOUNT_TIME_ZONE, "sender_time_zone_" + id);
			userSample.put(MetadataSet.USERACCOUNT_TWEETS_COUNT, "sender_statuses_count_" + id);
			userSample.put(MetadataSet.USERACCOUNT_URL, "sender_url_" + id);
			userSample.put(MetadataSet.USERACCOUNT_USER_NAME, "sender_screen_name_" + id);
			userSample.put(MetadataSet.USERACCOUNT_UTC_OFFSET, "sender_utc_offset_" + id);
			userSample.put(MetadataSet.USERACCOUNT_VERIFIED, "sender_verified_" + id);
			//
			sample.put(MetadataSet.TWEET_USER_ACCOUNT, new UserAccount(userSample));
			//
			userSample = new Hashtable();
			userSample.put(MetadataSet.USERACCOUNT_CREATE_DATE, Long.toString(StringUtil.convertTweetDateToLong("Sat Nov 07 21:30:03 +0000 2009")));
			userSample.put(MetadataSet.USERACCOUNT_DESCRIPTION, "recipient_description_" + id);
			userSample.put(MetadataSet.USERACCOUNT_FAVOURITES_COUNT, "recipient_favourites_count_" + id);
			userSample.put(MetadataSet.USERACCOUNT_FOLLOWERS_COUNT, "recipient_followers_count_" + id);
			userSample.put(MetadataSet.USERACCOUNT_FRIENDS_COUNT, "recipient_friends_count_" + id);
			userSample.put(MetadataSet.USERACCOUNT_ID, "recipient_id_" + id);
			userSample.put(MetadataSet.USERACCOUNT_LOCATION, "recipient_location_" + id);
			userSample.put(MetadataSet.USERACCOUNT_NAME, "recipient_name_" + id);
			userSample.put(MetadataSet.USERACCOUNT_NOTIFICATIONS, "recipient_notifications_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PICTURE_URI, "recipient_profile_image_url_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_COLOR, "recipient_profile_background_color_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI, "recipient_profile_background_image_url_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROFILE_LINK_COLOR, "recipient_profile_link_color_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROFILE_TEXT_COLOR, "recipient_profile_text_color_" + id);
			userSample.put(MetadataSet.USERACCOUNT_PROTECTED, "recipient_protected_" + id);
			userSample.put(MetadataSet.USERACCOUNT_TIME_ZONE, "recipient_time_zone_" + id);
			userSample.put(MetadataSet.USERACCOUNT_TWEETS_COUNT, "recipient_statuses_count_" + id);
			userSample.put(MetadataSet.USERACCOUNT_URL, "recipient_url_" + id);
			userSample.put(MetadataSet.USERACCOUNT_USER_NAME, "recipient_screen_name_" + id);
			userSample.put(MetadataSet.USERACCOUNT_UTC_OFFSET, "recipient_utc_offset_" + id);
			userSample.put(MetadataSet.USERACCOUNT_VERIFIED, "recipient_verified_" + id);
			//
			sample.put(MetadataSet.TWEET_RECIPIENT_ACCOUNT, new UserAccount(userSample));
			//
			assertTrue(new Tweet(sample).equals(ts[i]));
		}
		//
		assertEquals(0, new DirectMessageHandler().getParsedTweets().length);
		//
		//*********************************************************************
		//
		handler = new DirectMessageHandler();
		handler.setSearchDeviceListener(this);
		//
		try {
			tweetFoundCount = 0;
			//
			errorXML = getClass().getResourceAsStream("/xml/twitterapi-direct-message.xml");
			ParserFactory.getDefaultParser().parse(errorXML, handler);
			//
			assertEquals(1, tweetFoundCount);
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
		ts = handler.getParsedTweets();
		assertEquals(1, ts.length);
		//
		Hashtable sample = new Hashtable();
		sample.put(MetadataSet.TWEET_CONTENT, "text_1");
		sample.put(MetadataSet.TWEET_ID, "id_1");
		sample.put(MetadataSet.TWEET_PUBLISH_DATE, Long.toString(StringUtil.convertTweetDateToLong("Sat Apr 10 16:45:26 +0000 2010")));
		//
		Hashtable userSample = new Hashtable();
		userSample.put(MetadataSet.USERACCOUNT_CREATE_DATE, Long.toString(StringUtil.convertTweetDateToLong("Sat Nov 07 21:30:03 +0000 2009")));
		userSample.put(MetadataSet.USERACCOUNT_DESCRIPTION, "sender_description_1");
		userSample.put(MetadataSet.USERACCOUNT_FAVOURITES_COUNT, "sender_favourites_count_1");
		userSample.put(MetadataSet.USERACCOUNT_FOLLOWERS_COUNT, "sender_followers_count_1");
		userSample.put(MetadataSet.USERACCOUNT_FRIENDS_COUNT, "sender_friends_count_1");
		userSample.put(MetadataSet.USERACCOUNT_ID, "sender_id_1");
		userSample.put(MetadataSet.USERACCOUNT_LOCATION, "sender_location_1");
		userSample.put(MetadataSet.USERACCOUNT_NAME, "sender_name_1");
		userSample.put(MetadataSet.USERACCOUNT_NOTIFICATIONS, "sender_notifications_1");
		userSample.put(MetadataSet.USERACCOUNT_PICTURE_URI, "sender_profile_image_url_1");
		userSample.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_COLOR, "sender_profile_background_color_1");
		userSample.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI, "sender_profile_background_image_url_1");
		userSample.put(MetadataSet.USERACCOUNT_PROFILE_LINK_COLOR, "sender_profile_link_color_1");
		userSample.put(MetadataSet.USERACCOUNT_PROFILE_TEXT_COLOR, "sender_profile_text_color_1");
		userSample.put(MetadataSet.USERACCOUNT_PROTECTED, "sender_protected_1");
		userSample.put(MetadataSet.USERACCOUNT_TIME_ZONE, "sender_time_zone_1");
		userSample.put(MetadataSet.USERACCOUNT_TWEETS_COUNT, "sender_statuses_count_1");
		userSample.put(MetadataSet.USERACCOUNT_URL, "sender_url_1");
		userSample.put(MetadataSet.USERACCOUNT_USER_NAME, "sender_screen_name_1");
		userSample.put(MetadataSet.USERACCOUNT_UTC_OFFSET, "sender_utc_offset_1");
		userSample.put(MetadataSet.USERACCOUNT_VERIFIED, "sender_verified_1");
		//
		sample.put(MetadataSet.TWEET_USER_ACCOUNT, new UserAccount(userSample));
		//
		userSample = new Hashtable();
		userSample.put(MetadataSet.USERACCOUNT_CREATE_DATE, Long.toString(StringUtil.convertTweetDateToLong("Sat Nov 07 21:30:03 +0000 2009")));
		userSample.put(MetadataSet.USERACCOUNT_DESCRIPTION, "recipient_description_1");
		userSample.put(MetadataSet.USERACCOUNT_FAVOURITES_COUNT, "recipient_favourites_count_1");
		userSample.put(MetadataSet.USERACCOUNT_FOLLOWERS_COUNT, "recipient_followers_count_1");
		userSample.put(MetadataSet.USERACCOUNT_FRIENDS_COUNT, "recipient_friends_count_1");
		userSample.put(MetadataSet.USERACCOUNT_ID, "recipient_id_1");
		userSample.put(MetadataSet.USERACCOUNT_LOCATION, "recipient_location_1");
		userSample.put(MetadataSet.USERACCOUNT_NAME, "recipient_name_1");
		userSample.put(MetadataSet.USERACCOUNT_NOTIFICATIONS, "recipient_notifications_1");
		userSample.put(MetadataSet.USERACCOUNT_PICTURE_URI, "recipient_profile_image_url_1");
		userSample.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_COLOR, "recipient_profile_background_color_1");
		userSample.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI, "recipient_profile_background_image_url_1");
		userSample.put(MetadataSet.USERACCOUNT_PROFILE_LINK_COLOR, "recipient_profile_link_color_1");
		userSample.put(MetadataSet.USERACCOUNT_PROFILE_TEXT_COLOR, "recipient_profile_text_color_1");
		userSample.put(MetadataSet.USERACCOUNT_PROTECTED, "recipient_protected_1");
		userSample.put(MetadataSet.USERACCOUNT_TIME_ZONE, "recipient_time_zone_1");
		userSample.put(MetadataSet.USERACCOUNT_TWEETS_COUNT, "recipient_statuses_count_1");
		userSample.put(MetadataSet.USERACCOUNT_URL, "recipient_url_1");
		userSample.put(MetadataSet.USERACCOUNT_USER_NAME, "recipient_screen_name_1");
		userSample.put(MetadataSet.USERACCOUNT_UTC_OFFSET, "recipient_utc_offset_1");
		userSample.put(MetadataSet.USERACCOUNT_VERIFIED, "recipient_verified_1");
		//
		sample.put(MetadataSet.TWEET_RECIPIENT_ACCOUNT, new UserAccount(userSample));
		//
		assertTrue(new Tweet(sample).equals(ts[0]));
	}

	/**
	 * Test method for {@link com.twitterapime.rest.handler.DirectMessageHandler#loadParsedTweet(com.twitterapime.search.Tweet, int)}.
	 */
	public void testLoadParsedTweet() {
		InputStream errorXML = null;
		DirectMessageHandler handler = new DirectMessageHandler();
		handler.setSearchDeviceListener(this);
		//
		try {
			errorXML = getClass().getResourceAsStream("/xml/twitterapi-direct-message.xml");
			ParserFactory.getDefaultParser().parse(errorXML, handler);
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
		Tweet t = new Tweet();
		handler.loadParsedTweet(t, 0);
		assertTrue(t.size() > 0);
		assertTrue(t.equals(handler.getParsedTweets()[0]));
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