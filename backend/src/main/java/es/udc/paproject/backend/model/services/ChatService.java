package es.udc.paproject.backend.model.services;

import java.util.Calendar;
import java.util.List;

import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.ChatMessage;

public interface ChatService {
	ChatMessage createMessage(Long groupId, Long userId, String messageText, Calendar messageDate)
			throws InstanceNotFoundException;

	List<ChatMessage> getGroupMessages(Long groupId) throws InstanceNotFoundException;

	List<ChatMessage> getGroupMessagesByDate(Long groupId, Calendar begin, Calendar end)
		throws InstanceNotFoundException;
}
