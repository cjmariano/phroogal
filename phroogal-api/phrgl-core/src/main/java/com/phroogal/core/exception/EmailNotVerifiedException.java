package com.phroogal.core.exception;

/**
 * Thrown when password reset request does not exist
 * @author c.j.mariano
 *
 */
public class EmailNotVerifiedException  extends ApplicationException {

	private static final long serialVersionUID = 4192545247607534624L;
	
	public EmailNotVerifiedException() {
		super();
	}
	
	public EmailNotVerifiedException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_304";
	}
}
