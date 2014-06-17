/**
 * 
 */
package com.phroogal.core.validator;

import static com.phroogal.core.domain.security.UserRoleType.ADMIN;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.Post;
import com.phroogal.core.domain.User;
import com.phroogal.core.service.AuthenticationDetailsService;

/**
 * Validates if any instance of {@link Post} can still be deleted
 * @author Christopher Mariano
 *
 */
@Service(value="deletePostValidator")
public class DeletePostsValidator implements Validator<Post> {

	@Value("${post.delete.validity.minutes}")
	private int deletePostValidityInMinutes;
	
	@Autowired
	private AuthenticationDetailsService<String> authenticationService;
	
	@Override
	public boolean isValid(Post post) { 
		if (post != null) {
			DateTime createdDate = post.getCreatedOn();
			DateTime deadlineDate = createdDate.plusMinutes(deletePostValidityInMinutes);
			User principal = authenticationService.getAuthenticatedUser();
			if ( userHasAdminRole(principal) || DateTime.now().isBefore(deadlineDate)) {
				return true;
			}	
		}
		return false;
	}                                                                                     

	private boolean userHasAdminRole(User principal) {
		return principal != null && principal.hasRole(ADMIN);
	}
}
