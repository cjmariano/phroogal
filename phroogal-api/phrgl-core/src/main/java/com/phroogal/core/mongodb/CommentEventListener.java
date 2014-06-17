/**
 * 
 */
package com.phroogal.core.mongodb;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

import com.mongodb.DBObject;
import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.Comment;
import com.phroogal.core.domain.Reply;
import com.phroogal.core.repository.AnswerRepository;
import com.phroogal.core.repository.ReplyRepository;

/**
 * Intercepts changes in {@link Comment} and provide hooks for processes to executed on these
 * events 
 * @author Christopher Mariano
 *
 */
@Component
public class CommentEventListener extends AbstractMongoEventListener<Comment> {

	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Override
	public void onAfterSave(Comment source, DBObject dbo) {
		super.onAfterSave(source, dbo);
		updateAnswerMetaData(source);
	}
	
	@Override
	public void onBeforeDelete(DBObject dbo) {
		super.onBeforeDelete(dbo);
		deleteAssociatedReplies(dbo);
	}

	private void updateAnswerMetaData(Comment comment) {
		Answer answer = answerRepository.findOne(comment.getAnswerRef());
		if (answer != null && isNew(comment)) {
			int commentCount = answer.getTotalCommentCount();
			answer.setTotalCommentCount(commentCount + 1);
			answerRepository.save(answer);
		}
	}

	//TODO: Maybe extract to be reusable? -cjm
	private boolean isNew(Comment comment) {
		return comment.getCreatedOn().equals(comment.getModifiedOn());
	}
	
	private void deleteAssociatedReplies(DBObject dbo) {
		String id = dbo.get("id").toString();
		List<Reply> replies = replyRepository.findByCommentRef(new ObjectId(id));
		replyRepository.delete(replies);		
	}
}
