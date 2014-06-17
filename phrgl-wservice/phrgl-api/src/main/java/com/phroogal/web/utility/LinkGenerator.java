/**
 * 
 */
package com.phroogal.web.utility;

import static com.phroogal.web.context.WebApplicationContext.URI_QUESTION_LINK_FORMAT;

/**
 * Utility class that generates url request link from a given resource.
 * @author Christopher Mariano
 *
 */
public final class LinkGenerator {

	/**
	 * Generates a question link based on the given id and title
	 * @param referenceId used to reference a given domain
	 * @param title to be added as slug at the end of the link
	 * @return the partial link for the resource
	 */
	public static String getQuestionLink(long referenceId, String title) {
		return String.format(URI_QUESTION_LINK_FORMAT, String.valueOf(referenceId), 
				SlugGenerator.toSlug(title));
	}
}
