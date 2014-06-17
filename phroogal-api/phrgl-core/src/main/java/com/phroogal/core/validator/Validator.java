/**
 * 
 */
package com.phroogal.core.validator;

/**
 * Provides an abstraction for business requirement validations
 * @author Christopher Mariano
 *
 */
public interface Validator<T> {

	/**
	 * Process to validate the encompassing business rule.
	 * @param domain to be validated
	 * @return true if domain passes validation, false otherwise.
	 */
	public boolean isValid(T domain);
}
