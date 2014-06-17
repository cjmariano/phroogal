/**
 * 
 */
package com.phroogal.core.domain;

import com.phroogal.core.valueobjects.PropertyBag;


/**
 * Indicates that properties of implementing classes could be modified individually without affecting other attributes  
 * @author Christopher Mariano
 *
 */
public interface PropertyModifiable {

	/**
	 * Modifies the implementing class based on the list of properties given wherein a key-value pair, represents a field-value relationship.
	 * @param properties instance of {@link PropertyBag} that contains the key-value pair
	 * @throws Exception when update failed
	 */
	public void partialUpdate(PropertyBag properties) throws Exception;
}
