package com.phroogal.core.service.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phroogal.core.domain.User;
import com.phroogal.core.exception.InvalidPasswordException;
import com.phroogal.core.exception.UserEmailNotFoundException;
import com.phroogal.core.repository.UserRepository;
import com.phroogal.core.service.FileUploadService;
import com.phroogal.core.service.UserService;
import com.phroogal.core.social.connect.MongoUsersConnectionRepository;

/**
 * Default implementation of the {@link UserService} interface
 * @author Christopher Mariano
 *
 */
@Service(value="userService")
public class UserServiceImpl extends BaseService<User, ObjectId, UserRepository> implements UserService, UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MongoUsersConnectionRepository usersConnectionRepository;
	
	@Autowired
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Override
	protected UserRepository getRepository() {
		return userRepository;
	}
	
	@Override
	public User getUserByUserName(String username) throws UserEmailNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user != null) {
			return user;
		}
		throw new UserEmailNotFoundException();
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return getUserByUserName(email);
	}
	
	@Override
	public User getUserFromConnection(Connection<?> connection) {
		List<String> userIdsFromConnection = usersConnectionRepository.findUserIdsWithConnection(connection);
		if (userIdsFromConnection.size() > 0) {
			String userId = usersConnectionRepository.findUserIdsWithConnection(connection).get(0);
			return findById(new ObjectId(userId));	
		}
		return null;
	}
	
	@Override
	public boolean isEmailAssociatedWithSocialLogin(String email) {
		List<User> users = userRepository.findByProfileEmail(email);
		if (users != null && users.size() > 0) {
			for (Iterator<User> it = users.iterator(); it.hasNext();) {
				User user = (User) it.next();
				if (user.getPrimarySocialNetworkConnection() != null) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public User updateProfilePicture(ObjectId userId, MultipartFile file) throws IOException {
		User user = findById(userId);
		String profilePictureUrl = fileUploadService.uploadFile(generateFileKeyName(user), file);
		user.getProfile().setProfilePictureUrl(profilePictureUrl);
		//TODO: Try to get a smaller sized picture for this one -cjm
		user.getProfile().setProfileSmallPictureUrl(profilePictureUrl);
		return saveOrUpdate(user);
	}
	

	@Override
	public void changePassword(ObjectId userId, String oldPassword, String newPassword) throws InvalidPasswordException {
		User user = findById(userId);
		if (user != null && passwordEncoder.matches(oldPassword, user.getPassword())) {
			String encryptedPassword = passwordEncoder.encode(newPassword);
			user.getProfile().setPassword(encryptedPassword);
			saveOrUpdate(user);
			return;
		};
		throw new InvalidPasswordException();
	}
	
	@Override
	public void changePassword(ObjectId userId, String newPassword) {
		User user = findById(userId);
		if (user != null) {
			String encryptedPassword = passwordEncoder.encode(newPassword);
			user.getProfile().setPassword(encryptedPassword);
			saveOrUpdate(user);
		}
	}

	private String generateFileKeyName(User user) {
		StringBuffer sb = new StringBuffer(user.getId().toString());
		sb.append("-profile-pic");
		return sb.toString();
	}
}
