/**
 * 
 */
package com.phroogal.core.exception;

/**
 * Indicates that no user is authenticated within the system.
 * @author Christopher Mariano
 *
 */
public class UserNotAuthenticatedException extends ApplicationException {

	private static final long serialVersionUID = -8660579848398931302L;
	
	public UserNotAuthenticatedException() {
		super();
	}
	
	public UserNotAuthenticatedException(Throwable throwable) {
		super(throwable);
	}
	
	@Override
	protected String returnErrorCode() {
		return "ERR_302";
	}
}
