package com.dz_fs_dev.arachnidae.toolkit;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Non-constructable class containing web validation methods.
 * 
 * @author DZ-FSDev
 * @since 16.0.1
 * @version 0.0.1
 */
public final class ValidationTools {
	public final static String IANA_TLD_WEB_ADDRESS = "https://data.iana.org/TLD/tlds-alpha-by-domain.txt";

	public final static String COMMERCIAL_TOP_LEVEL_DOMAIN = "com";
	public final static String ORGANIZATION_TOP_LEVEL_DOMAIN = "org";

	private static String ianaVersionHeader = null;
	private static Set<String> tld = new HashSet<String>();

	/**
	 * Evaluates whether a given website is a valid organization(*.{@value ValidationTools#COMMERCIAL_TOP_LEVEL_DOMAIN})) one. Conforms to RFC 2732.
	 * 
	 * @param webAddress The web address to be tested.
	 * @return Whether the web address is a valid commercial one.
	 * @throws MalformedURLException When the web address provided cannot be parsed to conform to RFC 2732.
	 * @since 0.0.1
	 */
	public static boolean isWebsiteCommercial(String webAddress) throws MalformedURLException
	{
		return COMMERCIAL_TOP_LEVEL_DOMAIN.equalsIgnoreCase(new URL(webAddress).getHost());
	}

	/**
	 * Evaluates whether a given website is a valid organization(*.{@value ValidationTools#ORGANIZATION_TOP_LEVEL_DOMAIN})) one. Conforms to RFC 2732.
	 * 
	 * @param webAddress The web address to be tested.
	 * @return Whether the web address is a valid organizational one.
	 * @throws MalformedURLException When the web address provided cannot be parsed to conform to RFC 2732.
	 * @since 0.0.1
	 */
	public static boolean isWebsiteOrganizational(String webAddress) throws MalformedURLException
	{
		return ORGANIZATION_TOP_LEVEL_DOMAIN.equalsIgnoreCase(new URL(webAddress).getHost());
	}

	/**
	 * Populates the set of Top Level Domains. Proceeds in all-or-none fashion. Retries can be performed on a return of null indicating failure.
	 * 
	 * @see <a href=https://data.iana.org/TLD/tlds-alpha-by-domain.txt>
	 * 		IANA Authoritative Top Level Domains List</a>
	 * @return The current IANA TLD List version header; or null on failure.
	 * @since 0.0.1
	 */
	public static String queryIANA() {
		try (BufferedInputStream inputStream = new BufferedInputStream(new URL(IANA_TLD_WEB_ADDRESS).openStream());
				Scanner scanner = new Scanner(inputStream)) {
			String ianaVersionHeader = "";
			Set<String> tld = new HashSet<String>();
			
			while(scanner.hasNext()) {
				String line = scanner.nextLine();
				if(line.length() > 0) {
					if(Character.isAlphabetic(line.charAt(0))) {
						ianaVersionHeader += line;
					}else {
						tld.add(line.toLowerCase());
					}
				}
			}
			
			if(ianaVersionHeader.length() > 0) {
				ValidationTools.ianaVersionHeader = ianaVersionHeader;
				ValidationTools.tld = tld;
			}else {
				System.out.println("Population of IANA TLD set failed. Please contact your administrator.");
			}
		} catch (IOException e) {
			return null;
		}

		return ianaVersionHeader;
	}

	/**
	 * Gets the most recent TLD query header.
	 * 
	 * @return The IANA version header if a TLD database was queried successfully.
	 */
	public static String getIanaVersionHeader() {
		return ianaVersionHeader;
	}

	/**
	 * Resets the TLD database by clearing all known TLDs.
	 */
	public static void reset() {
		ianaVersionHeader = null;
		tld = new HashSet<String>();
	}
}
