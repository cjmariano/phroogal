package com.phroogal.core.exception;



import org.junit.Assert;
import org.junit.Test;

public class ApplicationExceptionTest {
	
	private static final String TEST_ERROR_MSG = "Please use the social network associated with this email address.";
	
	private ApplicationException testException = new UserEmailIsSocialLoginException();

	@Test
	public void testGetErrorMessage() throws Exception {
		String errorMsg = testException.getErrorMessage().getMessage();
		Assert.assertTrue(TEST_ERROR_MSG.equals(errorMsg));
	}
}
