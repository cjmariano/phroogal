/**
 * 
 */
package com.phroogal.core.exception;

/**
 * Thrown when a user's account is inactive
 * @author Christopher Mariano
 *
 */
public class AccountNotActiveException extends ApplicationException {

	private static final long serialVersionUID = -4855753461358879278L;
	
	public AccountNotActiveException() {
		super();
	}
	
	public AccountNotActiveException(Throwable throwable) {
		super(throwable);
	}
	
	public AccountNotActiveException(String[] errMsgArguments) {
		super(errMsgArguments);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_306";
	}
}
