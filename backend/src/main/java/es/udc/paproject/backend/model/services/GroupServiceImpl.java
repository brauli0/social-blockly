package es.udc.paproject.backend.model.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.paproject.backend.model.common.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.User.RoleType;
import es.udc.paproject.backend.model.entities.UserDao;
import es.udc.paproject.backend.model.entities.UsersGroup;
import es.udc.paproject.backend.model.entities.UsersGroupDao;
import es.udc.paproject.backend.model.entities.UsersGroupRel;
import es.udc.paproject.backend.model.entities.UsersGroupRelDao;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UsersGroupDao usersGroupDao;

	@Autowired
	private UsersGroupRelDao usersGroupRelDao;

	@Override
	public UsersGroup createGroup(Long userId, String groupName) throws InstanceNotFoundException, PermissionException {
		Optional<User> opUser = userDao.findById(userId);
		if (!opUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}
		User user = opUser.get();

		if (user.getRole() != RoleType.TEACHER) {
			throw new PermissionException();
		}

		UsersGroup usersGroup = new UsersGroup(groupName);
		UsersGroup savedGroup = usersGroupDao.save(usersGroup);

		UsersGroupRel usersGroupRel = new UsersGroupRel(user, savedGroup, true);

		List<UsersGroupRel> groupList = new ArrayList<>();
		groupList.add(usersGroupRel);
		savedGroup.setUsersRel(groupList);

		List<UsersGroupRel> userList = (user.getGroupsRel() == null) ? new ArrayList<>() : user.getGroupsRel();
		userList.add(usersGroupRel);
		user.setGroupsRel(userList);

//		This code should not be necessary
		usersGroupRelDao.save(usersGroupRel);
//		-----

		return savedGroup;
	}

	@Override
	public void deleteGroup(Long groupId) throws InstanceNotFoundException {
		Optional<UsersGroup> group = usersGroupDao.findById(groupId);
		if (group.isPresent())
			usersGroupDao.delete(group.get());
		else
			throw new InstanceNotFoundException("project.entities.usersGroup", groupId);
	}

	@Override
	public UsersGroup getGroup(Long groupId) throws InstanceNotFoundException {
		Optional<UsersGroup> group = usersGroupDao.findById(groupId);
		if (group.isPresent())
			return group.get();
		else
			throw new InstanceNotFoundException("project.entities.usersGroup", groupId);
	}

	@Override
	public UsersGroup updateGroup(Long groupId, String groupName, Boolean chatEnable)
			throws InstanceNotFoundException {

		Optional<UsersGroup> optionalGroup = usersGroupDao.findById(groupId);

		if (!optionalGroup.isPresent()) {
			throw new InstanceNotFoundException("project.entities.usersGroup", groupId);
		}

		UsersGroup group = optionalGroup.get();
		group.setGroupName(groupName);
		group.setChatEnable(chatEnable);

		return group;
	}

	@Override
	public List<User> getAllUsersByGroup(Long groupId) throws InstanceNotFoundException {
//		return usersGroupRelDaoImpl.getAllUsersByGroupId(groupId);

		Optional<UsersGroup> opGroup = usersGroupDao.findById(groupId);
		if (!opGroup.isPresent()) {
			throw new InstanceNotFoundException("project.entities.usersGroup", groupId);
		}

		List<UsersGroupRel> list = opGroup.get().getUsersRel();
		List<User> users = new ArrayList<>();

		for (UsersGroupRel r : list) {
			users.add(r.getUser());
		}

		return users;
	}

	@Override
	public List<UsersGroup> getAllGroupsByUser(Long userId) throws InstanceNotFoundException {
//		return usersGroupRelDaoImpl.getAllGroupsByUserId(userId);

		Optional<User> opUser = userDao.findById(userId);
		if (!opUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}

		List<UsersGroupRel> list = opUser.get().getGroupsRel();
		List<UsersGroup> groups = new ArrayList<>();

		for (UsersGroupRel r : list) {
			groups.add(r.getUsersGroup());
		}

		return groups;
	}

	@Override
	public void addMember(Long userId, Long groupId)
			throws InstanceNotFoundException, DuplicateInstanceException {
		Optional<User> optionalUser = userDao.findById(userId);

		if (!optionalUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}

		Optional<UsersGroup> optionalGroup = usersGroupDao.findById(groupId);

		if (!optionalGroup.isPresent()) {
			throw new InstanceNotFoundException("project.entities.usersGroup", userId);
		}

		User user = optionalUser.get();
		UsersGroup group = optionalGroup.get();

		UsersGroupRel usersGroupRel = new UsersGroupRel(user, group, false);

		List<UsersGroupRel> groupList = (group.getUsersRel() == null) ? new ArrayList<>() : group.getUsersRel();
		if (groupList.size() > 0) {
			for (UsersGroupRel r : groupList) {
				if (r.getUser().equals(user) && r.getUsersGroup().equals(group)) {
					throw new DuplicateInstanceException("project.entities.usersGroupRel", user.getUserName());
				}
			}
		}
		groupList.add(usersGroupRel);
		group.setUsersRel(groupList);

		List<UsersGroupRel> userList = (user.getGroupsRel() == null) ? new ArrayList<>() : user.getGroupsRel();
		userList.add(usersGroupRel);
		user.setGroupsRel(userList);

//		This code should not be necessary
		usersGroupRelDao.save(usersGroupRel);
//		-----
	}

	@Override
	public void removeMember(Long userId, Long groupId)
			throws InstanceNotFoundException, DuplicateInstanceException {
		Optional<User> optionalUser = userDao.findById(userId);

		if (!optionalUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}

		Optional<UsersGroup> optionalGroup = usersGroupDao.findById(groupId);

		if (!optionalGroup.isPresent()) {
			throw new InstanceNotFoundException("project.entities.usersGroup", groupId);
		}

		User user = optionalUser.get();
		UsersGroup group = optionalGroup.get();

		if (group.getUsersRel() == null) {
			throw new InstanceNotFoundException("project.entities.usersGroupRel - 1", userId);
		}

		if (user.getGroupsRel() == null) {
			throw new InstanceNotFoundException("project.entities.usersGroupRel - 2", userId);
		}


		List<UsersGroupRel> groupList = group.getUsersRel();
		List<UsersGroupRel> userList = user.getGroupsRel();

		int groupAux = 0, groupIndex = -1, userAux = 0, userIndex = -1;

		for (UsersGroupRel r : groupList) {
			if (r.getUser().equals(user) && r.getUsersGroup().equals(group))  {
				groupIndex = groupAux;
				break;
			}
			groupAux++;
		}

		for (UsersGroupRel r : userList) {
			if (r.getUser().equals(user) && r.getUsersGroup().equals(group))  {
				userIndex = userAux;
				break;
			}
			userAux++;
		}

		if (groupIndex == -1) {
			throw new InstanceNotFoundException("project.entities.usersGroupRel - 3", userId);
		}

		if (userIndex == -1) {
			throw new InstanceNotFoundException("project.entities.usersGroupRel - 4 ", userId);
		}

		groupList.remove(groupIndex);
		userList.remove(userIndex);

//		This code should not be necessary
		usersGroupRelDao.delete(new UsersGroupRel(user, group));
//		-----
	}
}
