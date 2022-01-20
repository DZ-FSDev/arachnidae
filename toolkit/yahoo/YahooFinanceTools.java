/*  Original Licensing Copyright
 * 
 *  Various methods to access data from Yahoo Finance.
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
package com.dz_fs_dev.arachnidae.toolkit.yahoo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dz_fs_dev.common.counters.PerMinuteExponentialRateCounter;
import com.dz_fs_dev.common.net.NetTools;

/**
 * Contains various methods to access data from Yahoo Finance. Keeps track of
 * request rates to prevent abuse filtering. When the internal rate limit is
 * hit, the default behavior is to hang the executing thread to adhere to the
 * specified limit. This behavior can be modified with
 * {@link YahooFinanceTools#setSleepThrottling} to cause an exception to be
 * thrown instead.
 * 
 * @author DZ_FSDev
 * @since 17.0.1
 * @version 0.0.1
 */
public class YahooFinanceTools {
	//https://query1.finance.yahoo.com/v7/finance/download/ACST?period1=1611092473&period2=1642628473&interval=1d&events=history&includeAdjustedClose=true
	private YahooFinanceTools() {}

	/**
	 * The default read rate limit per minute to adhere to when no limit was
	 * provided by Yahoo Finance's API on application initialization. 
	 */
	private static final int DEFAULT_READ_RATELIMIT = 95;
	private static final int EMA_PERIOD = 6;
	
	private static final long SECONDS_PER_YEAR = 31536000;
	
	private static final String WAPI = "https://query1.finance.yahoo.com/v7/finance/download/%s?%s";

	private static boolean sleepThrottling = true;

	//TODO 
	private static PerMinuteExponentialRateCounter requestRateCounter =
			new PerMinuteExponentialRateCounter(YahooFinanceTools.EMA_PERIOD);
	
	/**
	 * 
	 * @param ticker
	 * @return
	 */
	public static JSONObject get(String ticker) {
		JSONObject ret = new JSONObject();
		
		ArrayList<String> queryResult = null;
		try {
			queryResult = NetTools.readFromUrl(String.format(WAPI, ticker,
					"period1=1611092473&period2=1642628473&interval=1d&events=history&includeAdjustedClose=true"));
			
			JSONArray candles = new JSONArray();
			String[] header = queryResult.get(0).split(",");
			
			System.out.println(Arrays.toString(header));

			ret.put("ticker", ticker);
			
			for(int i = 1; i < queryResult.size(); i++) {
				String[] record = queryResult.get(i).split(",");
				JSONObject candle = new JSONObject();
				
				for(int j = 0; j < header.length; j++) {
					candle.put(header[j], record[j]);
				}
				
				candles.put(candle);
			}
			
			System.out.print(System.currentTimeMillis()/1000);
			
			ret.put("candles", candles);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @param sleepThrottling
	 */
	public static void setSleepThrottling(boolean sleepThrottling) {
		YahooFinanceTools.sleepThrottling = sleepThrottling;
	}
}
