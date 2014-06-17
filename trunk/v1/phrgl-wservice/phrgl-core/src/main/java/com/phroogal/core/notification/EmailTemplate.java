/**
 * 
 */
package com.phroogal.core.notification;




/**
 * Interface for generating email content based on templates
 * @author Christopher Mariano
 *
 */
public interface EmailTemplate {
	
	/**
	 * Returns the content of the email
	 * @param instance of {@link MailContentContext} that templates would use
	 * @return
	 */
	public String getContent(MailContentContext context);
}
