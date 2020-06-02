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
import es.udc.paproject.backend.model.entities.ChatMessage;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UsersGroup;
import es.udc.paproject.backend.model.services.ChatService;
import es.udc.paproject.backend.model.services.GroupService;
import es.udc.paproject.backend.model.services.PermissionException;
import es.udc.paproject.backend.model.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ChatServiceTest {
	private final Long NON_EXISTENT_ID = new Long(-1);

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private ChatService chatService;

	private User createUser(String userName) {
		return new User(userName, "password", "firstName", "lastName",
				userName + "@" + userName + ".com", Calendar.getInstance(), "TEACHER");
	}

	@Test
	public void testCreateMessageAndGetGroupMessages() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		User user1 = createUser("username1");
		userService.signUp(user1);
		User loggedInUser1 = userService.loginFromId(user1.getId());

		User user2 = createUser("username2");
		userService.signUp(user2);
		User loggedInUser2 = userService.loginFromId(user2.getId());

		UsersGroup group = groupService.createGroup(user1.getId(), "A new group");
		UsersGroup groupInBD = groupService.getGroup(group.getId());

		groupService.addMember(loggedInUser2.getId(), groupInBD.getId());

		ChatMessage message1 = chatService.createMessage(groupInBD.getId(), loggedInUser1.getId(), "Hello", Calendar.getInstance());

		ChatMessage message2 = chatService.createMessage(groupInBD.getId(), loggedInUser2.getId(), "Hi", Calendar.getInstance());
		ChatMessage message3 = chatService.createMessage(groupInBD.getId(), loggedInUser2.getId(), "How are you?", Calendar.getInstance());

		List<ChatMessage> chatMessages = chatService.getGroupMessages(groupInBD.getId());

		assertEquals(3, chatMessages.size());
		assertEquals(message1, chatMessages.get(0));
		assertEquals(message2, chatMessages.get(1));
		assertEquals(message3, chatMessages.get(2));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testCreateMessageInNonExistentGroup() throws InstanceNotFoundException, DuplicateInstanceException {
		User user1 = createUser("username1");
		userService.signUp(user1);
		User loggedInUser1 = userService.loginFromId(user1.getId());

		chatService.createMessage(NON_EXISTENT_ID, loggedInUser1.getId(), "Hello", Calendar.getInstance());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testCreateMessageWithNonExistentUser() throws InstanceNotFoundException, DuplicateInstanceException, PermissionException {
		User user1 = createUser("username1");
		userService.signUp(user1);

		UsersGroup group = groupService.createGroup(user1.getId(), "A new group");
		UsersGroup groupInBD = groupService.getGroup(group.getId());

		chatService.createMessage(groupInBD.getId(), NON_EXISTENT_ID, "Hello", Calendar.getInstance());
	}

	@Test
	public void testGetGroupMessagesByDate() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
		User user1 = createUser("username1");
		userService.signUp(user1);
		User loggedInUser1 = userService.loginFromId(user1.getId());

		UsersGroup group = groupService.createGroup(user1.getId(), "A new group");
		UsersGroup groupInBD = groupService.getGroup(group.getId());

		Calendar previousDate = Calendar.getInstance();
		previousDate.set(2019, 0, 5);

		chatService.createMessage(groupInBD.getId(), loggedInUser1.getId(), "Hello", previousDate);
		chatService.createMessage(groupInBD.getId(), loggedInUser1.getId(), "Hi", previousDate);

		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_YEAR, -1);

		ChatMessage message3 = chatService.createMessage(groupInBD.getId(), loggedInUser1.getId(), "A message", yesterday);
		ChatMessage message4 = chatService.createMessage(groupInBD.getId(), loggedInUser1.getId(), "Another message", yesterday);

		Calendar aWeekAgo = Calendar.getInstance();
		aWeekAgo.add(Calendar.DAY_OF_YEAR, -7);
		List<ChatMessage> chatMessages = chatService.getGroupMessagesByDate(groupInBD.getId(), aWeekAgo, Calendar.getInstance());

		assertEquals(2, chatMessages.size());
		assertEquals(message3, chatMessages.get(0));
		assertEquals(message4, chatMessages.get(1));
	}
}
