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
import es.udc.paproject.backend.model.entities.Comment;
import es.udc.paproject.backend.model.entities.Program;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.services.CommentService;
import es.udc.paproject.backend.model.services.ProgramService;
import es.udc.paproject.backend.model.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CommentServiceTest {
	private final Long NON_EXISTENT_ID = new Long(-1);

	@Autowired
	private UserService userService;

	@Autowired
	private ProgramService programService;

	@Autowired
	private CommentService commentService;

	private User createUser(String userName) {
		return new User(userName, "password", "firstName", "lastName",
				userName + "@" + userName + ".com", Calendar.getInstance(), "STUDENT");
	}

	@Test
	public void testCreateCommentAndGetCommentsByProgram() throws DuplicateInstanceException, InstanceNotFoundException {
		User user1 = createUser("username");
		userService.signUp(user1);

		User user2 = createUser("username2");
		userService.signUp(user2);

		Program program = programService.createProgram(user1.getId(), null, "Program name", "Description", "", false);

		Comment comment1 = commentService.createComment(user1.getId(), program.getId(), "I hope you like this program!");
		Comment comment2 = commentService.createComment(user2.getId(), program.getId(), "Really good");
		Comment comment3 = commentService.createComment(user2.getId(), program.getId(), "Keep it up");

		List<Comment> comments = commentService.getCommentsByProgram(program.getId());

		assertEquals(3, comments.size());
		assertEquals(comment1, comments.get(0));
		assertEquals(comment2, comments.get(1));
		assertEquals(comment3, comments.get(2));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testCreateCommentWithNonExistentUser() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username");
		userService.signUp(user);
		Program program = programService.createProgram(user.getId(), null, "Program name", "Description", "", false);

		commentService.createComment(NON_EXISTENT_ID, program.getId(), "I hope you like this program!");
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testCreateCommentWithNonExistentProgram() throws DuplicateInstanceException, InstanceNotFoundException {
		User user = createUser("username");
		userService.signUp(user);

		commentService.createComment(user.getId(), NON_EXISTENT_ID, "I hope you like this program!");
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetCommentsOfNonExistentProgram() throws InstanceNotFoundException {
		commentService.getCommentsByProgram(NON_EXISTENT_ID);
	}

	@Test
	public void testGetSingleComment() throws DuplicateInstanceException, InstanceNotFoundException {
		User user = createUser("username");
		userService.signUp(user);

		Program program = programService.createProgram(user.getId(), null, "Program name", "Description", "", false);

		Comment comment = commentService.createComment(user.getId(), program.getId(), "I hope you like this program!");

		Comment savedComment = commentService.getComment(comment.getId());

		assertEquals(comment, savedComment);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetNonExistentComment() throws InstanceNotFoundException {
		commentService.getComment(NON_EXISTENT_ID);
	}

	@Test
	public void testCreateCommentReply() throws DuplicateInstanceException, InstanceNotFoundException {
		User user1 = createUser("username");
		userService.signUp(user1);

		User user2 = createUser("username2");
		userService.signUp(user2);

		Program program = programService.createProgram(user1.getId(), null, "Program name", "Description", "", false);

		Comment comment1 = commentService.createComment(user1.getId(), program.getId(), "I hope you like this program!");
		Comment comment2 = commentService.replyComment(comment1.getId(), user2.getId(), program.getId(), "Really good");

		List<Comment> comments = commentService.getCommentsByProgram(program.getId());

		assertEquals(2, comments.size());
		assertEquals(comment1, comments.get(0));
		assertEquals(comment2, comments.get(1));
		assertEquals(comment1.getId(), comment2.getCommentOrigId());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testCreateReplyOfNonExistentComment() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username");
		userService.signUp(user);

		Program program = programService.createProgram(user.getId(), null, "Program name", "Description", "", false);

		commentService.replyComment(NON_EXISTENT_ID, user.getId(), program.getId(), "Really good");
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testCreateCommentReplyWithNonExistentUser() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username");
		userService.signUp(user);

		Program program = programService.createProgram(user.getId(), null, "Program name", "Description", "", false);

		Comment comment = commentService.createComment(user.getId(), program.getId(), "I hope you like this program!");

		commentService.replyComment(comment.getId(), NON_EXISTENT_ID, program.getId(), "Really good");
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testCreateCommentReplyOfNonExistentProgram() throws InstanceNotFoundException, DuplicateInstanceException {
		User user = createUser("username");
		userService.signUp(user);

		Program program = programService.createProgram(user.getId(), null, "Program name", "Description", "", false);

		Comment comment = commentService.createComment(user.getId(), program.getId(), "I hope you like this program!");

		commentService.replyComment(comment.getId(), user.getId(), NON_EXISTENT_ID, "Really good");
	}

	@Test
	public void testGetCommentReplies() throws InstanceNotFoundException, DuplicateInstanceException {
		User user1 = createUser("username");
		userService.signUp(user1);

		User user2 = createUser("username2");
		userService.signUp(user2);

		Program program = programService.createProgram(user1.getId(), null, "Program name", "Description", "", false);

		Comment comment1 = commentService.createComment(user1.getId(), program.getId(), "I hope you like this program!");
		Comment comment2 = commentService.replyComment(comment1.getId(), user2.getId(), program.getId(), "Really good");
		Comment comment3 = commentService.replyComment(comment1.getId(), user2.getId(), program.getId(), "Good!!");

		List<Comment> comments = commentService.getCommentReplies(comment1.getId());

		assertEquals(2, comments.size());
		assertEquals(comment2, comments.get(0));
		assertEquals(comment3, comments.get(1));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testGetNonExistentCommentReplies() throws InstanceNotFoundException {
		commentService.getCommentReplies(NON_EXISTENT_ID);
	}

	@Test
	public void testDeleteComment() throws DuplicateInstanceException, InstanceNotFoundException {
		User user = createUser("username");
		userService.signUp(user);

		Program program = programService.createProgram(user.getId(), null, "Program name", "Description", "", false);

		Comment comment = commentService.createComment(user.getId(), program.getId(), "I hope you like this program!");

		List<Comment> comments = commentService.getCommentsByProgram(program.getId());

		assertEquals(1, comments.size());
		assertEquals(comment, comments.get(0));

		commentService.deleteComment(comment.getId());

		comments = commentService.getCommentsByProgram(program.getId());

		assertEquals(0, comments.size());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testDeleteNonExistentComment() throws InstanceNotFoundException {
		commentService.deleteComment(NON_EXISTENT_ID);
	}
}
