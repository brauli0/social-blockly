package es.udc.paproject.backend.rest.dtos;

import java.util.ArrayList;
import java.util.List;

import es.udc.paproject.backend.model.entities.ChatMessage;

public class MessageConversor {

	private MessageConversor() {}

	public final static MessageDto toMessageDto(ChatMessage chatMessage) {
		return new MessageDto(chatMessage.getId(), chatMessage.getGroup().getId(),
				chatMessage.getUser().getId(), chatMessage.getMessageText(), chatMessage.getMessageDate());
	}

	public final static List<MessageDto> toMessageDtos(List<ChatMessage> chatMessages) {
		List<MessageDto> messageDtos = new ArrayList<>();

		for (ChatMessage m : chatMessages) {
			messageDtos.add(new MessageDto(m.getId(), m.getGroup().getId(),
					m.getUser().getId(), m.getMessageText(), m.getMessageDate()));
		}

		return messageDtos;
	}
}
