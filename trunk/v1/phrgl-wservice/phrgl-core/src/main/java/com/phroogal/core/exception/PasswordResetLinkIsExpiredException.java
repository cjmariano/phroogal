/**
 * 
 */
package com.phroogal.core.exception;

/**
 * Password link has already expired
 * @author Christopher Mariano
 *
 */
public class PasswordResetLinkIsExpiredException extends ApplicationException {

	private static final long serialVersionUID = -2727153932670156856L;
	
	public PasswordResetLinkIsExpiredException() {
		super();
	}
	
	public PasswordResetLinkIsExpiredException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_410";
	}
}
