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
import es.udc.paproject.backend.model.entities.Program;
import es.udc.paproject.backend.model.entities.Rating;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.services.ProgramService;
import es.udc.paproject.backend.model.services.RatingService;
import es.udc.paproject.backend.model.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RatingServiceTest {

	private final Long NON_EXISTENT_ID = new Long(-1);

	@Autowired
	private RatingService ratingService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProgramService programService;

	private User createUser(String userName) {
		return new User(userName, "password", "firstName", "lastName",
				userName + "@" + userName + ".com", Calendar.getInstance(), "STUDENT");
	}

	@Test
	public void testCreateRatingsAndGetAverageProgramRatings()
			throws DuplicateInstanceException, InstanceNotFoundException {
		User user1 = createUser("username");
		userService.signUp(user1);

		User user2 = createUser("username2");
		userService.signUp(user2);

		User user3 = createUser("username3");
		userService.signUp(user3);

		Program program = programService.createProgram(user1.getId(), null, "Program name", "Description", "", false);

		ratingService.createRating(user1.getId(), program.getId(), (float) 7.5);
		ratingService.createRating(user2.getId(), program.getId(), (float) 3.8);
		ratingService.createRating(user3.getId(), program.getId(), (float) 5.2);

		List<Rating> ratings = ratingService.getProgramRatings(program.getId());

		assertEquals(3, ratings.size());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetAverageRatingsOfNonExistentProgram() throws InstanceNotFoundException {
		ratingService.getProgramRatings(NON_EXISTENT_ID);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testCreateRatingWithNonExistentUser() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username");
		userService.signUp(user);

		Program program = programService.createProgram(user.getId(), null, "Program name", "Description", "", false);

		ratingService.createRating(NON_EXISTENT_ID, program.getId(), (float) 7.5);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testCreateRatingWithNonExistentProgram() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username");
		userService.signUp(user);

		programService.createProgram(user.getId(), null, "Program name", "Description", "", false);

		ratingService.createRating(user.getId(), NON_EXISTENT_ID, (float) 7.5);
	}

	@Test(expected = DuplicateInstanceException.class)
	public void testCreateDuplicateRatings() throws DuplicateInstanceException, InstanceNotFoundException {
		User user = createUser("username");
		userService.signUp(user);

		Program program = programService.createProgram(user.getId(), null, "Program name", "Description", "", false);

		ratingService.createRating(user.getId(), program.getId(), (float) 7.5);
		ratingService.createRating(user.getId(), program.getId(), (float) 9.5);
	}

	@Test
	public void testDeleteRating() throws DuplicateInstanceException, InstanceNotFoundException {
		User user1 = createUser("username");
		userService.signUp(user1);

		User user2 = createUser("username2");
		userService.signUp(user2);

		Program program = programService.createProgram(user1.getId(), null, "Program name", "Description", "", false);

		Rating rating1 = ratingService.createRating(user1.getId(), program.getId(), (float) 7.2);
		Rating rating2 = ratingService.createRating(user2.getId(), program.getId(), (float) 3.8);

		List<Rating> ratings = ratingService.getProgramRatings(program.getId());
		assertEquals(2, ratings.size());
		assertEquals(rating1, ratings.get(0));
		assertEquals(rating2, ratings.get(1));

		ratingService.deleteRating(user2.getId(), program.getId());

		ratings = ratingService.getProgramRatings(program.getId());
		assertEquals(1, ratings.size());
		assertEquals(rating1, ratings.get(0));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testDeleteNonExistentRatingByUser() throws DuplicateInstanceException, InstanceNotFoundException {
		User user = createUser("username");
		userService.signUp(user);

		Program program = programService.createProgram(user.getId(), null, "Program name", "Description", "", false);

		ratingService.deleteRating(NON_EXISTENT_ID, program.getId());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testDeleteNonExistentRatingByProgram() throws DuplicateInstanceException, InstanceNotFoundException {
		User user = createUser("username");
		userService.signUp(user);

		programService.createProgram(user.getId(), null, "Program name", "Description", "", false);

		ratingService.deleteRating(user.getId(), NON_EXISTENT_ID);
	}
}
