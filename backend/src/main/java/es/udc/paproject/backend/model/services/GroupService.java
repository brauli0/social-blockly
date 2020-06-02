package es.udc.paproject.backend.model.services;

import java.util.List;

import es.udc.paproject.backend.model.common.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UsersGroup;

public interface GroupService {
	UsersGroup createGroup(Long userId, String groupName) throws InstanceNotFoundException, PermissionException;

	void deleteGroup(Long groupId) throws InstanceNotFoundException;

	UsersGroup getGroup(Long groupId) throws InstanceNotFoundException;

	UsersGroup updateGroup(Long groupId, String groupName, Boolean chatEnable) throws InstanceNotFoundException;

	List<User> getAllUsersByGroup(Long groupId) throws InstanceNotFoundException;

	List<UsersGroup> getAllGroupsByUser(Long userId) throws InstanceNotFoundException;

	void addMember(Long userId, Long groupId)
			throws InstanceNotFoundException, DuplicateInstanceException;

	void removeMember(Long userId, Long groupId)
			throws InstanceNotFoundException, DuplicateInstanceException;
}
