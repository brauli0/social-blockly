package es.udc.paproject.backend.rest.dtos;

import java.util.ArrayList;
import java.util.List;

import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UsersGroup;

public class GroupConversor {
	public GroupConversor() {}

	public static final GroupDto toGroupDto(UsersGroup usersGroup, List<User> users, Boolean iAmAdmin) {
		for (User u : users) {
			u.setGroupsRel(null);
			u.setPassword(null);
		}
		return new GroupDto(usersGroup.getId(), iAmAdmin, usersGroup.getGroupName(),
				usersGroup.getCreationDate(), usersGroup.getChatEnable(), users);
	}

	public static final List<GroupDto> toGroupDtos(List<UsersGroup> usersGroups) {
		List<GroupDto> groups = new ArrayList<>();
		for (UsersGroup g : usersGroups) {
			groups.add(new GroupDto(g.getId(), g.getGroupName(), g.getCreationDate(), g.getChatEnable(), null));
		}

		return groups;
	}

	public static final UsersGroup toUsersGroup() {
		return null;
	}
}
