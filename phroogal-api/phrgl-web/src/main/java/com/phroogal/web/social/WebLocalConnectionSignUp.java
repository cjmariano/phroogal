/**
 * 
 */
package com.phroogal.web.social;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.Connection;

import com.phroogal.core.domain.User;
import com.phroogal.core.service.UserService;
import com.phroogal.core.social.connect.LocalConnectionSignUp;
import com.phroogal.web.notification.WelcomeMailNotification;

/**
 * Extends {@link LocalConnectionSignUp} to add an email notification on signing up by social network
 * @author Christopher Mariano
 *
 */
public class WebLocalConnectionSignUp extends LocalConnectionSignUp {

	private static final Logger log = Logger.getLogger(WebLocalConnectionSignUp.class);
	
	@Autowired
	@Qualifier(value="welcomeMailNotification")
	private WelcomeMailNotification welcomeMailNotification;
	
	@Autowired
	private UserService userService;
	
	@Override
	public String execute(Connection<?> connection) {
		String newUserId = super.execute(connection);
		User user = userService.findById(new ObjectId(newUserId));
		sendWelcomeEmail(user);
		return newUserId;
	}

	private void sendWelcomeEmail(User user) {
		try {
			welcomeMailNotification.sendWelcomeMail(user);
		} catch (Exception e) {
			log.error("Welcome email not sent");
			e.printStackTrace();
		}
	}
}
