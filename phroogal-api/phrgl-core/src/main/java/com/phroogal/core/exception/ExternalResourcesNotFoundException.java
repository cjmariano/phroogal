package com.phroogal.core.exception;

/**
 * Exception thrown when there are no external resources to search were found
 * @author Christopher Mariano
 *
 */
public class ExternalResourcesNotFoundException extends ApplicationException {
	
	private static final long serialVersionUID = -7760039238883180697L;
	
	public ExternalResourcesNotFoundException() {
		super();
	}
	
	public ExternalResourcesNotFoundException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_360";
	}
}
