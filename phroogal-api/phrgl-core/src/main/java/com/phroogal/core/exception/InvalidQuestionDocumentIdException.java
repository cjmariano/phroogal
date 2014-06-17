/**
 * 
 */
package com.phroogal.core.exception;

/**
 * Thrown when a given document id retrieved does not return any results
 * @author Christopher Mariano
 *
 */
public class InvalidQuestionDocumentIdException extends ApplicationException {

	private static final long serialVersionUID = -6760024448132627112L;

	public InvalidQuestionDocumentIdException() {
		super();
	}
	
	public InvalidQuestionDocumentIdException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_623";
	}
	
}
