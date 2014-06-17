/**
 * 
 */
package com.phroogal.core.mongodb;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

import com.mongodb.DBObject;
import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.ApplicationConstants;
import com.phroogal.core.domain.Question;
import com.phroogal.core.domain.QuestionThreadPost;
import com.phroogal.core.notification.Publisher;
import com.phroogal.core.repository.AnswerRepository;
import com.phroogal.core.service.QuestionService;
import com.phroogal.core.service.TagService;
import com.phroogal.core.utility.SequenceGenerator;

/**
 * Intercepts changes in {@link QuestionThreadPost} and provide hooks for processes to executed on these
 * events 
 * @author Christopher Mariano
 *
 */
@Component
public class QuestionEventListener extends AbstractMongoEventListener<Question> {

	@Autowired
	private SequenceGenerator sequenceGenerator;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	@Qualifier("core.onAddQuestionPublisher")
	private Publisher onAddQuestionPublisher;
	
	@Override
	public void onBeforeConvert(Question question) {
		super.onBeforeConvert(question);
		generateDocId(question);
		updateMetaData(question);
	}
	
	@Override
	public void onBeforeSave(Question question, DBObject dbo) {
		super.onBeforeSave(question, dbo);
		questionService.recalculateTrendingRank(question);
	}
	
	@Override
	public void onAfterSave(Question source, DBObject dbo) {
		super.onAfterSave(source, dbo);
		tagService.refreshTagMetadata(source.getTags());
		onAddQuestionPublisher.notify(source);
	}
	
	@Override
	public void onBeforeDelete(DBObject dbo) {
		super.onBeforeDelete(dbo);
		deleteAssociatedAnswers(dbo);
	}

	private void generateDocId(Question question) {
		if (question.getDocId() == 0L) {
			Long nextDocId = sequenceGenerator.getNextSequence(ApplicationConstants.COLLECTION_QUESTIONS);
			question.setDocId(nextDocId);			
		}
	}
	
	private void updateMetaData(Question question) {
		List<Answer> answers = question.getAnswers();
		if (answers != null && answers.size() > 0) {
			question.setAnswered(true);
			question.setTotalAnswerCount(answers.size());
			question.setTotalCommentCount(getTotalCommentCount(answers));	
		} else {
			question.setAnswered(false);
		}
	}

	private int getTotalCommentCount(List<Answer> answers) {
		int totalCommentCount = 0;
		for (Answer answer : answers) {
			if (answer.getComments() != null) {
				totalCommentCount += answer.getComments().size();	
			}
		}
		return totalCommentCount;
	}
	
	private void deleteAssociatedAnswers(DBObject dbo) {
		String id = dbo.get("id").toString();
		List<Answer> answers = answerRepository.findByQuestionRef(new ObjectId(id));
		answerRepository.delete(answers);
	}
}
