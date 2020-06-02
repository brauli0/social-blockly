package es.udc.paproject.backend.model.services;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.paproject.backend.model.common.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;
import es.udc.paproject.backend.model.entities.UserDaoSearchImpl;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserDaoSearchImpl userDaoSearchImpl;

	@Override
	public void signUp(User user) throws DuplicateInstanceException {

		if (userDao.existsByUserName(user.getUserName())) {
			throw new DuplicateInstanceException("project.entities.user", user.getUserName());
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		userDao.save(user);

	}

	@Override
	@Transactional(readOnly=true)
	public User login(String userName, String password) throws IncorrectLoginException {

		Optional<User> opUser = userDao.findByUserName(userName);

		if (!opUser.isPresent()) {
			throw new IncorrectLoginException(userName, password);
		}
		User user = opUser.get();

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new IncorrectLoginException(userName, password);
		}

		return user;

	}

	@Override
	public User saveLastLoginDate(Long userId) throws InstanceNotFoundException {
		Optional<User> opUser = userDao.findById(userId);
		if (!opUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}
		User user = opUser.get();

		user.setLastLoginDate(Calendar.getInstance());

		return user;
	}

	@Override
	@Transactional(readOnly=true)
	public User loginFromId(Long id) throws InstanceNotFoundException {
		return permissionChecker.checkUser(id);
	}

	@Override
	public User updateProfile(Long id, String firstName, String lastName, String email) throws InstanceNotFoundException {

		User user = permissionChecker.checkUser(id);

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);

		return user;

	}

	@Override
	public void changePassword(Long id, String oldPassword, String newPassword)
		throws InstanceNotFoundException, IncorrectPasswordException {

		User user = permissionChecker.checkUser(id);

		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new IncorrectPasswordException();
		} else {
			user.setPassword(passwordEncoder.encode(newPassword));
		}

	}

	@Override
	public User getUserInfo(Long id) throws InstanceNotFoundException {

		User user = permissionChecker.checkUser(id);

		return user;

	}

	@Override
	public User getUserInfo(String username) throws InstanceNotFoundException {
		User user;
		try {
			user = userDaoSearchImpl.getUserInfo(username);
		} catch (NoResultException e) {
			throw new InstanceNotFoundException("project.entities.user", username);
		}

		return user;
	}

	@Override
	public List<User> getUsersByKeyword(String keyword) {
		return userDaoSearchImpl.getUsersByKeyword(keyword);
	}
}
