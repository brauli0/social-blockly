package es.udc.paproject.backend.test.model.services;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.udc.paproject.backend.model.common.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.User.RoleType;
import es.udc.paproject.backend.model.entities.UsersGroup;
import es.udc.paproject.backend.model.services.GroupService;
import es.udc.paproject.backend.model.services.PermissionException;
import es.udc.paproject.backend.model.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class GroupServiceTest {

	private final Long NON_EXISTENT_ID = new Long(-1);

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	private User createStudent(String userName) {
		return new User(userName, "password", "firstName", "lastName",
				userName + "@" + userName + ".com", Calendar.getInstance(), "STUDENT");
	}

	private User createTeacher(String userName) {
		return new User(userName, "password", "firstName", "lastName",
				userName + "@" + userName + ".com", Calendar.getInstance(), "TEACHER");
	}

	@Test
	public void testCreateAndGetGroup() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		User teacher = createTeacher("John");

		userService.signUp(teacher);

		User lTeacher = userService.loginFromId(teacher.getId());

		UsersGroup group = groupService.createGroup(lTeacher.getId(), "A new group");

		UsersGroup groupInBD = groupService.getGroup(group.getId());

		assertEquals(groupInBD, group);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testCreateGroupWithNonExistentUser() throws InstanceNotFoundException, PermissionException {
		groupService.createGroup(NON_EXISTENT_ID, "A new group");
	}

	@Test(expected = PermissionException.class)
	public void testStudentCreatingGroup() throws InstanceNotFoundException, PermissionException, DuplicateInstanceException {
		User user = createTeacher("John");
		userService.signUp(user);

		user.setRole(RoleType.STUDENT);

		groupService.createGroup(user.getId(), "A new group");
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentGroup() throws InstanceNotFoundException {
		groupService.getGroup(NON_EXISTENT_ID);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testDeleteGroup() throws InstanceNotFoundException, DuplicateInstanceException, PermissionException {
		User teacher = createTeacher("John");

		userService.signUp(teacher);

		User lTeacher = userService.loginFromId(teacher.getId());

		UsersGroup group = groupService.createGroup(lTeacher.getId(), "A new group");

		UsersGroup groupInBD = groupService.getGroup(group.getId());

		assertEquals(groupInBD, group);

		groupService.deleteGroup(groupInBD.getId());

		groupService.getGroup(group.getId());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testDeleteGroupNonExistentGroup() throws InstanceNotFoundException {
		groupService.deleteGroup(NON_EXISTENT_ID);
	}

	@Test
	public void testUpdateProgram() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		User teacher = createTeacher("John");

		userService.signUp(teacher);

		User lTeacher = userService.loginFromId(teacher.getId());

		UsersGroup group = groupService.createGroup(lTeacher.getId(), "A new group");

		UsersGroup groupInBD = groupService.getGroup(group.getId());

		assertEquals(groupInBD, group);

		groupService.updateGroup(groupInBD.getId(), "New name", true);

		groupInBD = groupService.getGroup(group.getId());

		assertEquals("New name", groupInBD.getGroupName());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateNonExistentProgram() throws DuplicateInstanceException, InstanceNotFoundException {
		groupService.updateGroup(NON_EXISTENT_ID, "New name", true);
	}

	@Test
	public void testAddMember() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		User teacher = createTeacher("John");

		userService.signUp(teacher);

		User lTeacher = userService.loginFromId(teacher.getId());

		UsersGroup group = groupService.createGroup(lTeacher.getId(), "A new group");

		UsersGroup groupInBD = groupService.getGroup(group.getId());

		assertEquals(groupInBD, group);

		User student = createStudent("Lucy");
		userService.signUp(student);
		User lStudent = userService.loginFromId(student.getId());

		groupService.addMember(lStudent.getId(), groupInBD.getId());

		groupInBD = groupService.getGroup(group.getId());

		assertEquals(2, groupInBD.getUsersRel().size());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testAddNonExistentMember() throws InstanceNotFoundException, DuplicateInstanceException, PermissionException {
		User teacher = createTeacher("John");
		userService.signUp(teacher);
		User lTeacher = userService.loginFromId(teacher.getId());
		UsersGroup group = groupService.createGroup(lTeacher.getId(), "A new group");

		groupService.addMember(NON_EXISTENT_ID, group.getId());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testAddMemberToNonExistentGroup() throws InstanceNotFoundException, DuplicateInstanceException {
		User teacher = createTeacher("John");
		userService.signUp(teacher);

		groupService.addMember(teacher.getId(), NON_EXISTENT_ID);
	}

	@Test
	public void testRemoveMember() throws InstanceNotFoundException, DuplicateInstanceException, PermissionException, PermissionException {
		User teacher = createTeacher("John");

		userService.signUp(teacher);

		User lTeacher = userService.loginFromId(teacher.getId());

		UsersGroup group = groupService.createGroup(lTeacher.getId(), "A new group");

		UsersGroup groupInBD = groupService.getGroup(group.getId());

		assertEquals(groupInBD, group);

		User student = createStudent("Lucy");
		userService.signUp(student);
		User lStudent = userService.loginFromId(student.getId());

		groupService.addMember(lStudent.getId(), groupInBD.getId());

		groupInBD = groupService.getGroup(group.getId());

		assertEquals(2, groupInBD.getUsersRel().size());

		groupService.removeMember(lStudent.getId(), groupInBD.getId());

		groupInBD = groupService.getGroup(group.getId());

		assertEquals(1, groupInBD.getUsersRel().size());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testRemoveNonExistentMember() throws InstanceNotFoundException, DuplicateInstanceException, PermissionException {
		User teacher = createTeacher("John");
		userService.signUp(teacher);
		User lTeacher = userService.loginFromId(teacher.getId());
		UsersGroup group = groupService.createGroup(lTeacher.getId(), "A new group");

		groupService.removeMember(NON_EXISTENT_ID, group.getId());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testRemoveMemberToNonExistentGroup() throws InstanceNotFoundException, DuplicateInstanceException {
		User teacher = createTeacher("John");
		userService.signUp(teacher);

		groupService.removeMember(teacher.getId(), NON_EXISTENT_ID);
	}

	@Test
	public void testGetAllUsersByGroup() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		User teacher = createTeacher("John");
		userService.signUp(teacher);
		User lTeacher = userService.loginFromId(teacher.getId());

		UsersGroup group = groupService.createGroup(lTeacher.getId(), "A new group");
		UsersGroup groupInBD = groupService.getGroup(group.getId());

		User student = createStudent("Lucy");
		userService.signUp(student);
		User lStudent = userService.loginFromId(student.getId());
		groupService.addMember(lStudent.getId(), groupInBD.getId());

		User student2 = createStudent("Marco");
		userService.signUp(student2);
		User lStudent2 = userService.loginFromId(student2.getId());
		groupService.addMember(lStudent2.getId(), groupInBD.getId());

		groupInBD = groupService.getGroup(group.getId());

		List<User> users = groupService.getAllUsersByGroup(groupInBD.getId());

		assertEquals(3, users.size());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetAllUsersByNonExistentGroup() throws InstanceNotFoundException {
		groupService.getAllUsersByGroup(NON_EXISTENT_ID);
	}

	@Test
	public void testGetAllGroupsByUser() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		User teacher = createTeacher("John");
		userService.signUp(teacher);
		User lTeacher = userService.loginFromId(teacher.getId());

		groupService.createGroup(lTeacher.getId(), "A new group");
		groupService.createGroup(lTeacher.getId(), "Another group");
		groupService.createGroup(lTeacher.getId(), "One more group");

		List<UsersGroup> groups = groupService.getAllGroupsByUser(lTeacher.getId());

		assertEquals(3, groups.size());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetAllGroupsByNonExistentUser() throws InstanceNotFoundException {
		groupService.getAllGroupsByUser(NON_EXISTENT_ID);
	}

	@Test
	public void testEnableAndDisableChat() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		User teacher = createTeacher("John");
		userService.signUp(teacher);

		UsersGroup group = groupService.createGroup(teacher.getId(), "A new group");

		UsersGroup foundGroup = groupService.getGroup(group.getId());
		assertEquals(true, foundGroup.getChatEnable());

		groupService.updateGroup(group.getId(), group.getGroupName(), false);

		foundGroup = groupService.getGroup(group.getId());
		assertEquals(false, foundGroup.getChatEnable());

		groupService.updateGroup(group.getId(), group.getGroupName(), true);

		foundGroup = groupService.getGroup(group.getId());
		assertEquals(true, foundGroup.getChatEnable());
	}
}