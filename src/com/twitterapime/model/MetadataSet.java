/*
 * MetadataSet.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.model;

/**
 * <p>
 * MetadataSet is a interface that specifies a collection of metadata
 * attributes applicable to the entities that implement the interface Entity.
 * The prefix of each constant indicates which class that constant belongs to.
 * For instance, "TWEET_XXX" belongs to Tweet class.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.6
 * @since 1.0
 */
public final class MetadataSet {
	/**
	 * <p>
	 * Report the tweet's ID, e.g., "9637830025".
	 * </p>
	 */
	public static final String TWEET_ID = "TWEET_ID";

	/**
	 * <p>
	 * Report the tweet's URI, e.g.,
	 * "http://twitter.com/twitteruser/statuses/7895462131".
	 * </p>
	 */
	public static final String TWEET_URI = "TWEET_URI";

	/**
	 * <p>
	 * Report the tweet's content, e.g., "Hello Twitter!!! How are you?".
	 * </p>
	 */
	public static final String TWEET_CONTENT = "TWEET_CONTENT";

	/**
	 * <p>
	 * Report the tweet's publish date (long format), e.g., "165498132546".
	 * </p>
	 */
	public static final String TWEET_PUBLISH_DATE = "TWEET_PUBLISH_DATE";

	/**
	 * <p>
	 * Report the tweet's source (application), e.g., "TweetApp".
	 * </p>
	 */
	public static final String TWEET_SOURCE = "TWEET_SOURCE";

	/**
	 * <p>
	 * Report the tweet's language code, e.g., "en".
	 * </p>
	 */
	public static final String TWEET_LANG = "TWEET_LANG";

	/**
	 * <p>
	 * Report the tweet's author's name, e.g., "John Smith".
	 * </p>
	 */
	public static final String TWEET_AUTHOR_NAME = "TWEET_AUTHOR_NAME";

	/**
	 * <p>
	 * Report the tweet's author's username, e.g., "johnsmith".
	 * </p>
	 */
	public static final String TWEET_AUTHOR_USERNAME = "TWEET_AUTHOR_USERNAME";

	/**
	 * <p>
	 * Report the tweet's author's URI, e.g., "http://twitter.com/twitteruser".
	 * </p>
	 */
	public static final String TWEET_AUTHOR_URI = "TWEET_AUTHOR_URI";

	/**
	 * <p>
	 * Report the tweet's author's picture URI, e.g.,
	 * "http://a3.twimg.com/profile_images/8978974/pic_normal.JPG".
	 * </p>
	 */
	public static final String TWEET_AUTHOR_PICTURE_URI =
		"TWEET_AUTHOR_PICTURE_URI";

	/**
	 * <p>
	 * Report whether the tweet is marked as favourite, e.g., "true" or
	 * "false".
	 * </p>
	 */
	public static final String TWEET_FAVOURITE = "TWEET_FAVOURITE";

	/**
	 * <p>
	 * Report the user account object of tweet's sender, e.g., UserAccount
	 * class.
	 * </p>
	 */
	public static final String TWEET_USER_ACCOUNT = "TWEET_USER_ACCOUNT";

	/**
	 * <p>
	 * Report the user account object of Direct Message's recipient, e.g.,
	 * UserAccount class.
	 * </p>
	 */
	public static final String TWEET_RECIPIENT_ACCOUNT =
		"TWEET_RECIPIENT_ACCOUNT";

	/**
	 * <p>
	 * Report the reposted tweet object from a repost operation, e.g.,
	 * Tweet class.
	 * </p>
	 */
	public static final String TWEET_REPOSTED_TWEET = "TWEET_REPOSTED_TWEET";
	
	/**
	 * <p>
	 * Report the tweet's location object, e.g., GeoLocation class.
	 * </p>
	 */
	public static final String TWEET_LOCATION = "TWEET_LOCATION";

	/**
	 * <p>
	 * Report the tweet's entity object, e.g., TweetEntity class.
	 * </p>
	 */
	public static final String TWEET_ENTITY = "TWEET_ENTITY";

	/**
	 * <p>
	 * Report the tweet's replied tweet's id, e.g., "83721987832".
	 * </p>
	 */
	public static final String TWEET_IN_REPLY_TO_TWEET_ID =
		"TWEET_IN_REPLY_TO_TWEET_ID";

	/**
	 * <p>
	 * Report the credential's username, e.g., "johnsmith".
	 * </p>
	 */
	public static final String CREDENTIAL_USERNAME = "CREDENTIAL_USERNAME";

	/**
	 * <p>
	 * Report the credential's password, e.g., "j0hnsm1th".
	 * </p>
	 */
	public static final String CREDENTIAL_PASSWORD = "CREDENTIAL_PASSWORD";

	/**
	 * <p>
	 * Report the credential's consumer key, e.g., "jhdas78asjdhajsdas87d8adj".
	 * </p>
	 */
	public static final String CREDENTIAL_CONSUMER_KEY =
		"CREDENTIAL_CONSUMER_KEY";

	/**
	 * <p>
	 * Report the credential's consumer secret, e.g., "fiuwnmlfd89d23hspmghuyf".
	 * </p>
	 */
	public static final String CREDENTIAL_CONSUMER_SECRET =
		"CREDENTIAL_CONSUMER_SECRET";

	/**
	 * <p>
	 * Report the credential's access token, e.g.,
	 * "oauth_token=fduye8fd&oauth_token_secret=897fdjhf7d7h".
	 * </p>
	 */
	public static final String CREDENTIAL_ACCESS_TOKEN =
		"CREDENTIAL_ACCESS_TOKEN";

	/**
	 * <p>
	 * Report the remaining number of hits to Twitter REST API, e.g., "140".
	 * </p>
	 */
	public static final String RATELIMITSTATUS_REMAINING_HITS =
		"RATELIMITSTATUS_REMAINING_HITS";

	/**
	 * <p>
	 * Report the hourly limit number of hits to Twitter REST API, e.g., "150".
	 * </p>
	 */
	public static final String RATELIMITSTATUS_HOURLY_LIMIT =
		"RATELIMITSTATUS_HOURLY_LIMIT";

	/**
	 * <p>
	 * Report the time at which the number of hits to Twitter REST API will be
	 * reseted, e.g., "1654897954646".
	 * </p>
	 */
	public static final String RATELIMITSTATUS_RESET_TIME =
		"RATELIMITSTATUS_RESET_TIME";
	
	/**
	 * <p>
	 * Report the user account's ID, e.g., "489613465".
	 * </p>
	 */
	public static final String USERACCOUNT_ID = "USERACCOUNT_ID";
	
	/**
	 * <p>
	 * Report the user account's name, e.g., "John Smith".
	 * </p>
	 */
	public static final String USERACCOUNT_NAME = "USERACCOUNT_NAME";
	
	/**
	 * <p>
	 * Report the user account's username, e.g., "johnsmith".
	 * </p>
	 */
	public static final String USERACCOUNT_USER_NAME =
		"USERACCOUNT_USER_NAME";
	
	/**
	 * <p>
	 * Report the user account's location, e.g., "San Francisco".
	 * </p>
	 */
	public static final String USERACCOUNT_LOCATION = "USERACCOUNT_LOCATION";
	
	/**
	 * <p>
	 * Report the user account's description, e.g., "It's my personal account.".
	 * </p>
	 */
	public static final String USERACCOUNT_DESCRIPTION =
		"USERACCOUNT_DESCRIPTION";
	
	/**
	 * <p>
	 * Report the user account's picture URI, e.g.,
	 * "http://a3.twimg.com/profile_images/8978974/pic_normal.JPG".
	 * </p>
	 */
	public static final String USERACCOUNT_PICTURE_URI =
		"USERACCOUNT_PICTURE_URI";
	
	/**
	 * <p>
	 * Report the user account's URL, e.g., "http://www.johnsmith.com".
	 * </p>
	 */
	public static final String USERACCOUNT_URL = "USERACCOUNT_URL";
	
	/**
	 * <p>
	 * Report whether the user account is protected, e.g., "true" or "false".
	 * </p>
	 */
	public static final String USERACCOUNT_PROTECTED =
		"USERACCOUNT_PROTECTED";
	
	/**
	 * <p>
	 * Report the user account's followers count, e.g., "10".
	 * </p>
	 */
	public static final String USERACCOUNT_FOLLOWERS_COUNT =
		"USERACCOUNT_FOLLOWERS_COUNT";
	
	/**
	 * <p>
	 * Report the user account's profile background color, e.g., "1A1B1F".
	 * </p>
	 */
	public static final String USERACCOUNT_PROFILE_BACKGROUND_COLOR =
		"USERACCOUNT_PROFILE_BACKGROUND_COLOR";
	
	/**
	 * <p>
	 * Report the user account's profile text color, e.g., "666666".
	 * </p>
	 */
	public static final String USERACCOUNT_PROFILE_TEXT_COLOR =
		"USERACCOUNT_PROFILE_TEXT_COLOR";
	
	/**
	 * <p>
	 * Report the user account's profile link color, e.g., "2FC2EF".
	 * </p>
	 */
	public static final String USERACCOUNT_PROFILE_LINK_COLOR =
		"USERACCOUNT_PROFILE_LINK_COLOR";
	
	/**
	 * <p>
	 * Report the user account's friends count, e.g., "26".
	 * </p>
	 */
	public static final String USERACCOUNT_FRIENDS_COUNT =
		"USERACCOUNT_FRIENDS_COUNT";
	
	/**
	 * <p>
	 * Report the user account's create date (long format), e.g., "16549813254".
	 * </p>
	 */
	public static final String USERACCOUNT_CREATE_DATE =
		"USERACCOUNT_CREATE_DATE";
	
	/**
	 * <p>
	 * Report the user account's favourites count, e.g., "16".
	 * </p>
	 */
	public static final String USERACCOUNT_FAVOURITES_COUNT =
		"USERACCOUNT_FAVOURITES_COUNT";
	
	/**
	 * <p>
	 * Report the user account's UCT offset, e.g., "-10800".
	 * </p>
	 */
	public static final String USERACCOUNT_UTC_OFFSET =
		"USERACCOUNT_UTC_OFFSET";
	
	/**
	 * <p>
	 * Report the user account's time zone, e.g., "California".
	 * </p>
	 */
	public static final String USERACCOUNT_TIME_ZONE =
		"USERACCOUNT_TIME_ZONE";
	
	/**
	 * <p>
	 * Report the user account's profile background image URI, e.g.,
	 * "http://s.twimg.com/a/1259882278/images/themes/theme9/bg.gif".
	 * </p>
	 */
	public static final String USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI =
		"USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI";
	
	/**
	 * <p>
	 * Report the user account's tweets count, e.g., "159".
	 * </p>
	 */
	public static final String USERACCOUNT_TWEETS_COUNT =
		"USERACCOUNT_TWEETS_COUNT";
	
	/**
	 * <p>
	 * Report whether the user account receives notifications, e.g., "true" or
	 * "false".
	 * </p>
	 */
	public static final String USERACCOUNT_NOTIFICATIONS =
		"USERACCOUNT_NOTIFICATIONS";
	
	/**
	 * <p>
	 * Report whether the user account is verified, e.g., "true" or "false".
	 * </p>
	 */
	public static final String USERACCOUNT_VERIFIED = "USERACCOUNT_VERIFIED";
	
	/**
	 * <p>
	 * Report whether tweet location is enabled in user profile, e.g., "true" or
	 * "false".
	 * </p>
	 */
	public static final String USERACCOUNT_GEO_ENABLED =
		"USERACCOUNT_GEO_ENABLED";

	/**
	 * <p>
	 * Report the user account's last tweet object, e.g., Tweet class.
	 * </p>
	 */
	public static final String USERACCOUNT_LAST_TWEET =
		"USERACCOUNT_LAST_TWEET";

	/**
	 * <p>
	 * Reports the geo location latitude, e.g., "+50".
	 * </p>
	 */
	public static final String GEOLOCATION_LATITUDE = "GEOLOCATION_LATITUDE";

	/**
	 * <p>
	 * Reports the geo location longitude, e.g., "-10".
	 * </p>
	 */
	public static final String GEOLOCATION_LONGITUDE = "GEOLOCATION_LONGITUDE";

	/**
	 * <p>
	 * Reports the geo location place id, e.g., "87d6s767dsd".
	 * </p>
	 */
	public static final String GEOLOCATION_PLACE_ID = "GEOLOCATION_PLACE_ID";

	/**
	 * <p>
	 * Reports the geo location place name, e.g., "San Francisco".
	 * </p>
	 */
	public static final String GEOLOCATION_PLACE_NAME =
		"GEOLOCATION_PLACE_NAME";

	/**
	 * <p>
	 * Reports the geo location full name, e.g., "San Francisco, CA".
	 * </p>
	 */
	public static final String GEOLOCATION_PLACE_FULL_NAME =
		"GEOLOCATION_PLACE_FULL_NAME";

	/**
	 * <p>
	 * Reports the geo location place type, e.g., "city".
	 * </p>
	 */
	public static final String GEOLOCATION_PLACE_TYPE =
		"GEOLOCATION_PLACE_TYPE";
	
	/**
	 * <p>
	 * Reports the geo location url, e.g.,
	 * "http://api.twitter.com/1/geo/id/3c9e627dd6b55d9e.json".
	 * </p>
	 */
	public static final String GEOLOCATION_PLACE_URL = "GEOLOCATION_PLACE_URL";

	/**
	 * <p>
	 * Reports the geo location country, e.g., "United States".
	 * </p>
	 */
	public static final String GEOLOCATION_COUNTRY = "GEOLOCATION_COUNTRY";

	/**
	 * <p>
	 * Reports the geo location polygon, e.g., {"123", "456", "789", "0"}.
	 * </p>
	 */
	public static final String GEOLOCATION_POLYGON = "GEOLOCATION_POLYGON";
	
	/**
	 * <p>
	 * Reports the tweet's entity hashtags, e.g., Array of TweetEntity.
	 * </p>
	 */
	public static final String TWEETENTITY_HASHTAGS = "TWEETENTITY_HASHTAGS";
	
	/**
	 * <p>
	 * Reports the tweet's entity URLs, e.g., Array of TweetEntity.
	 * </p>
	 */
	public static final String TWEETENTITY_URLS = "TWEETENTITY_URLS";
	
	/**
	 * <p>
	 * Reports the tweet's entity mentions, e.g., Array of TweetEntity.
	 * </p>
	 */
	public static final String TWEETENTITY_MENTIONS = "TWEETENTITY_MENTIONS";

	/**
	 * <p>
	 * Reports a tweet's entity url, e.g., "http://www.twapime.com".
	 * </p>
	 */
	public static final String TWEETENTITY_URL = "TWEETENTITY_URL";
	
	/**
	 * <p>
	 * Reports a tweet's entity hashtag, e.g., "#twapime".
	 * </p>
	 */
	public static final String TWEETENTITY_HASHTAG = "TWEETENTITY_HASHTAG";
	
	/**
	 * <p>
	 * Reports a tweet's entity user id, e.g., "876547484".
	 * </p>
	 */
	public static final String TWEETENTITY_USERACCOUNT_ID =
		"TWEETENTITY_USERACCOUNT_ID";
	
	/**
	 * <p>
	 * Reports a tweet's entity user's name, e.g., "John Smith".
	 * </p>
	 */
	public static final String TWEETENTITY_USERACCOUNT_NAME =
		"TWEETENTITY_USERACCOUNT_NAME";
	
	/**
	 * <p>
	 * Reports a tweet's entity username, e.g., "johnsmith".
	 * </p>
	 */
	public static final String TWEETENTITY_USERACCOUNT_USER_NAME =
		"TWEETENTITY_USERACCOUNT_USER_NAME";

	/**
	 * <p>
	 * Reports a topic's text, e.g., "#nowplaying".
	 * </p>
	 */
	public static final String TOPIC_TEXT =	"TOPIC_TEXT";
	
	/**
	 * <p>
	 * Reports a topic's query, e.g., "#nowplaying".
	 * </p>
	 */
	public static final String TOPIC_QUERY = "TOPIC_QUERY";

	/**
	 * <p>
	 * Reports a topic's date, e.g., "14649876532156".
	 * </p>
	 */
	public static final String TOPIC_DATE =	"TOPIC_DATE";
	
	/**
	 * <p>
	 * Report the user account object of a list, e.g., UserAccount
	 * class.
	 * </p>
	 */
	public static final String LIST_USER_ACCOUNT = "LIST_USER_ACCOUNT";
	
	/**
	 * <p>
	 * Report the list's ID, e.g., "9637830025".
	 * </p>
	 */
	public static final String LIST_ID = "LIST_ID";
	
	/**
	 * <p>
	 * Report the list's name, e.g., "Java IT".
	 * </p>
	 */
	public static final String LIST_NAME = "LIST_NAME";
	
	/**
	 * <p>
	 * Report the list's full name, e.g., "@username/javait".
	 * </p>
	 */
	public static final String LIST_FULL_NAME = "LIST_FULL_NAME";
	
	/**
	 * <p>
	 * Report the list's slug, e.g., "javait".
	 * </p>
	 */
	public static final String LIST_SLUG = "LIST_SLUG";
	
	/**
	 * <p>
	 * Report the list's description, e.g., "This lists is about...".
	 * </p>
	 */
	public static final String LIST_DESCRIPTION = "LIST_DESCRIPTION";
	
	/**
	 * <p>
	 * Report the list's subscriber account, e.g., "10".
	 * </p>
	 */
	public static final String LIST_SUBSCRIBER_COUNT = "LIST_SUBSCRIBER_COUNT";

	/**
	 * <p>
	 * Report the list's member account, e.g., "7".
	 * </p>
	 */
	public static final String LIST_MEMBER_COUNT = "LIST_MEMBER_COUNT";
	
	/**
	 * <p>
	 * Report the list's URI, e.g., "/username/javait".
	 * </p>
	 */
	public static final String LIST_URI = "LIST_URI";
	
	/**
	 * <p>
	 * Report the list's following flag, e.g., "true" or "false".
	 * </p>
	 */
	public static final String LIST_FOLLOWING = "LIST_FOLLOWING";
	
	/**
	 * <p>
	 * Report the list's mode flag, e.g., "public" or "private".
	 * </p>
	 */
	public static final String LIST_MODE = "LIST_MODE";

	/**
	 * <p>
	 * Report the friendship's source user object, e.g., Friendship.
	 * class.
	 * </p>
	 */
	public static final String FRIENDSHIP_SOURCE = "FRIENDSHIP_SOURCE";

	/**
	 * <p>
	 * Report the friendship's target user object, e.g., Friendship.
	 * class.
	 * </p>
	 */
	public static final String FRIENDSHIP_TARGET = "FRIENDSHIP_TARGET";
	
	/**
	 * <p>
	 * Report the friendship's following state. It means the user is following
	 * the other one, e.g., "true" or "false".
	 * class.
	 * </p>
	 */
	public static final String FRIENDSHIP_FOLLOWING = "FRIENDSHIP_FOLLOWING";
	
	/**
	 * <p>
	 * Report the friendship's followed by state. It means the user is followed
	 * by the other one, e.g., "true" or "false".
	 * class.
	 * </p>
	 */
	public static final String FRIENDSHIP_FOLLOWED_BY =
		"FRIENDSHIP_FOLLOWED_BY";
	
	/**
	 * <p>
	 * Report the friendship's username, e.g., "johnsmith".
	 * </p>
	 */
	public static final String FRIENDSHIP_USER_NAME = "FRIENDSHIP_USER_NAME";
	
	/**
	 * <p>
	 * Report the friendship's user ID, e.g., "5465487864".
	 * </p>
	 */
	public static final String FRIENDSHIP_USER_ID = "FRIENDSHIP_USER_ID";

	/**
	 * <p>
	 * Report the friendship's marked spam state. It means the user is marked as
	 * spammer by the other one, e.g., "true" or "false".
	 * class.
	 * </p>
	 */
	public static final String FRIENDSHIP_MARKED_SPAM =
		"FRIENDSHIP_MARKED_SPAM";
	
	/**
	 * <p>
	 * Report the friendship's all replies state, e.g., "true" or "false".
	 * class.
	 * </p>
	 */
	public static final String FRIENDSHIP_ALL_REPLIES =
		"FRIENDSHIP_ALL_REPLIES";

	/**
	 * <p>
	 * Report the friendship's blocking state. It means the user is blocking the
	 * other one, e.g., "true" or "false".
	 * class.
	 * </p>
	 */
	public static final String FRIENDSHIP_BLOCKING = "FRIENDSHIP_BLOCKING";

	/**
	 * <p>
	 * Report the friendship's notifications enabled state. It means the user is
	 * up to receive notifications, e.g., "true" or "false".
	 * class.
	 * </p>
	 */
	public static final String FRIENDSHIP_NOTIFICATIONS_ENABLED =
		"FRIENDSHIP_NOTIFICATIONS_ENABLED";
	
	/**
	 * <p>
	 * Report the friendship's can direct message state. It means the user can 
	 * send DM to the other one, e.g., "true" or "false".
	 * class.
	 * </p>
	 */
	public static final String FRIENDSHIP_CAN_DM = "FRIENDSHIP_CAN_DM";
	
	/**
	 * <p>
	 * Report the friendship's want retweets state, e.g., "true" or "false".
	 * class.
	 * </p>
	 */
	public static final String FRIENDSHIP_WANT_RETWEETS =
		"FRIENDSHIP_WANT_RETWEETS";
	
	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private MetadataSet() {
	}
}