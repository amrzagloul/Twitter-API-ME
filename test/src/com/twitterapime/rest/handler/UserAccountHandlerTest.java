/**
 * 
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.util.StringUtil;

/**
 * @author Main
 *
 */
public class UserAccountHandlerTest extends TestCase {
	/**
	 * 
	 */
	public UserAccountHandlerTest() {
		super("UserAccountHandlerTest");
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.handler.UserAccountHandler#populate(java.util.Hashtable, String, String)}.
	 */
	public void testPopulate() {
		Hashtable sampleRef = new Hashtable();
		sampleRef.put(MetadataSet.USERACCOUNT_CREATE_DATE, Long.toString(StringUtil.convertTweetDateToLong("Sat Nov 07 21:30:03 +0000 2009")));
		sampleRef.put(MetadataSet.USERACCOUNT_DESCRIPTION, "API for accessing Twitter's Service from mobile devices.");
		sampleRef.put(MetadataSet.USERACCOUNT_FAVOURITES_COUNT, "0");
		sampleRef.put(MetadataSet.USERACCOUNT_FOLLOWERS_COUNT, "1");
		sampleRef.put(MetadataSet.USERACCOUNT_FRIENDS_COUNT, "1");
		sampleRef.put(MetadataSet.USERACCOUNT_ID, "88275918");
		sampleRef.put(MetadataSet.USERACCOUNT_LOCATION, "Brazil");
		sampleRef.put(MetadataSet.USERACCOUNT_NAME, "Twiter API ME");
		sampleRef.put(MetadataSet.USERACCOUNT_NOTIFICATIONS, "false");
		sampleRef.put(MetadataSet.USERACCOUNT_PICTURE_URI, "http://s.twimg.com/a/1261519751/images/default_profile_3_normal.png");
		sampleRef.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_COLOR, "9ae4e8");
		sampleRef.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI, "http://s.twimg.com/a/1261519751/images/themes/theme1/bg.png");
		sampleRef.put(MetadataSet.USERACCOUNT_PROFILE_LINK_COLOR, "0000ff");
		sampleRef.put(MetadataSet.USERACCOUNT_PROFILE_TEXT_COLOR, "000000");
		sampleRef.put(MetadataSet.USERACCOUNT_PROTECTED, "false");
		sampleRef.put(MetadataSet.USERACCOUNT_TIME_ZONE, "Santiago");
		sampleRef.put(MetadataSet.USERACCOUNT_TWEETS_COUNT, "17");
		sampleRef.put(MetadataSet.USERACCOUNT_URL, "http://www.twitterapime.com");
		sampleRef.put(MetadataSet.USERACCOUNT_USER_NAME, "twiterapime");
		sampleRef.put(MetadataSet.USERACCOUNT_UTC_OFFSET, "-14400");
		sampleRef.put(MetadataSet.USERACCOUNT_VERIFIED, "false");
		//
		UserAccountHandler h = new UserAccountHandler();
		//
		Hashtable sampleTest = new Hashtable();
		h.populate(sampleTest, "/created_at", "Sat Nov 07 21:30:03 +0000 2009");
		h.populate(sampleTest, "/description", "API for accessing Twitter's Service from mobile devices.");
		h.populate(sampleTest, "/favourites_count", "0");
		h.populate(sampleTest, "/followers_count", "1");
		h.populate(sampleTest, "/friends_count", "1");
		h.populate(sampleTest, "/id", "88275918");
		h.populate(sampleTest, "/location", "Brazil");
		h.populate(sampleTest, "/name", "Twiter API ME");
		h.populate(sampleTest, "/notifications", "false");
		h.populate(sampleTest, "/profile_image_url", "http://s.twimg.com/a/1261519751/images/default_profile_3_normal.png");
		h.populate(sampleTest, "/profile_background_color", "9ae4e8");
		h.populate(sampleTest, "/profile_background_image_url", "http://s.twimg.com/a/1261519751/images/themes/theme1/bg.png");
		h.populate(sampleTest, "/profile_link_color", "0000ff");
		h.populate(sampleTest, "/profile_text_color", "000000");
		h.populate(sampleTest, "/protected", "false");
		h.populate(sampleTest, "/time_zone", "Santiago");
		h.populate(sampleTest, "/statuses_count", "17");
		h.populate(sampleTest, "/url", "http://www.twitterapime.com");
		h.populate(sampleTest, "/screen_name", "twiterapime");
		h.populate(sampleTest, "/utc_offset", "-14400");
		h.populate(sampleTest, "/verified", "false");
		//
		assertTrue(new UserAccount(sampleRef).equals(new UserAccount(sampleTest)));
	}
}