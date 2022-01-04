/*  Original Licensing Copyright
 * 
 *  Various methods to access data from Wikipedia.
 *  Copyright (C) 2022  DZ-FSDev
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.dz_fs_dev.arachnidae.toolkit.wikimedia;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dz_fs_dev.common.counters.PerMinuteExponentialRateCounter;
import com.dz_fs_dev.common.net.NetTools;

/**
 * Contains various methods to access data from Wikipedia. Keeps track of
 * request rates to prevent abuse filtering. When the internal rate limit is
 * hit, the default behavior is to hang the executing thread to adhere to the
 * specified limit. This behavior can be modified with
 * {@link WikipediaTools#setSleepThrottling} to cause an exception to be
 * thrown instead.
 * 
 * @author DZ_FSDev
 * @since 17.0.1
 * @version 0.0.2
 */
public final class WikipediaTools {
	private WikipediaTools() {}

	/**
	 * The default read rate limit per minute to adhere to when no limit was
	 * provided by Wikipedia's API on application initialization. 
	 */
	private static final int DEFAULT_READ_RATELIMIT = 200;
	private static final int EMA_PERIOD = 6;
	
	private static final String WAPI = "https://en.wikipedia.org/w/api.php?";

	private static boolean sleepThrottling = true;

	//TODO https://en.wikipedia.org/w/api.php?action=query&meta=userinfo&uiprop=ratelimits
	private static PerMinuteExponentialRateCounter requestRateCounter =
			new PerMinuteExponentialRateCounter(WikipediaTools.EMA_PERIOD);

	/**
	 * Returns an array of wikimedia page title suggestions given an impartial
	 * query.
	 * 
	 * @param query The specified query to render suggestions.
	 * @return The suggestions rendered for the specified query.
	 * @throws JSONException Thrown when the format of the JSON data from
	 *                       Wikipedia may be malformed.
	 * @throws IOException Thrown if unable to connect to Wikipedia's web API.
	 * @since 0.0.1
	 */
	public static String[] getSuggestions(String query) throws JSONException, IOException {
		if(sleepThrottling && requestRateCounter.poll() > WikipediaTools.DEFAULT_READ_RATELIMIT) {
			//TODO
		}
		requestRateCounter.tick();
		JSONArray jarr = NetTools.tryReadJSONArrayFromUrl(
				String.format("%saction=opensearch&search=%s&format=json", WAPI, query)).getJSONArray(1);
		String[] ret = new String[jarr.length()];

		for(int i = 0; i < ret.length; i++) {
			ret[i] = jarr.getString(i);
		}

		return ret;
	}
	
	/**
	 * Returns the Wikipedia summary text given a page title.
	 * 
	 * @param title The specified page title to query for the summary text.
	 * @return The Wikipedia summary text given a page title.
	 * @throws JSONException Thrown when the format of the JSON data from
	 *                       Wikipedia may be malformed.
	 * @throws IOException Thrown if unable to connect to Wikipedia's web API.
	 * @since 0.0.2
	 */
	public static String getSummary(String title) throws JSONException, IOException {
		if(sleepThrottling && requestRateCounter.poll() > WikipediaTools.DEFAULT_READ_RATELIMIT) {
			//TODO
		}
		requestRateCounter.tick();
		JSONObject jobj = NetTools.tryReadJSONFromUrl(
				String.format("%saction=query&prop=extracts&exintro&explaintext&redirects=1&titles=%s&format=json", 
						WAPI, title.replace(' ', '_'))).getJSONObject("query").getJSONObject("pages");
		jobj = jobj.getJSONObject(jobj.keys().next());
		
		return jobj.getString("extract").trim();
	}
	
	/**
	 * 
	 * @param sleepThrottling
	 */
	public static void setSleepThrottling(boolean sleepThrottling) {
		WikipediaTools.sleepThrottling = sleepThrottling;
	}
}
