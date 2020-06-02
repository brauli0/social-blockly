package es.udc.paproject.backend.rest.controllers;

import static es.udc.paproject.backend.rest.dtos.MessageConversor.toMessageDto;
import static es.udc.paproject.backend.rest.dtos.MessageConversor.toMessageDtos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.ChatMessage;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UsersGroup;
import es.udc.paproject.backend.model.services.ChatService;
import es.udc.paproject.backend.model.services.GroupService;
import es.udc.paproject.backend.model.services.PermissionException;
import es.udc.paproject.backend.rest.dtos.MessageDto;
import es.udc.paproject.backend.rest.dtos.MessageParamsDto;
import es.udc.paproject.backend.rest.dtos.MessageParamsDto2;

@RestController
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private ChatService chatService;

	@Autowired
	private GroupService groupService;

	@PostMapping("/create")
	public MessageDto createMessage(@RequestAttribute Long userId,
			@RequestBody MessageParamsDto messageParamsDto)
			throws InstanceNotFoundException, PermissionException {

		List<User> members = groupService.getAllUsersByGroup(messageParamsDto.getGroupId());
		boolean isMember = false;
		for (User user : members) {
			if (user.getId() == userId) {
				isMember = true;
			}
		}
		if (!isMember) {
			throw new PermissionException();
		}

		UsersGroup group = groupService.getGroup(messageParamsDto.getGroupId());
		if (!group.getChatEnable()) {
			throw new PermissionException();
		}

		ChatMessage chatMessage = chatService.createMessage(messageParamsDto.getGroupId(),
				messageParamsDto.getUserId(), messageParamsDto.getMessageText(),
				messageParamsDto.getMessageDate());

		return toMessageDto(chatMessage);
	}

	@GetMapping("/all/{groupId}")
	public List<MessageDto> getGroupMessages(@RequestAttribute Long userId, @PathVariable Long groupId)
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

		return toMessageDtos(chatService.getGroupMessages(groupId));
	}

	@PostMapping("/allbydate")
	public List<MessageDto> getGroupMessagesByDate(@RequestAttribute Long userId,
			@RequestBody MessageParamsDto2 messageParamsDto2)
			throws InstanceNotFoundException, PermissionException {

		List<User> members = groupService.getAllUsersByGroup(messageParamsDto2.getGroupId());
		boolean isMember = false;
		for (User user : members) {
			if (user.getId() == userId) {
				isMember = true;
			}
		}
		if (!isMember) {
			throw new PermissionException();
		}

		return toMessageDtos(chatService.getGroupMessagesByDate(messageParamsDto2.getGroupId(),
				messageParamsDto2.getBegin(), messageParamsDto2.getEnd()));
	}
}
