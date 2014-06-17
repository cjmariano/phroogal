package com.phroogal.core.exception;

/**
 * Exception thrown when a user credential contains invalid username and password
 * @author Christopher Mariano
 *
 */
public class DocumentNotFoundException extends ApplicationException {
	
	private static final long serialVersionUID = 6770026213810591629L;
	
	public DocumentNotFoundException() {
		super();
	}
	
	public DocumentNotFoundException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_401";
	}
}
