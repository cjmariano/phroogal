/**
 * 
 */
package com.phroogal.core.utility;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class that contains methods tht could be applied to search keywords
 * @author Christopher Mariano
 *
 */
public final class SearchKeywordUtility {
	
	/**
	 * Removes characters from the keyword that could otherwise not be consumable by the search engine
	 * @param keyword to be processed
	 * @return normalized keyword
	 */
	public static String distill(String keyword) {
		return keyword.replace("?", StringUtils.EMPTY)
				.replaceAll("[\\W]|_-", " ").trim();
	}
}
