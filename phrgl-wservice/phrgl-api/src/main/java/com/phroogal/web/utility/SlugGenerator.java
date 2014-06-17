/**
 * 
 */
package com.phroogal.web.utility;

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 * Utility class that generates slugs for a given string. Useful for creating SEO-friendly URL
 * @author Christopher Mariano
 *
 */
public final class SlugGenerator {
	
	/**
	 * Generates slugs for a given string
	 * @param text to be converted to slug
	 * @return slug
	 */
	public static String toSlug(String text) {
		return Normalizer.normalize(text.toLowerCase(), Form.NFD)
			.replace(".", " ")
			.replaceAll("\\p{InCombiningDiacriticalMarks}|[^\\w\\s]", "")
			.replaceAll("[\\s-]+", " ")
			.trim()
			.replaceAll("\\s", "-");
	}
}
