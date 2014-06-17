package com.phroogal.core.validator;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phroogal.core.domain.security.PasswordResetRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class PasswordResetLinkValidatorTest {

	@Autowired
	@Qualifier(value="passwordResetLinkValidator")
	private Validator<PasswordResetRequest> passwordResetLinkValidator;
	
	@Value("${mail.password.reset.validity.hours}")
	private int passwordLinkValidity;
	
	@Test
	public void testIsValidReturnsFalse() throws Exception {
		PasswordResetRequest request = new PasswordResetRequest();
		DateTime requestDate = new DateTime();
		requestDate = requestDate.minusHours(passwordLinkValidity + 2);
		request.setRequestDate(requestDate);
		
		assertFalse(passwordResetLinkValidator.isValid(request));
	}
	
	@Test
	public void testIsValidReturnsTrue() throws Exception {
		PasswordResetRequest request = new PasswordResetRequest();
		DateTime requestDate = new DateTime();
		requestDate = requestDate.plusHours(passwordLinkValidity - 2);
		request.setRequestDate(requestDate);
		
		assertTrue(passwordResetLinkValidator.isValid(request));
	}

}
