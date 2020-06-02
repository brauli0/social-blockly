package es.udc.paproject.backend.model.entities;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ChatMessage {
	private Long id;
	private UsersGroup group;
	private User user;
    private String messageText;
    private Calendar messageDate;

	public ChatMessage() {}

	public ChatMessage(UsersGroup group, User user, String messageText, Calendar messageDate) {
		this.group = group;
		this.user = user;
		this.messageText = messageText;
		this.messageDate = messageDate;
	}

	public ChatMessage(Long id, UsersGroup group, User user, String messageText, Calendar messageDate) {
		this.id = id;
		this.group = group;
		this.user = user;
		this.messageText = messageText;
		this.messageDate = messageDate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="groupId")
	public UsersGroup getGroup() {
		return group;
	}

	public void setGroup(UsersGroup group) {
		this.group = group;
	}

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
