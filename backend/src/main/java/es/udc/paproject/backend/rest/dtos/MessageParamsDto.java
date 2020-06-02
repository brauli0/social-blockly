package es.udc.paproject.backend.rest.dtos;

import java.util.Calendar;

public class MessageParamsDto {

	Long groupId;
	Long userId;
	String messageText;
	Calendar messageDate;

	public MessageParamsDto() {}

	public MessageParamsDto(Long groupId, Long userId, String messageText, Calendar messageDate) {
		this.groupId = groupId;
		this.userId = userId;
		this.messageText = messageText;
		this.messageDate = messageDate;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
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