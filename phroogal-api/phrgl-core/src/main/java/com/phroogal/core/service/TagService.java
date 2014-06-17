/**
 * 
 */
package com.phroogal.core.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import com.phroogal.core.domain.Tag;



public interface TagService extends Service<Tag, ObjectId> {

	/**
	 * Finds a tag that starts with the given keyword
	 * @param keyword for search
	 * @return a list of tags that has a name which starts with the keyword
	 */
	public List<Tag> findByNameStartingWith(String keyword);
	
	/**
	 * Returns a list of {@link Tag} with trending details populated.
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return a paginated list of Trending Tag
	 */
	public Page<Tag> getTrendingTags(int pageAt, int pageSize);
	
	/**
	 * Returns a list of {@link Tag} with trending details populated, and sorted by ascending name
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return a paginated list of Tags 
	 */
	public Page<Tag> getTagsSortByAscName(int pageAt, int pageSize);
	
	/**
	 * Returns a list of {@link Tag} with trending details populated, and sorted by descending 
	 * total number of questions tagged.
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return a paginated  list of Tags
	 */
	public Page<Tag> getTagsSortByDescTotalNumQuestionsTagged(int pageAt, int pageSize);
	
	/**
	 * Refreshes the statistical information for tags.<br> 
	 * Statistical details includes:<br>
	 * a) Number of questions that are tagged with the particular tag entry<br>
	 * b) Summation of the number of views on each questions that are tagged with the particular tag entry 
	 * @param list of tags.
	 */
	public void refreshTagMetadata(List<String> tags);
}
