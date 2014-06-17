package com.phroogal.core.exception;

/**
 * Exception thrown when a user credential contains invalid username and password
 * @author Christopher Mariano
 *
 */
public class LoginFailedException extends ApplicationException {
	
	private static final long serialVersionUID = -5588695559827056155L;
	
	public LoginFailedException() {
		super();
	}
	
	public LoginFailedException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_301";
	}
}
