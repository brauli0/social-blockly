package es.udc.paproject.backend.rest.controllers;

import static es.udc.paproject.backend.rest.dtos.GroupConversor.toGroupDto;
import static es.udc.paproject.backend.rest.dtos.GroupConversor.toGroupDtos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.udc.paproject.backend.model.common.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UsersGroup;
import es.udc.paproject.backend.model.entities.UsersGroupRel;
import es.udc.paproject.backend.model.services.GroupService;
import es.udc.paproject.backend.model.services.PermissionException;
import es.udc.paproject.backend.rest.dtos.GroupDto;
import es.udc.paproject.backend.rest.dtos.GroupIdDto;
import es.udc.paproject.backend.rest.dtos.GroupParamsDto;
import es.udc.paproject.backend.rest.dtos.GroupParamsDto2;
import es.udc.paproject.backend.rest.dtos.MemberParamsDto;

@RestController
@RequestMapping("/groups")
public class GroupController {

	@Autowired
	private GroupService groupService;

	private boolean isAdmin(Long groupId, Long userId)
			throws InstanceNotFoundException, PermissionException {
		boolean isAdmin = false;
		UsersGroup group = groupService.getGroup(groupId);
		List<UsersGroupRel> rels = group.getUsersRel();
		for (UsersGroupRel rel : rels) {
			if (rel.getUser().getId() == userId && rel.getIsAdmin()) {
				isAdmin = true;
			}
		}
		return isAdmin;
	}

	@PostMapping("/create")
	public GroupDto createGroup(@RequestAttribute Long userId,
			@RequestBody GroupParamsDto groupParamsDto)
			throws InstanceNotFoundException, PermissionException {

		if (!groupParamsDto.getUserId().equals(userId)) {
		 	throw new PermissionException();
		}

		UsersGroup usersGroup = groupService.createGroup(groupParamsDto.getUserId(),
				groupParamsDto.getGroupName());

		return toGroupDto(usersGroup, groupService.getAllUsersByGroup(usersGroup.getId()), true);
	}

	@PostMapping("/delete")
	public void deleteGroup(@RequestAttribute Long userId, @RequestBody GroupIdDto groupIdDto)
			throws InstanceNotFoundException, PermissionException {

		if (!isAdmin(groupIdDto.getGroupId(), userId)) {
			throw new PermissionException();
		}

		groupService.deleteGroup(groupIdDto.getGroupId());
	}

	@GetMapping("/{groupId}")
	public GroupDto getGroup(@RequestAttribute Long userId, @PathVariable Long groupId)
			throws InstanceNotFoundException, PermissionException {

		List<User> members = groupService.getAllUsersByGroup(groupId);
		boolean isMember = false;
		for (User user : members) {
			if (user.getId() == userId) {
				isMember = true;
			}
		}
		if (!isMember) {
			throw new PermissionException();
		}

		boolean iAmAdmin = isAdmin(groupId, userId);

		return toGroupDto(groupService.getGroup(groupId), groupService.getAllUsersByGroup(groupId), iAmAdmin);
	}

	@PutMapping("/update")
	public GroupDto updateGroup(@RequestAttribute Long userId,
			@RequestBody GroupParamsDto2 groupParamsDto2)
			throws InstanceNotFoundException, PermissionException {

		if (!isAdmin(groupParamsDto2.getGroupId(), userId)) {
			throw new PermissionException();
		}

		return toGroupDto(
				groupService.updateGroup(groupParamsDto2.getGroupId(), groupParamsDto2.getGroupName(),
						groupParamsDto2.getChatEnable()),
				groupService.getAllUsersByGroup(groupParamsDto2.getGroupId()), true);
	}

//	getAllUsersByGroup => getGroup

	@GetMapping("/allgroups/{id}")
	public List<GroupDto> getAllGroupsByUser(@PathVariable Long id)
			throws InstanceNotFoundException {

		// IT DOESN'T RETURN USERNAMES
		return toGroupDtos(groupService.getAllGroupsByUser(id));
	}

	@PutMapping("/addmember")
	public void addMember(@RequestAttribute Long userId,
			@RequestBody MemberParamsDto memberParamsDto)
			throws InstanceNotFoundException, DuplicateInstanceException, PermissionException {

		if (!isAdmin(memberParamsDto.getGroupId(), userId)) {
			throw new PermissionException();
		}

		groupService.addMember(memberParamsDto.getUserId(), memberParamsDto.getGroupId());
	}

	@PutMapping("/removemember")
	public void removeMember(@RequestAttribute Long userId,
			@RequestBody MemberParamsDto memberParamsDto)
			throws InstanceNotFoundException, DuplicateInstanceException, PermissionException {

		if (!isAdmin(memberParamsDto.getGroupId(), userId)) {
			// User is not the administrator but wants to leave the group
			if (userId != memberParamsDto.getUserId()) {
				throw new PermissionException();
			}
		}

		groupService.removeMember(memberParamsDto.getUserId(), memberParamsDto.getGroupId());
	}
}
