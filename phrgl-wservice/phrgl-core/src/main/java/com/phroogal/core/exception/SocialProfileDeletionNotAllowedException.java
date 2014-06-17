package com.phroogal.core.exception;

/**
 * Exception thrown when a user credential contains invalid username and password
 * @author Christopher Mariano
 *
 */
public class SocialProfileDeletionNotAllowedException extends ApplicationException {
	
	private static final long serialVersionUID = -5429370437120599185L;
	
	public SocialProfileDeletionNotAllowedException() {
		super();
	}
	
	public SocialProfileDeletionNotAllowedException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_502";
	}
}
