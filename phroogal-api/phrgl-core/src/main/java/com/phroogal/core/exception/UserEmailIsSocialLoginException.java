/**
 * 
 */
package com.phroogal.core.exception;

/**
 * Thrown when an email that is retrieved, is associated with a social login, not an email login.
 * @author Christopher Mariano
 *
 */
public class UserEmailIsSocialLoginException extends ApplicationException {

	private static final long serialVersionUID = 4427504581314923327L;
	
	public UserEmailIsSocialLoginException() {
		super();
	}
	
	public UserEmailIsSocialLoginException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_305";
	}
}
