/**
 * 
 */
package com.phroogal.core.exception;

/**
 * This is thrown whenever a given password is invalid
 * @author Christopher Mariano
 *
 */
public class InvalidPasswordException extends ApplicationException {

	private static final long serialVersionUID = -4228651849982188929L;
	
	public InvalidPasswordException() {
		super();
	}
	
	public InvalidPasswordException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_404";
	}

}
