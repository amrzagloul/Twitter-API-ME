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
 * This class defines the implementation of an Atom feed parser responsible for
 * parsing the result from Twitter Search API, for Java ME platform. 
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
							getAttributeValue(parser, "type");
						//
						if (attrValue.equals("text/html")) {
							tweetValues.put(
								MetadataSet.TWEET_URI,
								getAttributeValue(parser, "href"));
						} else if (attrValue.equals("image/png")) {
							tweetValues.put(
								MetadataSet.TWEET_AUTHOR_PICTURE_URI,
								getAttributeValue(parser, "href"));
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
						tweetValues.put(
							MetadataSet.TWEET_PUBLISH_DATE, formatDate(text));
					} else if (xmlPath.equals("/feed/entry/title")) {
						tweetValues.put(MetadataSet.TWEET_CONTENT, text);
					} else if (xmlPath.equals("/feed/entry/twitter:source")) {
						tweetValues.put(MetadataSet.TWEET_SOURCE,
								removeTags(text));
					} else if (xmlPath.equals("/feed/entry/twitter:lang")) {
						tweetValues.put(MetadataSet.TWEET_LANG, text);
					} else if (xmlPath.equals("/feed/entry/author/name")) {
						String[] names = splitAuthorNames(text);
						tweetValues.put(
							MetadataSet.TWEET_AUTHOR_USERNAME, names[0]);
						tweetValues.put(
							MetadataSet.TWEET_AUTHOR_NAME, names[1]);
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
	 * Split the author's username and name from the given string.
	 * </p>
	 * @param name The name.
	 * @return The username [0] and full name [1].
	 */
	private String[] splitAuthorNames(String name) {
		String[] names = new String[2];
		//
		if (name == null) {
			return names;
		}
		//
		name = name.trim();
		final int i = name.indexOf(' ');
		if (i == -1) {
			names[0] = name;
			names[1] = name;
		} else {
			names[0] = name.substring(0, i);
			names[1] = name.substring(i +2, name.length() -1);
		}
		//
		return names;
	}
	
	/**
	 * <p>
	 * Format the date of a tweet in standard date value.
	 * </p>
	 * @param date Tweet date.
	 * @return Standard date value.
	 */
	private String formatDate(String date) {
		return date != null
			? date.replace('T', ' ').replace('Z', ' ').trim()
			: null;
	}
	
	/**
	 * <p>
	 * Get the value of a given attribute from the parser object.
	 * </p>
	 * @param parser The parser object.
	 * @param attr The attribute.
	 * @return The value.
	 */
	private String getAttributeValue(KXmlParser parser, String attr) {
		for (int i = parser.getAttributeCount() -1; i >= 0; i--) {
			if (parser.getAttributeName(i).equalsIgnoreCase(attr)) {
				return parser.getAttributeValue(i);
			}
		}
		//
		return null;
	}
	
	/**
	 * <p>
	 * Remove any tag occurrence from the given string.
	 * </p>
	 * @param str String to be parsed.
	 * @return String with no tags.
	 */
	private String removeTags(String str) {
		if (str == null) {
			return null;
		}
		//
		StringBuffer out = new StringBuffer();
		char cs[] = str.toCharArray();
		boolean tagFound = false;
		int i1 = 0;
		int l = 0;
		//
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] == '<' && !tagFound) {
				out.append(cs, i1, l);
				//
				i1 = i;
				l = 0;
				tagFound = true;
				l++;
			} else if (cs[i] == '>' && tagFound) {
				i1 = i +1;
				l = 0;
				tagFound = false;
			} else {
				l++;
			}
		}
		if (l > 0) {
			out.append(cs, i1, l);
		}
		//
		return out.toString().trim();
	}
	
	/**
	 * <p>
	 * This class implements a feed.
	 * </p>
	 * 
	 * @author Ernandes Mourao Junior (ernandes@gmail.com)
	 * @version 1.0
	 * @since 1.0
	 */
	private class TFeed extends DefaultEntity implements Feed {
		/**
		 * <p>
		 * Hold the feed's entries.
		 * </p>
		 */
		private Vector entries;
		
		/**
		 * <p>
		 * Create an instance of TFeed class.
		 * </p>
		 */
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
}