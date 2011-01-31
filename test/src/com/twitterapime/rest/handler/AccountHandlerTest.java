/**
 * 
 */
package com.twitterapime.rest.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.Tweet;
import com.twitterapime.test.TwitterAPIMETestCase;
import com.twitterapime.util.StringUtil;

/**
 * @author Main
 *
 */
public class AccountHandlerTest extends TwitterAPIMETestCase {
	/**
	 * 
	 */
	public AccountHandlerTest() {
		super("AccountHandlerTest");
	}

	/**
	 * Test method for {@link com.twitterapime.rest.handler.UserAccountHandler#getParsedUserAccount()}.
	 */
	public void testGetParsedUserAccount() {
		InputStream errorXML = null;
		AccountHandler handler = new AccountHandler();
		//
		try {
			errorXML = getClass().getResourceAsStream("/xml/twitterapi-user-account.xml");
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
		Hashtable sample = new Hashtable();
		sample.put(MetadataSet.USERACCOUNT_CREATE_DATE, Long.toString(StringUtil.convertTweetDateToLong("Sat Nov 07 21:30:03 +0000 2009")));
		sample.put(MetadataSet.USERACCOUNT_DESCRIPTION, "API for accessing Twitter's Service from mobile devices.");
		sample.put(MetadataSet.USERACCOUNT_FAVOURITES_COUNT, "0");
		sample.put(MetadataSet.USERACCOUNT_FOLLOWERS_COUNT, "1");
		sample.put(MetadataSet.USERACCOUNT_FRIENDS_COUNT, "1");
		sample.put(MetadataSet.USERACCOUNT_ID, "88275918");
		sample.put(MetadataSet.USERACCOUNT_LOCATION, "Brazil");
		sample.put(MetadataSet.USERACCOUNT_NAME, "Twiter API ME");
		sample.put(MetadataSet.USERACCOUNT_NOTIFICATIONS, "false");
		sample.put(MetadataSet.USERACCOUNT_PICTURE_URI, "http://s.twimg.com/a/1261519751/images/default_profile_3_normal.png");
		sample.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_COLOR, "9ae4e8");
		sample.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI, "http://s.twimg.com/a/1261519751/images/themes/theme1/bg.png");
		sample.put(MetadataSet.USERACCOUNT_PROFILE_LINK_COLOR, "0000ff");
		sample.put(MetadataSet.USERACCOUNT_PROFILE_TEXT_COLOR, "000000");
		sample.put(MetadataSet.USERACCOUNT_PROTECTED, "false");
		sample.put(MetadataSet.USERACCOUNT_TIME_ZONE, "Santiago");
		sample.put(MetadataSet.USERACCOUNT_TWEETS_COUNT, "17");
		sample.put(MetadataSet.USERACCOUNT_URL, "http://www.twitterapime.com");
		sample.put(MetadataSet.USERACCOUNT_USER_NAME, "twapime");
		sample.put(MetadataSet.USERACCOUNT_UTC_OFFSET, "-14400");
		sample.put(MetadataSet.USERACCOUNT_VERIFIED, "false");
		sample.put(MetadataSet.USERACCOUNT_GEO_ENABLED, "false");
		Tweet tweet = new Tweet();
		Hashtable tweetSample = new Hashtable();
		tweetSample.put(MetadataSet.TWEET_ID, "6714723469");
		tweetSample.put(MetadataSet.TWEET_PUBLISH_DATE, Long.toString(StringUtil.convertTweetDateToLong("Wed Dec 16 01:17:06 +0000 2009")));
		tweetSample.put(MetadataSet.TWEET_CONTENT, "Hello, everybody!!!");
		tweetSample.put(MetadataSet.TWEET_SOURCE, "API");
		tweetSample.put(MetadataSet.TWEET_FAVOURITE, "false");
		tweet.setData(tweetSample);
		sample.put(MetadataSet.USERACCOUNT_LAST_TWEET, tweet);
		//
		assertEquals(new UserAccount(sample), handler.getParsedUserAccounts()[0]);
		assertEquals(0, new AccountHandler().getParsedUserAccounts().length);
	}
}