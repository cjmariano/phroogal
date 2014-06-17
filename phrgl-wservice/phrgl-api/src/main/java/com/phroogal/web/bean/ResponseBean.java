package com.phroogal.web.bean;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.phroogal.core.resource.ApplicationMessage;

/**
 * Bean that wraps the response of the controllers to the requests
 * @author Christopher Mariano
 *
 */
public class ResponseBean {

	private static final Logger log = Logger.getLogger(ResponseBean.class);
	
	public enum Status {
		SUCCESS, FAILURE, ERROR
	};
	
	private Status status;
	
	@JsonSerialize(include=Inclusion.NON_NULL)
	private Object response;
	
	@JsonSerialize(include=Inclusion.NON_NULL)
	private ErrorResponseBean error;
	
	public ResponseBean() {
		super();
	}
	
	public ResponseBean(Status status, Object response) {
		this.status = status;
		this.response = response;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public ErrorResponseBean getError() {
		return error;
	}
	
	public void setError(ErrorResponseBean error) {
		this.error = error;
	}
	
	public ResponseBean addErrorApplicationMessage(ApplicationMessage errCode) {
		error = createErrorBean(errCode);
		status = ResponseBean.Status.ERROR;	
		return this;
	}

	private ErrorResponseBean createErrorBean(ApplicationMessage errMsg) {
		ErrorResponseBean error = new ErrorResponseBean();
		error.setCode(errMsg.getCode());
		error.setMessage(errMsg.getMessage());		
		log.error(errMsg.getCode() + ": " + errMsg.getMessage());
		return error;
	}
}
