package com.phroogal.core.resource;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * This class handles messages for UI consumption.
 *  It is used to ensure that all messages to be displayed in the
 *  UI are using language property keys instead of explicit <code>String</code>
 *  messages.
 *
 * @author Christopher Mariano
 * @version 0.01
 */
public class ApplicationMessage {   
    

	private static final String APPLICATION_MESSAGES_RES = "application_messages";
	
    /**
     * Public Constructor. 
     * @param code - this is the error code as indicated in the resource bundle.
     */
    public ApplicationMessage(String code) {
        this.code = code;
    }
    
    /**
     * Public Constructor. 
     * @param code - this is the error code as indicated in the resource bundle.
    * @param arguments - this would be the parameters for error messages that is parameterized in the language resources bundle.
     * <p>
     * For example: <br><br>
     *  Object[] arguments = {member.getEmail(), new Date()}; <br>
     *  errCodes.add(new ApplicationMessages("ERR_01", arguments));<br>
     *  throw (new ApplicationException(errCodes, FWD_ERROR)); <br><br>
     *  Properties File: <br>
     *  ERR_01=At {1,time} on {1,date}, The email {0} is blocked.<br><br>
     *  Output: At 12:30 PM on Jul 3, 2053, The email me@isp.com is blocked..
     * </p>
     */
    public ApplicationMessage(String code, Object[] arguments) {
        this.code = code;
        this.arguments = arguments.clone();
    }
    
    /**
     * key in the properties file
     */
    private String code = "";
    
    /**
     * arguments in the properties file.
     */
    private Object[] arguments = new Object[] {};

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the arguments
     */
    public Object[] getArguments() {
        return arguments.clone();
    }

    /**
     * Convenience method that returns the message from the properties file given the code
     * @return the message as defined in the application messages properties file
     */
	public String getMessage() {
		ResourceBundle resource = ResourceManager.getResource(APPLICATION_MESSAGES_RES);
		String message = resource.getString(getCode());
		
		if (getArguments() != null && getArguments().length > 0) {
			message = MessageFormat.format(message, getArguments());
		}
		return message;
	}
}
