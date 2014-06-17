package com.phroogal.web.controller.rest;

public class RestPatchRequest {
	
	private RestPatchOperationType operation;

	private String property;
	
	private String value;

	public RestPatchOperationType getOperation() {
		return operation;
	}

	public void setOperation(RestPatchOperationType operation) {
		this.operation = operation;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
