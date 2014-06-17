package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.URI_USER_CHANGE_PASSWORD_POST;
import static com.phroogal.web.context.WebApplicationContext.URI_USER_GET;
import static com.phroogal.web.context.WebApplicationContext.URI_USER_GET_ALL;
import static com.phroogal.web.context.WebApplicationContext.URI_USER_LOCATION_POST;
import static com.phroogal.web.context.WebApplicationContext.URI_USER_PARTIAL_POST;
import static com.phroogal.web.context.WebApplicationContext.URI_USER_POST;
import static com.phroogal.web.context.WebApplicationContext.URI_USER_REMOVE_SOCIAL_NETWORK;
import static com.phroogal.web.context.WebApplicationContext.URI_USER_SEARCH;
import static com.phroogal.web.context.WebApplicationContext.URI_USER_UPLOAD_PICTURE_POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.phroogal.core.domain.Location;
import com.phroogal.core.domain.User;
import com.phroogal.core.service.LocationService;
import com.phroogal.core.service.Service;
import com.phroogal.core.service.SocialContactService;
import com.phroogal.core.service.UserService;
import com.phroogal.core.social.SocialNetworkType;
import com.phroogal.web.bean.LocationBean;
import com.phroogal.web.bean.PasswordChangeBean;
import com.phroogal.web.bean.UserBean;
import com.phroogal.web.controller.rest.RestPatchRequest;

@Controller
public class UserController extends BasicController<User, UserBean, ObjectId> {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SocialContactService socialContactService;
	
	@Autowired
	private LocationService locationService;
	
	@Override
	protected Service<User, ObjectId> returnDomainService() {
		return userService;
	}
	
	@RequestMapping(value = URI_USER_POST, method = RequestMethod.POST)
	public @ResponseBody
	Object addUpdateUser(HttpServletRequest request, HttpServletResponse response, @RequestBody UserBean userProfileBean) {
		return super.addUpdateResource(request, response, userProfileBean);
	}
	
	@RequestMapping(value = URI_USER_PARTIAL_POST, method = RequestMethod.PATCH)
	public @ResponseBody Object doPatchResource(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response, @RequestBody List<RestPatchRequest> patchRequest) throws Exception {
		return super.doPatchResource(id, request, response, patchRequest);
	}
	
	@RequestMapping(value = URI_USER_GET, method = RequestMethod.GET)
	public @ResponseBody Object getUser(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response) {
		return super.getResource(id, request, response);
	}
	
	@RequestMapping(value = URI_USER_GET_ALL, method = RequestMethod.GET)
	public @ResponseBody Object getAllUsers(HttpServletRequest request, HttpServletResponse response) {
		return super.getAllResources(request, response);
	}
	
	@RequestMapping(value = URI_USER_GET_ALL, method = RequestMethod.GET, params={"pageAt", "pageSize"})
	public @ResponseBody
	Object getAllQuestions(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		return super.getAllResources(pageAt, pageSize, request, response);
	}
	
	@RequestMapping(value = URI_USER_SEARCH, method = GET, params="email")
	public @ResponseBody Object searchUsersByEmail(@RequestParam("email") String email, HttpServletRequest request, HttpServletResponse response) {
		User user = userService.getUserByUserName(email);
		return getObjectMapper().toBean(user, UserBean.class);
	}
	
	@RequestMapping(value = URI_USER_REMOVE_SOCIAL_NETWORK, method = RequestMethod.DELETE)
	public @ResponseBody Object disconnectSocialNetwork(@PathVariable ObjectId id, @PathVariable String providerId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = userService.findById(id);
		user.disconnectSocialProfile(SocialNetworkType.get(providerId));
		userService.saveOrUpdate(user);
		socialContactService.removeSocialNetworkContacts(user.getId(), SocialNetworkType.get(providerId));
		return getObjectMapper().toBean(user, UserBean.class);
	}
	
	@RequestMapping(value = URI_USER_LOCATION_POST, method = POST)
	public @ResponseBody Object addUpdateUserLocation(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response, @RequestBody LocationBean locationBean) {
		Location location = resolveUserLocation(locationBean);
		User user = updateUserLocation(id, location);
		return getObjectMapper().toBean(user, UserBean.class);
	}
	
	@RequestMapping(value = URI_USER_CHANGE_PASSWORD_POST, method = RequestMethod.POST)
	public @ResponseBody Object changePassword(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response, @RequestBody PasswordChangeBean passwordChangeDetails) throws Exception {
		String oldPassword = passwordChangeDetails.getOldPassword();
		String newPassword = passwordChangeDetails.getNewPassword();
  		userService.changePassword(id, oldPassword, newPassword);
  		return null;
	}
	
	@RequestMapping(value = URI_USER_UPLOAD_PICTURE_POST, method = RequestMethod.POST)
	public @ResponseBody Object   doUploadProfilePicture(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) throws Exception {
  		return userService.updateProfilePicture(id, file);
	}
	
	private Location resolveUserLocation(LocationBean locationBean) {
		Location location = locationService.getLocationByReference(locationBean.getLocationRef());
		if (location == null) {
			location = new Location();
			location.setDisplayName(locationBean.getDisplayName());
		}
		return location;
	}
	
	private User updateUserLocation(ObjectId id, Location location) {
		User user = userService.findById(id);
		user.getProfile().setLocation(location);
		return userService.saveOrUpdate(user);
	}
}
