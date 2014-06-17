package com.phroogal.core.exception;

/**
 * Exception thrown when a given email is not associated with any user in the data store
 * @author Christopher Mariano
 *
 */
public class UserEmailNotFoundException extends ApplicationException {
	
	private static final long serialVersionUID = 3351094171493451875L;
	
	public UserEmailNotFoundException() {
		super();
	}
	
	public UserEmailNotFoundException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_402";
	}
}
