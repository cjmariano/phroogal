/**
 * 
 */
package com.phroogal.core.rule;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Holds the context (i.e. properties) that would be included in the Drools knowledgebase
 * @author Christopher Mariano
 *
 */
@Component("rulesContext")
public class RulesContext implements Fact {

	/**
	 * ======================================================================
	 * Holds the weight of a user's (who posted the answer) social network in 
	 * terms of sorting answers down the answer hierarchy tree.
	 * ======================================================================
	 */
	@Value(value="${answer.hierarchy.socialNetwork.weight.facebook}")
	public int answerWeightFacebook;
	
	@Value(value="${answer.hierarchy.socialNetwork.weight.twitter}")
	public int answerWeightTwitter;
	
	@Value(value="${answer.hierarchy.socialNetwork.weight.google}")
	public int answerWeightGoogle;
	
	@Value(value="${answer.hierarchy.socialNetwork.weight.linkedin}")
	public int answerWeightLinkedIn;
	
	/**
	 * ======================================================================
	 * Holds the weight of a user's (who posted the answer) location in terms 
	 * of sorting answers down the answer hierarchy tree.
	 * ======================================================================
	 */
	@Value(value="${answer.hierarchy.location.weight}")
	public int answerWeightLocation;
	
	/**
	 * ======================================================================
	 * Holds the weight of the collective user's vote in terms 
	 * of sorting answers down the answer hierarchy tree.
	 * ======================================================================
	 */
	@Value(value="${answer.hierarchy.votes.weight}")
	public int answerWeightVotes;


	/**
	 * ======================================================================
	 * Getter methods
	 * ======================================================================
	 */
	

	public int getAnswerWeightFacebook() {
		return answerWeightFacebook;
	}
	
	public int getAnswerWeightTwitter() {
		return answerWeightTwitter;
	}

	public int getAnswerWeightGoogle() {
		return answerWeightGoogle;
	}

	public int getAnswerWeightLinkedIn() {
		return answerWeightLinkedIn;
	}

	public int getAnswerWeightLocation() {
		return answerWeightLocation;
	}

	public int getAnswerWeightVotes() {
		return answerWeightVotes;
	}
}
