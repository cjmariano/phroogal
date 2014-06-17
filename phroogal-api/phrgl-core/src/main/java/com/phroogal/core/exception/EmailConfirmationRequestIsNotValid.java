package com.phroogal.core.exception;

/**
 * Thrown when password reset request does not exist
 * @author c.j.mariano
 *
 */
public class EmailConfirmationRequestIsNotValid  extends ApplicationException {

	private static final long serialVersionUID = 6532253077180396993L;
	
	public EmailConfirmationRequestIsNotValid() {
		super();
	}
	
	public EmailConfirmationRequestIsNotValid(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_303";
	}
}
