package com.phroogal.core.exception;

import com.phroogal.core.resource.ApplicationMessage;



/**
 * <b> This class is the base exception for all exceptions. All exceptions
 * will normally extend this base exception. </b>
 * 
 * @author Christopher Mariano
 *
 */
public abstract class ApplicationException extends RuntimeException {
	
	private static final long serialVersionUID = 1142281567130678923L;
	
	private Throwable throwable;
	
	private String errorCode;
	
	private String[] errMsgArguments;
	
	/**
	 * To be overridden by child classes to return the error code
	 * @return error code
	 */
	protected abstract String returnErrorCode();
	
	public ApplicationException() {
		this.errorCode = returnErrorCode();
    }
	
	public ApplicationException(String[] errMsgArguments) {
		this.errorCode = returnErrorCode();
		this.errMsgArguments = errMsgArguments;
		
    }

	public ApplicationException(Throwable throwable) {
		this.throwable = throwable;
		this.errorCode = returnErrorCode();
    }
	
	public ApplicationException(Throwable throwable, String[] arguments) {
		this.throwable = throwable;
		this.errorCode = returnErrorCode();
		this.errMsgArguments = arguments;
    }
	
	/**
	 * Method to return the original exception that triggered this exception
	 * @return instance of {@link Throwable} that triggered this exception.
	 */
	public Throwable getThrowable() {
		return throwable;
	}
	
	/**
	 * Method to return the application message associated with the given error code<br>
	 * set in this class
	 * @return the application message
	 */
	public ApplicationMessage getErrorMessage() {
		String[] args = errMsgArguments; 
		if ( args != null) {
			return new ApplicationMessage(errorCode, args);	
		}
		return new ApplicationMessage(errorCode);
	}
}
