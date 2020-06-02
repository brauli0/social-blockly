package es.udc.paproject.backend.rest.dtos;

import java.util.Calendar;

public class MessageDto {
	private Long id;
	private Long groupId;
	private Long userId;
    private String messageText;
    private Calendar messageDate;

	public MessageDto() {}

	public MessageDto(Long id, Long groupId, Long userId, String messageText, Calendar messageDate) {
		this.id = id;
		this.groupId = groupId;
		this.userId = userId;
		this.messageText = messageText;
		this.messageDate = messageDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGroup() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUser(Long userId) {
		this.userId = userId;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public Calendar getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(Calendar messageDate) {
		this.messageDate = messageDate;
	}
}
