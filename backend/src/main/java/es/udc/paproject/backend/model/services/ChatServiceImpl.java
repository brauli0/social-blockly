package es.udc.paproject.backend.model.services;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.ChatMessage;
import es.udc.paproject.backend.model.entities.MessageDao;
import es.udc.paproject.backend.model.entities.MessageDaoImpl;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;
import es.udc.paproject.backend.model.entities.UsersGroup;
import es.udc.paproject.backend.model.entities.UsersGroupDao;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UsersGroupDao usersGroupDao;

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private MessageDaoImpl messageDaoImpl;

	@Override
	public ChatMessage createMessage(Long groupId, Long userId, String messageText, Calendar messageDate)
			throws InstanceNotFoundException {

		Optional<UsersGroup> opGroup = usersGroupDao.findById(groupId);
		if (!opGroup.isPresent()) {
			throw new InstanceNotFoundException("project.entities.usersGroup", groupId);
		}
		UsersGroup group = opGroup.get();

		Optional<User> opUser = userDao.findById(userId);
		if (!opUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}
		User user = opUser.get();

		TimeZone tz = TimeZone.getTimeZone("GMT+2");
		messageDate.setTimeZone(tz);
		ChatMessage chatMessage = new ChatMessage(group, user, messageText, messageDate);

		return messageDao.save(chatMessage);
	}

	@Override
	public List<ChatMessage> getGroupMessages(Long groupId) throws InstanceNotFoundException {
		Optional<UsersGroup> opGroup = usersGroupDao.findById(groupId);
		if (!opGroup.isPresent()) {
			throw new InstanceNotFoundException("project.entities.usersGroup", groupId);
		}

		return messageDaoImpl.getGroupMessages(groupId);
	}

	@Override
	public List<ChatMessage> getGroupMessagesByDate(Long groupId, Calendar begin, Calendar end)
			throws InstanceNotFoundException {

		Optional<UsersGroup> opGroup = usersGroupDao.findById(groupId);
		if (!opGroup.isPresent()) {
			throw new InstanceNotFoundException("project.entities.usersGroup", groupId);
		}

		return messageDaoImpl.getGroupMessagesByDate(groupId, begin, end);
	}

}
