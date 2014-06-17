/**
 * 
 */
package com.phroogal.core.exception;

/**
 * Thrown when file upload fails
 * @author Christopher Mariano
 *
 */
public class FileUploadFailedException extends ApplicationException {

	private static final long serialVersionUID = -2428040717539740174L;
	
	public FileUploadFailedException() {
		super();
	}
	
	public FileUploadFailedException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_403";
	}

}
