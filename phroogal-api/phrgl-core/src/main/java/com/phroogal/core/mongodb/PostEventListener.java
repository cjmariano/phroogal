/**
 * 
 */
package com.phroogal.core.mongodb;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

import com.mongodb.DBObject;
import com.phroogal.core.domain.Post;
import com.phroogal.core.domain.User;
import com.phroogal.core.service.UserService;

/**
 * Intercepts changes in {@link Post} and provide hooks for processes to executed on these
 * events 
 * @author Christopher Mariano
 *
 */
@Component
public class PostEventListener extends AbstractMongoEventListener<Post> {

	@Autowired
	private UserService userService;
	
	@Override
	public void onBeforeSave(Post post, DBObject dbo) {
		super.onBeforeSave(post, dbo);
		resolvePostByUser(post);
	}

	private void resolvePostByUser(Post post) {
		ObjectId postById = post.getPostById();
		if (postById != null) {
			User postBy = userService.findById(postById);
			post.setPostBy(postBy);	
		}
	}
}
