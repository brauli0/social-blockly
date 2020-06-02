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
import es.udc.paproject.backend.model.entities.Exercise;
import es.udc.paproject.backend.model.entities.Program;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UsersGroup;
import es.udc.paproject.backend.model.services.ExerciseService;
import es.udc.paproject.backend.model.services.GroupService;
import es.udc.paproject.backend.model.services.PermissionException;
import es.udc.paproject.backend.model.services.ProgramService;
import es.udc.paproject.backend.model.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProgramServiceTest {
	private final Long NON_EXISTENT_ID = new Long(-1);

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private ExerciseService exerciseService;

	@Autowired
	private ProgramService programService;

	private User createUser(String userName) {
		return new User(userName, "password", "firstName", "lastName",
				userName + "@" + userName + ".com", Calendar.getInstance(), "TEACHER");
	}

//	private Program createProgram(Long userId) {
//		return new Program(userId, "Program name", "Description...", "");
//	}

	@Test
	public void testCreateAndFindProgram() throws DuplicateInstanceException, InstanceNotFoundException {
		User user = createUser("username");

		userService.signUp(user);

		User loggedInUser = userService.loginFromId(user.getId());

		Program program = programService.createProgram(loggedInUser.getId(), null, "Program name", "Description", "", false);

		Program programInBD = programService.getProgram(program.getId());

		assertEquals(program, programInBD);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testCreateWithNonExistentUser() throws InstanceNotFoundException {
		programService.createProgram(NON_EXISTENT_ID, null, "Program name", "Description", "", false);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentProgram() throws InstanceNotFoundException {
		programService.getProgram(NON_EXISTENT_ID);
	}

	@Test
	public void testGetAllProgramsByUser() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username");

		userService.signUp(user);

		User loggedInUser = userService.loginFromId(user.getId());

		Program program1 = programService.createProgram(loggedInUser.getId(), null, "Program 1", "Description", "", false);
		Program program2 = programService.createProgram(loggedInUser.getId(), null, "Program 2", "Description", "", false);
		Program program3 = programService.createProgram(loggedInUser.getId(), null, "Program 3", "Description", "", false);

		List<Program> programs = programService.getProgramsByUser(loggedInUser.getId());

		assertEquals(programs.size(), 3);
		assertEquals(programs.get(0), program1);
		assertEquals(programs.get(1), program2);
		assertEquals(programs.get(2), program3);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetAllProgramsByNonExistentUser() throws InstanceNotFoundException, DuplicateInstanceException {
		programService.getProgramsByUser(NON_EXISTENT_ID);
	}

	@Test
	public void testDeleteProgram() throws DuplicateInstanceException, InstanceNotFoundException {
		User user = createUser("username");

		userService.signUp(user);

		User loggedInUser = userService.loginFromId(user.getId());

		Program program1 = programService.createProgram(loggedInUser.getId(), null, "Program 1", "Description", "", false);
		Program program2 = programService.createProgram(loggedInUser.getId(), null, "Program 2", "Description", "", false);

		List<Program> programs = programService.getProgramsByUser(loggedInUser.getId());

		assertEquals(programs.size(), 2);

		programService.deleteProgram(program1.getId());

		programs = programService.getProgramsByUser(loggedInUser.getId());

		assertEquals(programs.size(), 1);
		assertEquals(programs.get(0), program2);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testDeleteNonExistentProgram() throws InstanceNotFoundException {
		programService.deleteProgram(NON_EXISTENT_ID);
	}

	@Test
	public void testUpdateProgram() throws DuplicateInstanceException, InstanceNotFoundException {
		User user = createUser("username");

		userService.signUp(user);

		User loggedInUser = userService.loginFromId(user.getId());

		Program program = programService.createProgram(loggedInUser.getId(), null, "Program name", "Description", "", false);

		Program programInBD = programService.getProgram(program.getId());

		assertEquals(program, programInBD);

		Program updatedProgram = programService.updateProgram(program.getId(), "Name edited", "Desc edited", "");

		Program updatedProgramInBD = programService.getProgram(program.getId());

		assertEquals(updatedProgram, updatedProgramInBD);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateNonExistentProgram() throws InstanceNotFoundException {
		programService.updateProgram(NON_EXISTENT_ID, "Name edited", "Desc edited", "");
	}

	@Test
	public void testSearchProgramsByKeyword() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username");
		userService.signUp(user);

		User loggedInUser = userService.loginFromId(user.getId());

		Program program1 = programService.createProgram(loggedInUser.getId(), null, "Program with a loop", "Description", "", false);
		Program program2 = programService.createProgram(loggedInUser.getId(), null, "A loop", "A loop", "", false);
		Program program3 = programService.createProgram(loggedInUser.getId(), null, "Program 3", "Other loop in this program", "", false);

		List<Program> programs = programService.getProgramsByKeyword("loop");

		assertEquals(programs.size(), 3);
		assertEquals(programs.get(0), program1);
		assertEquals(programs.get(1), program2);
		assertEquals(programs.get(2), program3);

		programService.setPrivate(program1.getId());

		programs = programService.getProgramsByKeyword("loop");

		assertEquals(programs.size(), 2);
		assertEquals(programs.get(0), program2);
		assertEquals(programs.get(1), program3);
	}

	@Test
	public void testGetPublicProgramsByUser() throws DuplicateInstanceException, InstanceNotFoundException {
		User user = createUser("username");

		userService.signUp(user);

		User loggedInUser = userService.loginFromId(user.getId());

		Program program1 = programService.createProgram(loggedInUser.getId(), null, "Program 1", "Description", "", false);
		programService.createProgram(loggedInUser.getId(), null, "Program 2", "Description", "", true);
		Program program3 = programService.createProgram(loggedInUser.getId(), null, "Program 3", "Description", "", false);

		List<Program> programs = programService.getPublicProgramsByUser(loggedInUser.getId());

		assertEquals(programs.size(), 2);
		assertEquals(programs.get(0), program1);
		assertEquals(programs.get(1), program3);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetPublicProgramsByNonExistentUser() throws InstanceNotFoundException {
		programService.getPublicProgramsByUser(NON_EXISTENT_ID);
	}

	@Test
	public void testSetPublic() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username");

		userService.signUp(user);

		User loggedInUser = userService.loginFromId(user.getId());

		Program program1 = programService.createProgram(loggedInUser.getId(), null, "Program 1", "Description", "", true);
		programService.createProgram(loggedInUser.getId(), null, "Program 2", "Description", "", true);

		List<Program> programs = programService.getPublicProgramsByUser(loggedInUser.getId());
		assertEquals(0, programs.size());

		programService.setPublic(program1.getId());

		programs = programService.getPublicProgramsByUser(loggedInUser.getId());
		assertEquals(1, programs.size());
		assertEquals(program1, programs.get(0));
	}

	@Test
	public void testSetPublicWithSharedUsers() throws DuplicateInstanceException, InstanceNotFoundException {
		// When we set a program as public, its shared users are removed

		User user1 = createUser("username1");
		userService.signUp(user1);
		User user2 = createUser("username2");
		userService.signUp(user2);
		User user3 = createUser("username3");
		userService.signUp(user3);

		Program program = programService.createProgram(user1.getId(), null, "Program", "Description", "", false);

		programService.shareProgram(user2.getId(), program.getId());
		programService.shareProgram(user3.getId(), program.getId());

		List<User> users = programService.getSharedUsersByProgram(program.getId());

		assertEquals(2, users.size());
		assertEquals(user2, users.get(0));
		assertEquals(user3, users.get(1));

		programService.setPublic(program.getId());

		users = programService.getSharedUsersByProgram(program.getId());

		assertEquals(0, users.size());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testSetPublicNonExistentProgram() throws InstanceNotFoundException {
		programService.setPublic(NON_EXISTENT_ID);
	}

	@Test
	public void testSetPrivate() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username");

		userService.signUp(user);

		User loggedInUser = userService.loginFromId(user.getId());

		Program program1 = programService.createProgram(loggedInUser.getId(), null, "Program 1", "Description", "", false);
		Program program2 = programService.createProgram(loggedInUser.getId(), null, "Program 2", "Description", "", false);

		List<Program> programs = programService.getPublicProgramsByUser(loggedInUser.getId());
		assertEquals(2, programs.size());
		assertEquals(program1, programs.get(0));
		assertEquals(program2, programs.get(1));

		programService.setPrivate(program1.getId());

		programs = programService.getPublicProgramsByUser(loggedInUser.getId());
		assertEquals(1, programs.size());
		assertEquals(program2, programs.get(0));
	}

	@Test
	public void testSetPrivateWithSharedUsers() throws DuplicateInstanceException, InstanceNotFoundException {
		// When we set a program as private, its shared users are removed

		User user1 = createUser("username1");
		userService.signUp(user1);
		User user2 = createUser("username2");
		userService.signUp(user2);
		User user3 = createUser("username3");
		userService.signUp(user3);

		Program program = programService.createProgram(user1.getId(), null, "Program", "Description", "", false);

		programService.shareProgram(user2.getId(), program.getId());
		programService.shareProgram(user3.getId(), program.getId());

		List<User> users = programService.getSharedUsersByProgram(program.getId());

		assertEquals(2, users.size());
		assertEquals(user2, users.get(0));
		assertEquals(user3, users.get(1));

		programService.setPrivate(program.getId());

		users = programService.getSharedUsersByProgram(program.getId());

		assertEquals(0, users.size());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testSetPrivateNonExistentProgram() throws InstanceNotFoundException {
		programService.setPrivate(NON_EXISTENT_ID);
	}

	@Test
	public void testShareProgramAndGetSharedPrograms() throws DuplicateInstanceException, InstanceNotFoundException {
		User user1 = createUser("username1");
		userService.signUp(user1);
		User user2 = createUser("username2");
		userService.signUp(user2);
		User user3 = createUser("username3");
		userService.signUp(user3);

		programService.createProgram(user1.getId(), null, "Program 1", "Description", "", true);
		Program program = programService.createProgram(user1.getId(), null, "Program 2", "Description", "", true);

		programService.shareProgram(user2.getId(), program.getId());

		List<Program> sharedProgramsUser2 = programService.getSharedProgramsWithMe(user2.getId());
		List<Program> sharedProgramsUser3 = programService.getSharedProgramsWithMe(user3.getId());

		assertEquals(1, sharedProgramsUser2.size());
		assertEquals(program, sharedProgramsUser2.get(0));

		assertEquals(0, sharedProgramsUser3.size());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetSharedProgramsOfNonExistentUser() throws InstanceNotFoundException {
		programService.getSharedProgramsWithMe(NON_EXISTENT_ID);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testShareNonExistentUser() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username1");
		userService.signUp(user);

		Program program = programService.createProgram(user.getId(), null, "Program 1", "Description", "", true);

		programService.shareProgram(NON_EXISTENT_ID, program.getId());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testShareNonExistentProgram() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username1");
		userService.signUp(user);

		programService.shareProgram(user.getId(), NON_EXISTENT_ID);
	}

	@Test
	public void testUnShare() throws DuplicateInstanceException, InstanceNotFoundException {
		User user1 = createUser("username1");
		userService.signUp(user1);
		User user2 = createUser("username2");
		userService.signUp(user2);

		Program program = programService.createProgram(user1.getId(), null, "Program 1", "Description", "", true);

		programService.shareProgram(user2.getId(), program.getId());

		List<Program> sharedPrograms = programService.getSharedProgramsWithMe(user2.getId());

		assertEquals(1, sharedPrograms.size());
		assertEquals(program, sharedPrograms.get(0));

		programService.unshareProgram(user2.getId(), program.getId());

		sharedPrograms = programService.getSharedProgramsWithMe(user2.getId());

		assertEquals(0, sharedPrograms.size());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUnshareNonExistentUser() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username1");
		userService.signUp(user);

		Program program = programService.createProgram(user.getId(), null, "Program 1", "Description", "", true);

		programService.unshareProgram(NON_EXISTENT_ID, program.getId());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUnshareNonExistentProgram() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username1");
		userService.signUp(user);

		programService.unshareProgram(user.getId(), NON_EXISTENT_ID);
	}

	@Test
	public void testGetSharedUsersByProgram() throws DuplicateInstanceException, InstanceNotFoundException {
		User user1 = createUser("username1");
		userService.signUp(user1);
		User user2 = createUser("username2");
		userService.signUp(user2);
		User user3 = createUser("username3");
		userService.signUp(user3);

		programService.createProgram(user1.getId(), null, "Program 1", "Description", "", true);
		Program program = programService.createProgram(user1.getId(), null, "Program 2", "Description", "", true);

		programService.shareProgram(user2.getId(), program.getId());
		programService.shareProgram(user3.getId(), program.getId());

		List<Program> sharedProgramsUser2 = programService.getSharedProgramsWithMe(user2.getId());
		List<Program> sharedProgramsUser3 = programService.getSharedProgramsWithMe(user3.getId());

		assertEquals(1, sharedProgramsUser2.size());
		assertEquals(program, sharedProgramsUser2.get(0));
		assertEquals(1, sharedProgramsUser3.size());
		assertEquals(program, sharedProgramsUser3.get(0));

		List<User> users = programService.getSharedUsersByProgram(program.getId());

		assertEquals(2, users.size());
		assertEquals(user2, users.get(0));
		assertEquals(user3, users.get(1));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetSharedUsersOfNonExistentProgram() throws InstanceNotFoundException {
		programService.getSharedUsersByProgram(NON_EXISTENT_ID);
	}

	@Test
	public void testGetSharedProgramsWithMeByUser() throws DuplicateInstanceException, InstanceNotFoundException {
		User user1 = createUser("username1");
		userService.signUp(user1);
		User user2 = createUser("username2");
		userService.signUp(user2);
		User user3 = createUser("username3");
		userService.signUp(user3);

		Program program1 = programService.createProgram(user1.getId(), null, "Program 1", "Description", "", true);
		Program program2 = programService.createProgram(user1.getId(), null, "Program 2", "Description", "", true);
		Program program3 = programService.createProgram(user2.getId(), null, "Program 2", "Description", "", true);

		programService.shareProgram(user2.getId(), program1.getId());
		programService.shareProgram(user3.getId(), program2.getId());	// <==
		programService.shareProgram(user3.getId(), program3.getId());

		List<Program> programs = programService.getSharedProgramsWithMeByUser(user3.getId(), user1.getId());

		assertEquals(1, programs.size());
		assertEquals(program2, programs.get(0));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetSharedProgramsWithNonExistantMeByUser() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username");
		userService.signUp(user);

		programService.getSharedProgramsWithMeByUser(NON_EXISTENT_ID, user.getId());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetSharedProgramsWithMeByNonExistentUser() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username");
		userService.signUp(user);

		programService.getSharedProgramsWithMeByUser(user.getId(), NON_EXISTENT_ID);
	}

	@Test
	public void testGetProgramsByExercise() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		User user1 = createUser("username1");
		userService.signUp(user1);

		User user2 = createUser("username2");
		userService.signUp(user2);

		User user3 = createUser("username3");
		userService.signUp(user3);

		UsersGroup group = groupService.createGroup(user1.getId(), "A new group");

		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_YEAR, 1);

		Exercise exercise = exerciseService.createExercise("Enunciado...", group.getId(), user1.getId(), tomorrow, "YYYYYYYY");

		Program program1 = programService.createProgram(user1.getId(), exercise.getId(), "Program 1", "Description", "", true);
		Program program2 = programService.createProgram(user2.getId(), exercise.getId(), "Program 2", "Description", "", true);
		programService.createProgram(user3.getId(), null, "Program 2", "Description", "", true);

		List<Program> programs = programService.getProgramsByExercise(exercise.getId());
		assertEquals(2, programs.size());
		assertEquals(program1, programs.get(0));
		assertEquals(program2, programs.get(1));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetProgramsByNonExistentExercise() throws InstanceNotFoundException {
		programService.getProgramsByExercise(NON_EXISTENT_ID);
	}
}