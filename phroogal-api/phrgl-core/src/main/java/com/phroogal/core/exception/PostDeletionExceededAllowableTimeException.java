/**
 * 
 */
package com.phroogal.core.exception;

import com.phroogal.core.domain.Post;

/**
 * Exception thrown when a {@link Post} is being deleted, is not allowed under business specifications, 
 * on the given context. 
 * @author Christopher Mariano
 *
 */
public class PostDeletionExceededAllowableTimeException extends ApplicationException {
	
	private static final long serialVersionUID = 5840868143395741901L;
	
	public PostDeletionExceededAllowableTimeException() {
		super();
	}
	
	public PostDeletionExceededAllowableTimeException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_619";
	}
	

}
