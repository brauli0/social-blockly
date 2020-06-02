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
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UsersGroup;
import es.udc.paproject.backend.model.services.ExerciseService;
import es.udc.paproject.backend.model.services.GroupService;
import es.udc.paproject.backend.model.services.PermissionException;
import es.udc.paproject.backend.model.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ExerciseServiceTest {
	private final Long NON_EXISTENT_ID = new Long(-1);

	@Autowired
	private ExerciseService exerciseService;

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	private User createUser(String userName) {
		return new User(userName, "password", "firstName", "lastName",
				userName + "@" + userName + ".com", Calendar.getInstance(), "TEACHER");
	}

	@Test
	public void testCreateAndGetExercise() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		User user = createUser("username");
		userService.signUp(user);

		UsersGroup group = groupService.createGroup(user.getId(), "A new group");

		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_YEAR, 1);

		Exercise exercise = exerciseService.createExercise("Enunciado...", group.getId(),
				user.getId(), tomorrow, "YYYYYYYY");

		Exercise foundExercise = exerciseService.getExercise(exercise.getId());

		assertEquals(exercise, foundExercise);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testCreateExerciseWithNonExistantGroup() throws DuplicateInstanceException, InstanceNotFoundException {
		User user = createUser("username");
		userService.signUp(user);

		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_YEAR, 1);

		exerciseService.createExercise("Enunciado...", NON_EXISTENT_ID, user.getId(), tomorrow, "YYYYYYYY");
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testCreateExerciseWithNonExistantUser() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		User user = createUser("username");
		userService.signUp(user);

		UsersGroup group = groupService.createGroup(user.getId(), "A new group");

		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_YEAR, 1);

		exerciseService.createExercise("Enunciado...", group.getId(), NON_EXISTENT_ID, tomorrow, "YYYYYYYY");
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetNonExistantExercise() throws InstanceNotFoundException {
		exerciseService.getExercise(NON_EXISTENT_ID);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testDeleteExercise() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		User user = createUser("username");
		userService.signUp(user);

		UsersGroup group = groupService.createGroup(user.getId(), "A new group");

		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_YEAR, 1);

		Exercise exercise = exerciseService.createExercise("Enunciado...", group.getId(),
				user.getId(), tomorrow, "YYYYYYYY");

		Exercise foundExercise = exerciseService.getExercise(exercise.getId());

		assertEquals(exercise, foundExercise);

		exerciseService.deleteExercise(exercise.getId());

		exerciseService.getExercise(exercise.getId());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testDeleteNonExistentExercise() throws InstanceNotFoundException {
		exerciseService.deleteExercise(NON_EXISTENT_ID);
	}

	@Test
	public void testUpdateExercise() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		User user = createUser("username");
		userService.signUp(user);

		UsersGroup group = groupService.createGroup(user.getId(), "A new group");

		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_YEAR, 1);

		Exercise exercise = exerciseService.createExercise("Enunciado...", group.getId(),
				user.getId(), tomorrow, "YYYYYYYY");

		Exercise foundExercise = exerciseService.getExercise(exercise.getId());

		assertEquals(exercise, foundExercise);

		Calendar inAWeek = Calendar.getInstance();
		inAWeek.add(Calendar.DAY_OF_YEAR, 7);

		Exercise updatedExercise = exerciseService.updateExercise(exercise.getId(), "Novo enunciado", inAWeek);

		Exercise foundUpdatedExercise = exerciseService.getExercise(exercise.getId());

		assertEquals(updatedExercise, foundUpdatedExercise);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateNonExistentExercise() throws InstanceNotFoundException {
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_YEAR, 7);

		exerciseService.updateExercise(NON_EXISTENT_ID, "Novo enunciado", tomorrow);
	}

	@Test
	public void testGetExercisesByGroup() throws InstanceNotFoundException, DuplicateInstanceException, PermissionException {
		User user = createUser("username");
		userService.signUp(user);

		UsersGroup group = groupService.createGroup(user.getId(), "A new group");

		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_YEAR, 1);

		Exercise exercise1 = exerciseService.createExercise("Primeiro exercicio...", group.getId(),
				user.getId(), tomorrow, "YYYYYYYY");

		Exercise exercise2 = exerciseService.createExercise("Segundo exercicio...", group.getId(),
				user.getId(), tomorrow, "YYYYYYYY");

		Exercise exercise3 = exerciseService.createExercise("Terceiro exercicio...", group.getId(),
				user.getId(), tomorrow, "YYYYYYYY");

		List<Exercise> exercises = exerciseService.getExercisesByGroup(group.getId());

		assertEquals(3, exercises.size());
		assertEquals(exercise1, exercises.get(0));
		assertEquals(exercise2, exercises.get(1));
		assertEquals(exercise3, exercises.get(2));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetExercisesOfNonExistentGroup() throws InstanceNotFoundException {
		exerciseService.getExercisesByGroup(NON_EXISTENT_ID);
	}
}
