/**
 * 
 */
package com.twitterapime.rest;

import java.io.IOException;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;

/**
 * @author ernandes
 *
 */
public class TimelineTest extends TestCase {
	/**
	 * @param name
	 */
	public TimelineTest(String name) {
		super("TimelineTest");
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#getInstance(com.twitterapime.rest.UserAccountManager)}.
	 */
	public void testGetInstanceUserAccountManager() {
		try {
			Timeline.getInstance(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			Timeline.getInstance(uam);
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			uam.verifyCredential();
		} catch (IOException e) {
			fail();
		}
		//
		Timeline t = Timeline.getInstance(uam);
		assertNotNull(t);
		assertSame(t, Timeline.getInstance(uam));
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#getInstance()}.
	 */
	public void testGetInstance() {
		Timeline t = Timeline.getInstance();
		assertNotNull(t);
		assertSame(t, Timeline.getInstance());
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetPublicTweets(com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetPublicTweets() {
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetPublicTweets(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.startGetPublicTweets(new SearchDeviceListener() {
				private int count;
				public void tweetFound(Tweet tweet) {
					count++;
				}
				public void searchFailed(Throwable cause) {
					fail();
				}
				public void searchCompleted() {
					assertTrue(count > 0);
				}
			});	
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetHomeTweets(com.twitterapime.search.Query, com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetHomeTweets() {
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			assertTrue(uam.verifyCredential());
		} catch (Exception e1) {
			fail();
		}
		//
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetHomeTweets(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.startGetHomeTweets(null, new SearchDeviceListener() {
				public void tweetFound(Tweet tweet) {}
				public void searchFailed(Throwable cause) {}
				public void searchCompleted() {}
			});
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		t = Timeline.getInstance(uam);
		//
		try {
			t.startGetHomeTweets(null, new SearchDeviceListener() {
				private int countUser;
				private int countFollower;
				public void tweetFound(Tweet tweet) {
					try {
						String userName = tweet.getUserAccount().getString(MetadataSet.TWEET_AUTHOR_USERNAME);
						if (userName.equals("twiterapime")) {
							countFollower++;
						} else {
							userName = tweet.getUserAccount().getString(MetadataSet.TWEET_AUTHOR_USERNAME);
							if (userName.equals("twiterapimetest")) {
								countUser++;
							}
						}
					} catch (Exception e) {
						fail();
					}
				}
				public void searchFailed(Throwable cause) {
					fail();
				}
				public void searchCompleted() {
					assertTrue(countUser > 0);
					assertTrue(countFollower > 0);
				}
			});	
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.startGetHomeTweets(QueryComposer.count(1), new SearchDeviceListener() {
				private int count;
				public void tweetFound(Tweet tweet) {
					count++;
				}
				public void searchFailed(Throwable cause) {
					fail();
				}
				public void searchCompleted() {
					assertTrue(count == 1);
				}
			});	
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetUserTweets(com.twitterapime.search.Query, com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetUserTweets() {
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			assertTrue(uam.verifyCredential());
		} catch (Exception e1) {
			fail();
		}
		//
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetUserTweets(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.startGetUserTweets(null, new SearchDeviceListener() {
				public void tweetFound(Tweet tweet) {}
				public void searchFailed(Throwable cause) {}
				public void searchCompleted() {}
			});
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		t = Timeline.getInstance(uam);
		//
		try {
			t.startGetUserTweets(null, new SearchDeviceListener() {
				private int count;
				public void tweetFound(Tweet tweet) {
					try {
						assertEquals("twiterapimetest", tweet.getUserAccount().getString(MetadataSet.TWEET_AUTHOR_USERNAME));
						count++;
					} catch (Exception e) {
						fail();
					}
				}
				public void searchFailed(Throwable cause) {
					fail();
				}
				public void searchCompleted() {
					assertTrue(count > 0);
				}
			});	
		} catch (Exception e) {
			fail();
		}
		//
		//
		try {
			t.startGetUserTweets(QueryComposer.count(1), new SearchDeviceListener() {
				private int count;
				public void tweetFound(Tweet tweet) {
					try {
						assertEquals("twiterapimetest", tweet.getUserAccount().getString(MetadataSet.TWEET_AUTHOR_USERNAME));
						count++;
					} catch (Exception e) {
						fail();
					}
				}
				public void searchFailed(Throwable cause) {
					fail();
				}
				public void searchCompleted() {
					assertTrue(count == 1);
				}
			});	
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetMentions(com.twitterapime.search.Query, com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetMentions() {
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			assertTrue(uam.verifyCredential());
		} catch (Exception e1) {
			fail();
		}
		//
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetMentions(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.startGetMentions(null, new SearchDeviceListener() {
				public void tweetFound(Tweet tweet) {}
				public void searchFailed(Throwable cause) {}
				public void searchCompleted() {}
			});
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		t = Timeline.getInstance(uam);
		//
		try {
			t.startGetMentions(null, new SearchDeviceListener() {
				private int count;
				public void tweetFound(Tweet tweet) {
					try {
						assertTrue(tweet.getString(MetadataSet.TWEET_CONTENT).indexOf("@twiterapimetest") != -1);
						count++;
					} catch (Exception e) {
						fail();
					}
				}
				public void searchFailed(Throwable cause) {
					fail();
				}
				public void searchCompleted() {
					assertTrue(count > 0);
				}
			});	
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.startGetMentions(QueryComposer.count(1), new SearchDeviceListener() {
				private int count;
				public void tweetFound(Tweet tweet) {
					try {
						assertTrue(tweet.getString(MetadataSet.TWEET_CONTENT).indexOf("@twiterapimetest") != -1);
						count++;
					} catch (Exception e) {
						fail();
					}
				}
				public void searchFailed(Throwable cause) {
					fail();
				}
				public void searchCompleted() {
					assertTrue(count == 1);
				}
			});	
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Timeline#startGetDirectMessages(com.twitterapime.search.Query, boolean, com.twitterapime.search.SearchDeviceListener)}.
	 */
	public void testStartGetDirectMessages() {
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			assertTrue(uam.verifyCredential());
		} catch (Exception e1) {
			fail();
		}
		//
		Timeline t = Timeline.getInstance();
		//
		try {
			t.startGetDirectMessages(null, true, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.startGetDirectMessages(null, true, new SearchDeviceListener() {
				public void tweetFound(Tweet tweet) {}
				public void searchFailed(Throwable cause) {}
				public void searchCompleted() {}
			});
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		t = Timeline.getInstance(uam);
		//
		try {
			t.startGetDirectMessages(null, true, new SearchDeviceListener() {
				private int count;
				public void tweetFound(Tweet tweet) {
					try {
						assertEquals("twiterapimetest", tweet.getRecipientAccount().getString(MetadataSet.TWEET_AUTHOR_USERNAME));
						count++;
					} catch (Exception e) {
						fail();
					}
				}
				public void searchFailed(Throwable cause) {
					fail();
				}
				public void searchCompleted() {
					assertTrue(count > 0);
				}
			});	
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.startGetDirectMessages(null, false, new SearchDeviceListener() {
				private int count;
				public void tweetFound(Tweet tweet) {
					try {
						assertEquals("twiterapimetest", tweet.getUserAccount().getString(MetadataSet.TWEET_AUTHOR_USERNAME));
						count++;
					} catch (Exception e) {
						fail();
					}
				}
				public void searchFailed(Throwable cause) {
					fail();
				}
				public void searchCompleted() {
					assertTrue(count > 0);
				}
			});	
		} catch (Exception e) {
			fail();
		}
		//
		try {
			t.startGetDirectMessages(QueryComposer.count(1), true, new SearchDeviceListener() {
				private int count;
				public void tweetFound(Tweet tweet) {
					try {
						assertEquals("twiterapimetest", tweet.getRecipientAccount().getString(MetadataSet.TWEET_AUTHOR_USERNAME));
						count++;
					} catch (Exception e) {
						fail();
					}
				}
				public void searchFailed(Throwable cause) {
					fail();
				}
				public void searchCompleted() {
					assertTrue(count == 1);
				}
			});	
		} catch (Exception e) {
			fail();
		}
	}
}