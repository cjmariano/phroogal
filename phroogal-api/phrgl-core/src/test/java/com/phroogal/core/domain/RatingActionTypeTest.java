package com.phroogal.core.domain;

import static com.phroogal.core.domain.RatingActionType.DOWNVOTE;

import org.junit.Assert;
import org.junit.Test;

public class RatingActionTypeTest {

	@Test
	public void testGet() throws Exception {
		Assert.assertEquals(RatingActionType.get("upvote"), RatingActionType.UPVOTE);
		Assert.assertEquals(RatingActionType.get("downvote"), RatingActionType.DOWNVOTE);
		Assert.assertEquals(RatingActionType.get("UPVOTE"), RatingActionType.UPVOTE);
		Assert.assertEquals(RatingActionType.get("DOWNVOTE"), RatingActionType.DOWNVOTE);
	}

	@Test
	public void testGetValue() throws Exception {
		Assert.assertEquals("upvote", RatingActionType.UPVOTE.getValue());
		Assert.assertEquals("downvote", DOWNVOTE.getValue());
	}

}
