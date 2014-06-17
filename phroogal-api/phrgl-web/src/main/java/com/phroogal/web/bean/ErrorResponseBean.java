package com.phroogal.web.bean;


/**
 * {@link ResponseBean} that wraps response wth errors
 * @author Christopher Mariano
 *
 */
public class ErrorResponseBean {

	private String code;
	
	private String message;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
