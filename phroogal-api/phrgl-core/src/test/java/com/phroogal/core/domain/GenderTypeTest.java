package com.phroogal.core.domain;

import org.junit.Assert;
import org.junit.Test;

public class GenderTypeTest {

	@Test
	public void testGet() throws Exception {
		Assert.assertEquals(GenderType.get("male"), GenderType.MALE);
		Assert.assertEquals(GenderType.get("female"), GenderType.FEMALE);
		Assert.assertEquals(GenderType.get("MALE"), GenderType.MALE);
		Assert.assertEquals(GenderType.get("FEMALE"), GenderType.FEMALE);
	}

	@Test
	public void testGetValue() throws Exception {
		Assert.assertEquals("Male", GenderType.MALE.getValue());
		Assert.assertEquals("Female", GenderType.FEMALE.getValue());
	}

}
