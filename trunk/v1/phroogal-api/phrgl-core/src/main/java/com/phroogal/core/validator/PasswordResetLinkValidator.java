/**
 * 
 */
package com.phroogal.core.validator;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.security.PasswordResetRequest;

/**
 * Validates if the password reset link is still valid
 * @author Christopher Mariano
 *
 */
@Service(value="passwordResetLinkValidator")
public class PasswordResetLinkValidator implements Validator<PasswordResetRequest> {


	@Value("${mail.password.reset.validity.hours}")
	private int passwordLinkValidity;
	
	@Override
	public boolean isValid(PasswordResetRequest passwordResetRequest) {
		DateTime requestDate = passwordResetRequest.getRequestDate();
		DateTime deadlineDate = requestDate.plusHours(passwordLinkValidity);
		if (deadlineDate.isBeforeNow()) {
			return false;
		}
		return true;
	}

}
