/*
 * AtomTweetParserImpl.java
 * 03/09/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package impl.javame.com.twitterapime.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.twitterapime.model.DefaultEntity;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Feed;
import com.twitterapime.parser.FeedEntry;
import com.twitterapime.parser.FeedParser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.search.Tweet;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class AtomTweetParserImpl extends FeedParser {
	/**
	 * @see com.twitterapime.parser.FeedParser#parse(java.io.InputStream)
	 */
	public void parse(InputStream xmlStream)
	  throws IOException,ParserException{
		KXmlParser parser = new KXmlParser();
		//
		try {
			parser.setInput(new InputStreamReader(xmlStream));
			//
			feed = new TFeed();
			int etype;
			Tweet tweet = null;
			Hashtable tweetValues = null;
			String tag;
			String xmlPath = "";
			//
			while (true) {
				etype = parser.next();
				//
				if (etype == XmlPullParser.START_TAG) {
					tag = parser.getName().toLowerCase();
					//
					if (tag.equals("entry")) {
						tweet = new Tweet();
						tweetValues = new Hashtable(15);
					} else if (tag.equals("link")
							&& xmlPath.equals("/feed/entry")) {
						final String attrValue =
							parser.getAttributeValue(0).toLowerCase();
						//
						if (attrValue.equals("text/html")) {
							tweetValues.put(
								MetadataSet.TWEET_HTML_LINK,
								parser.getAttributeValue(2));
						} else if (attrValue.equals("image/png")) {
							tweetValues.put(
								MetadataSet.TWEET_PICTURE_LINK,
								parser.getAttributeValue(2));
						}
					}
					//
					xmlPath += '/' + tag;
				} else if (etype == XmlPullParser.END_TAG) {
					tag = parser.getName().toLowerCase();
					//
					if (tag.equals("entry")) {
						tweet.setData(tweetValues);
						feed.addEntry(tweet);
						fireFeedEntryParsed(tweet);
					}
					//
					xmlPath = xmlPath.substring(0, xmlPath.lastIndexOf('/'));
				} else if (etype == XmlPullParser.TEXT) {
					final String text = parser.getText().trim();
					//
					if (xmlPath.equals("/feed/entry/id")) {
						tweetValues.put(MetadataSet.TWEET_ID, text);
					} else if (xmlPath.equals("/feed/entry/published")) {
						tweetValues.put(MetadataSet.TWEET_PUBLISHED, text);
					} else if (xmlPath.equals("/feed/entry/title")) {
						tweetValues.put(MetadataSet.TWEET_TITLE, text);
					} else if (xmlPath.equals("/feed/entry/content")) {
						tweetValues.put(MetadataSet.TWEET_CONTENT, text);
					} else if (xmlPath.equals("/feed/entry/updated")) {
						tweetValues.put(MetadataSet.TWEET_UPDATED, text);
					} else if (xmlPath.equals("/feed/entry/twitter:source")) {
						tweetValues.put(MetadataSet.TWEET_SOURCE, text);
					} else if (xmlPath.equals("/feed/entry/twitter:lang")) {
						tweetValues.put(MetadataSet.TWEET_LANG, text);
					} else if (xmlPath.equals("/feed/entry/author/name")) {
						tweetValues.put(MetadataSet.TWEET_AUTHOR_NAME, text);
					} else if (xmlPath.equals("/feed/entry/author/uri")) {
						tweetValues.put(MetadataSet.TWEET_AUTHOR_URI, text);
					}
				} else if (etype == XmlPullParser.END_DOCUMENT) {
					fireFeedParsed(feed);
					break;
				}
			}
		} catch (XmlPullParserException e) {
			throw new ParserException(e.getMessage());
		}
	}
	
	/**
	 * <p>
	 * </p>
	 * 
	 * @author Ernandes Mourao Junior (ernandes@gmail.com)
	 * @version 1.0
	 * @since 1.0
	 */
	private class TFeed extends DefaultEntity implements Feed {
		/**
		 * 
		 */
		private Vector entries;
		
		public TFeed() {
			entries = new Vector(20);
			data = new Hashtable();
			data.put("AtomTweetParserImpl.TFeed.TWEET_ARRAY", entries);
		}
		
		/**
		 * @see com.twitterapime.parser.Feed#getEntries()
		 */
		public FeedEntry[] getEntries() {
			if (entries == null || entries.size() == 0) {
				return new Tweet[0];
			}
			//
			Tweet[] tweets = new Tweet[entries.size()];
			entries.copyInto(tweets);
			//
			return tweets;
		}
		
		/**
		 * @see com.twitterapime.parser.Feed#addEntry(com.twitterapime.parser.FeedEntry)
		 */
		public void addEntry(FeedEntry entry) {
			entries.addElement(entry);
		}
	}
	
//	public static void main(String[] args) {
//		AtomTweetParserImpl p = new AtomTweetParserImpl();
//		try {
//			p.setFeedParserListener(new FeedParserListener() {
//				public void feedParsed(Feed feed) {
//					System.out.println(feed.toString());
//				}
//				
//				public void feedEntryParsed(FeedEntry entry) {
//					System.out.println(entry.toString());
//				}
//			});
//			p.parse(AtomTweetParserImpl.class.getResourceAsStream("/atom-teste.xml"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (FeedParserException e) {
//			e.printStackTrace();
//		}
//	}
}