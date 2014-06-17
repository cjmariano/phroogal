package com.phroogal.core.domain;


import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.phroogal.core.repository.TestEntityGenerator;

public class AnswerTest {

	@Test
	public void testApplyVote() throws Exception {
		TestEntityGenerator generator = new TestEntityGenerator();
		ObjectId userId = ObjectId.get();
		Answer answer = generator.generateTestAnswer();
		VoteActionType voteAction = mock(VoteActionType.class);
		answer.applyVote(userId, voteAction);
		
		verify(voteAction, atLeastOnce()).applyTo(userId, answer.getVotes());
	}

}
