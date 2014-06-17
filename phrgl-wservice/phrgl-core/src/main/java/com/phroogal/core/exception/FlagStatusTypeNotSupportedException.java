package com.phroogal.core.exception;

import com.phroogal.core.domain.PostType;

/**
 * Exception thrown when a given vote action type is not supported. Refer to {@link PostType} for supported values.
 * @author Christopher Mariano
 *
 */
public class FlagStatusTypeNotSupportedException extends ApplicationException {

	private static final long serialVersionUID = 5151544904755536880L;

	@Override
	protected String returnErrorCode() {
		return "ERR_622";
	}
}
