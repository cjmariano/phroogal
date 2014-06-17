/**
 * 
 */
package com.phroogal.core.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.Comment;
import com.phroogal.core.domain.Question;
import com.phroogal.core.domain.QuestionThreadPost;
import com.phroogal.core.domain.Reply;
import com.phroogal.core.repository.QuestionRepository;
import com.phroogal.core.service.AnswerService;
import com.phroogal.core.service.CommentService;
import com.phroogal.core.service.QuestionService;
import com.phroogal.core.service.QuestionThreadPostService;
import com.phroogal.core.service.ReplyService;

/**
 * Default implementation of the {@link QuestionThreadPostService} interface.
 * @author Christopher Mariano
 *
 */
@Service
public class QuestionThreadPostServiceImpl implements QuestionThreadPostService {
	
	@Autowired
	private QuestionService questionService; 
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private AnswerService answerService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private ReplyService replyService;

	/**
	 * Recreates a question thread of its associated answers, comments and replies and embeds them unto the
	 * {@link QuestionThreadPost} document
	 * @param questionService for updating the question
	 * @param post - updated question post
	 */
	public void refreshQuestionAnswerThread(QuestionThreadPost post) {
		if (! (post instanceof Question)) {
			ObjectId rootQuestionId = post.getRootQuestionId();
			if (rootQuestionId != null) {
				Question question = returnCompleteQuestionThread(rootQuestionId);
				if (question != null) {
					questionService.saveOrUpdate(question);	
				}
			}	
		}
	}
	
	private Question returnCompleteQuestionThread(ObjectId id) {
		Question question = questionRepository.findOne(id);
		if (question != null) {
			attachAnswersToQuestion(question);	
		}
		return question;
	}
	
	private void attachAnswersToQuestion(Question question) {
		List<Answer> answers = answerService.getByQuestionRef(question.getId());
		attachCommentsToAnswers(answers);
		question.setAnswers(answers);
	}
	
	private void attachCommentsToAnswers(List<Answer> answers) {
		for (Answer answer : answers) {
			List<Comment> comments = commentService.getByAnswerRef(answer.getId());
			attachRepliesToComments(comments);
			answer.setComments(comments);
		}
	}

	private void attachRepliesToComments(List<Comment> comments) {
		for (Comment comment : comments) {
			List<Reply> replies = replyService.getByCommentRef(comment.getId());
			comment.setReplies(replies);
		}
	}
}
