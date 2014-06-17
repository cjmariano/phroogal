package com.phroogal.core.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.Comment;
import com.phroogal.core.repository.CommentRepository;
import com.phroogal.core.service.AnswerService;
import com.phroogal.core.service.CommentService;

/**
 * Default implementation of the {@link Comment} interface
 * @author Christopher Mariano
 *
 */
@Service
public class CommentServiceImpl extends BaseService<Comment, ObjectId, CommentRepository> implements CommentService{

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private AnswerService answerService;
	
	@Override
	protected CommentRepository getRepository() {
		return commentRepository;
	}
	
	@Override
	protected boolean onBeforeSaveOrUpdate(Comment comment) {
		Answer answer = answerService.findById(comment.getAnswerRef());
		comment.setRootQuestionId(answer.getRootQuestionId());
		return true;
	}

	@Override
	public List<Comment> getByAnswerRef(ObjectId answerRef) {
		return commentRepository.findByAnswerRef(answerRef);
	}
}
