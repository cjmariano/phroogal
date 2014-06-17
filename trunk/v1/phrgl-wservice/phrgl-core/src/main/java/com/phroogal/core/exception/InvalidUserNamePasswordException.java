package com.phroogal.core.exception;

/**
 * Exception thrown when a user credential contains invalid username and password
 * @author Christopher Mariano
 *
 */
public class InvalidUserNamePasswordException extends ApplicationException {
	
	private static final long serialVersionUID = -6933322431988598748L;
	
	public InvalidUserNamePasswordException() {
		super();
	}
	
	public InvalidUserNamePasswordException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_300";
	}
}
