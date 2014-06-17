/**
 * 
 */
package com.phroogal.core.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.mongodb.DBObject;
import com.phroogal.core.domain.QuestionThreadPost;
import com.phroogal.core.service.QuestionThreadPostService;

/**
 * Intercepts changes in {@link QuestionThreadPost} and provide hooks for processes to executed on these
 * events 
 * @author Christopher Mariano
 *
 */
@Component
public class QuestionThreadPostEventListener extends AbstractMongoEventListener<QuestionThreadPost> {

	@Autowired
	private QuestionThreadPostService questionThreadPostService;
	
	@Override
	@Async
	public void onAfterSave(QuestionThreadPost source, DBObject dbo) {
		super.onAfterSave(source, dbo);
		questionThreadPostService.refreshQuestionAnswerThread(source);
	}
}
