package com.phroogal.core.exception;

import com.phroogal.core.domain.VoteActionType;

/**
 * Exception thrown when a given vote action type is not supported. Refer to {@link VoteActionType} for supported values.
 * @author Christopher Mariano
 *
 */
public class VoteActionNotSupportedException extends ApplicationException {
	
	private static final long serialVersionUID = -5130609326821613766L;
	
	public VoteActionNotSupportedException() {
		super();
	}
	
	public VoteActionNotSupportedException(Throwable throwable) {
		super(throwable);
	}

	@Override
	protected String returnErrorCode() {
		return "ERR_620";
	}
}
