/**
 * 
 */
package com.phroogal.core.service;

import java.util.List;

import com.phroogal.core.domain.ExternalResource;
import com.phroogal.core.exception.ExternalResourcesNotFoundException;

/**
 * Service for providing links to External Resources
 * @author Christopher Mariano
 *
 */
public interface ExternalResourceService {

	/**
	 * Retrieves an all external resource given a keyword
	 * @param keyword to match results
	 * @param starting index for the results to be returned
	 * @return list of {@link ExternalResource} that matches the keyword
	 * @throws {@link ExternalResourcesNotFoundException} when no resources were found
	 */
	public List<ExternalResource> getResourceByKeyword (String keyword, Long start) throws ExternalResourcesNotFoundException;
	
	/**
	 * Retrieves an external resource given a keyword and a category
	 * @param category to search results in
	 * @param keyword to match results
	 * @param starting index for the results to be returned
	 * @return list of {@link ExternalResource} that matches the keyword
	 * @throws {@link ExternalResourcesNotFoundException} when no resources were found
	 */
	public List<ExternalResource> getResourceByKeyword (String category, String keyword, Long start) throws ExternalResourcesNotFoundException;
}
