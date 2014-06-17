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
import com.phroogal.core.domain.Comment;
import com.phroogal.core.notification.Publisher;
import com.phroogal.core.repository.CommentRepository;

/**
 * Intercepts changes in {@link Answer} and provide hooks for processes to executed on these
 * events 
 * @author Christopher Mariano
 *
 */
@Component
public class AnswerEventListener extends AbstractMongoEventListener<Answer> {

	@Autowired
	@Qualifier("core.onAddAnswerPublisher")
	private Publisher onAddAnswerPublisher; 
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Override
	public void onAfterSave(Answer source, DBObject dbo) {
		super.onAfterSave(source, dbo);
		onAddAnswerPublisher.notify(source);
	}
	
	@Override
	public void onBeforeDelete(DBObject dbo) {
		super.onBeforeDelete(dbo);
		deleteAssociatedComments(dbo);
	}

	private void deleteAssociatedComments(DBObject dbo) {
		String id = dbo.get("id").toString();
		List<Comment> comments = commentRepository.findByAnswerRef(new ObjectId(id));
		commentRepository.delete(comments);
	}
}
