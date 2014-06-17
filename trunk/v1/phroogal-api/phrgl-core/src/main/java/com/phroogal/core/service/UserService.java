/**
 * 
 */
package com.phroogal.core.service;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.springframework.social.connect.Connection;
import org.springframework.web.multipart.MultipartFile;

import com.phroogal.core.domain.User;
import com.phroogal.core.exception.InvalidPasswordException;
import com.phroogal.core.exception.UserEmailNotFoundException;

/**
 * Service for UserProfile functions
 * @author Christopher Mariano
 *
 */
public interface UserService extends Service<User, ObjectId> {
	
	/**
	 * Retrieves a user given the userName
	 * @param username associated with the user
	 * @return the user with the given userName
	 */
	public User getUserByUserName(String username) throws UserEmailNotFoundException;
	
	/**
	 * Determines if an email is associated with a social network
	 * @param email of the user
	 * @return true if the email associated with a user is signed in via Social Network
	 */
	public boolean isEmailAssociatedWithSocialLogin(String email);
	
	/**
	 * Creates an instance of {@link User} from a social connection
	 * @param connection - social network connection
	 * @return {@link User} created from social connection
	 */
	public User getUserFromConnection(Connection<?> connection);
	
	/**
	 * Uploads the profile picture of a user given the user id, and saves the picture url
	 * @param userId - id of the user whose picture is to be updated
	 * @param file - A representation of an uploaded file received in a multipart request.
	 * @return {@link User} with updated profile picture 
	 * @throws IOException 
	 */
	public User updateProfilePicture(ObjectId userId, MultipartFile file) throws IOException;

	/**
	 * Updates a user password to the given new password. Old password must match the existing password in order for password to be updated
	 * @param id - of the user whose password is being changed
	 * @param oldPassword - to be changed
	 * @param newPassword - that the password would be updated to
	 * @throws InvalidPasswordException if the old password given doesn't match the old one.
	 */
	public void changePassword(ObjectId id, String oldPassword, String newPassword) throws InvalidPasswordException;
	
	/**
	 * Forces an update on a user's password to the new password.
	 * @param id - of the user whose password is being changed
	 * @param newPassword - that the password would be updated to
	 */
	public void changePassword(ObjectId id, String newPassword);

}
