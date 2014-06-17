package com.phroogal.core.domain;

import java.util.Set;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VoteActionTypeTest {
	
	private Votes votes;
	
	@Before
	public void setUp() {
		votes = new Votes();
	}

	@Test
	public void testWhenUpvoteListShouldIncrement() throws Exception {
		VoteActionType voteAction = VoteActionType.UPVOTE;
		ObjectId userId1 = ObjectId.get();
		voteAction.applyTo(userId1, votes);
		Assert.assertEquals(1, votes.getTotal());
		Assert.assertEquals(1, votes.getUserUpvotes().size());
		
		ObjectId userId2 = ObjectId.get();
		voteAction.applyTo(userId2, votes);
		Assert.assertEquals(2, votes.getTotal());
		Assert.assertEquals(2, votes.getUserUpvotes().size());
	}
	
	@Test
	public void testWhenUpvoteShouldRemoveFromDownvoteList() throws Exception {
		VoteActionType voteActionUp = VoteActionType.UPVOTE;
		VoteActionType VoteActionDown = VoteActionType.DOWNVOTE;
		ObjectId userId1 = ObjectId.get();
		VoteActionDown.applyTo(userId1, votes);
		voteActionUp.applyTo(userId1, votes);
		Set<ObjectId> userDownvotes = votes.getUserDownvotes();
		if (userDownvotes.contains(userId1)) 
		{
			Assert.assertEquals(0, votes.getUserDownvotes().size());
		}
		
	}
	
	@Test
	public void testWhenDownvoteListShouldIncrement() throws Exception {
		VoteActionType voteAction = VoteActionType.DOWNVOTE;
		ObjectId userId1 = ObjectId.get();
		voteAction.applyTo(userId1, votes);
		Assert.assertEquals(0, votes.getTotal());
		Assert.assertEquals(1, votes.getUserDownvotes().size());	
		
		ObjectId userId2 = ObjectId.get();
		voteAction.applyTo(userId2, votes);
		Assert.assertEquals(2, votes.getUserDownvotes().size());			
		
		//if total votes is negative, return 0
		Assert.assertEquals(0, votes.getTotal()); 		
	}
	
	@Test
	public void testWhenDownvoteShouldRemoveFromUpvoteList() throws Exception {
		VoteActionType voteActionUp = VoteActionType.UPVOTE;
		VoteActionType VoteActionDown = VoteActionType.DOWNVOTE;
		ObjectId userId1 = ObjectId.get();
		VoteActionDown.applyTo(userId1, votes);
		voteActionUp.applyTo(userId1, votes);
		Set<ObjectId> userUpvotes = votes.getUserUpvotes();
		if (userUpvotes.contains(userId1)) 
		{
			Assert.assertEquals(0, votes.getUserUpvotes().size());
		}		
		
	}

}
