package com.phroogal.core.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.Comment;
import com.phroogal.core.domain.Reply;
import com.phroogal.core.repository.ReplyRepository;
import com.phroogal.core.service.CommentService;
import com.phroogal.core.service.ReplyService;

/**
 * Default implementation of the {@link Reply} interface
 * @author Christopher Mariano
 *
 */
@Service
public class ReplyServiceImpl extends BaseService<Reply, ObjectId, ReplyRepository> implements ReplyService{

	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private CommentService commentService;
	
	@Override
	protected ReplyRepository getRepository() {
		return replyRepository;
	}
	
	@Override
	protected boolean onBeforeSaveOrUpdate(Reply reply) {
		Comment comment = commentService.findById(reply.getCommentRef());
		reply.setRootQuestionId(comment.getRootQuestionId());
		return true;
	}

	@Override
	public List<Reply> getByCommentRef(ObjectId commentRef) {
		return replyRepository.findByCommentRef(commentRef);
	}
}
