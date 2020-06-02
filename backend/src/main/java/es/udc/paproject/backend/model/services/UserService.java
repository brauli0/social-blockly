package es.udc.paproject.backend.model.services;

import java.util.List;

import es.udc.paproject.backend.model.common.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.User;

public interface UserService {

	void signUp(User user) throws DuplicateInstanceException;

	User login(String userName, String password) throws IncorrectLoginException;

	User saveLastLoginDate(Long userId) throws InstanceNotFoundException;

	User loginFromId(Long id) throws InstanceNotFoundException;

	User updateProfile(Long id, String firstName, String lastName, String email)
			throws InstanceNotFoundException;

	void changePassword(Long id, String oldPassword, String newPassword)
		throws InstanceNotFoundException, IncorrectPasswordException;

	User getUserInfo(Long id) throws InstanceNotFoundException;

	User getUserInfo(String username) throws InstanceNotFoundException;

	List<User> getUsersByKeyword(String keyword);
}
