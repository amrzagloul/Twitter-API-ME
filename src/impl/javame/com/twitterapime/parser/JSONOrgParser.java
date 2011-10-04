/*
 * JSONOrgParser.java
 * 15/10/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package impl.javame.com.twitterapime.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.twitterapime.parser.Handler;
import com.twitterapime.parser.JSONHandler;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;

/**
 * <p>
 * This class implements a parser based on JSON.org library.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.5
 */
public final class JSONOrgParser extends Parser {
	/**
	 * @see com.twitterapime.parser.Parser#parse(java.io.InputStream, com.twitterapime.parser.Handler)
	 */
	public void parse(InputStream in, Handler handler) throws IOException,
		ParserException {
		if (!(handler instanceof JSONHandler)) {
			throw new IllegalArgumentException(
				"Handler object must implement JSONHandler.");
		}
		//
		try {
			JSONHandler hdlr = (JSONHandler)handler;
			JSONObject outer = new JSONObject(streamToString(in));
			//
			hdlr.handle(new JSONOrgObject(outer));
		} catch (ParserException e) {
			throw new ParserException(e.getMessage());
		} catch (JSONException e) {
			throw new ParserException(e.getMessage());
		} catch (Exception e) {
			throw new ParserException(e.getMessage());
		}
	}
	
	/**
	 * <p>
	 * Get the string content from a given stream.
	 * </p>
	 * @param stream Stream.
	 * @return String.
	 * @throws IOException If an I/O error occurs.
	 */
	private String streamToString(InputStream stream) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        int c;
        //
        while ((c = stream.read()) != -1) {
        	out.write(c);
        }
        //
        String json = new String(out.toByteArray(), "UTF-8");
        //
        if (json.startsWith("[")) {
        	json = json.substring(1, json.length() -1); //remove leading and trailing brackets.
        	json = "{\"root\": " + json + "}";
        }
        //
        return json;
	}
}
