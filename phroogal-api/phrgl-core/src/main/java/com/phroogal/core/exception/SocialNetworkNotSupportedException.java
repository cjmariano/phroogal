package com.phroogal.core.exception;

/**
 * Exception thrown when a user credential contains invalid username and password
 * @author Christopher Mariano
 *
 */
public class SocialNetworkNotSupportedException extends ApplicationException {

	private static final long serialVersionUID = 2994970725510016597L;
	
	public SocialNetworkNotSupportedException() {
		super();
	}
	
	public SocialNetworkNotSupportedException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_501";
	}
}
