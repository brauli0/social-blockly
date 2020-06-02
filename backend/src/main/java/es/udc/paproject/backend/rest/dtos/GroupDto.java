package es.udc.paproject.backend.rest.dtos;

import java.util.Calendar;
import java.util.List;

import es.udc.paproject.backend.model.entities.User;

public class GroupDto {
	private Long id;
	private Boolean iAmAdmin;
    private String groupName;
	private Calendar creationDate;
	private Boolean chatEnable;
    private List<User> users;

	public GroupDto() {}

	public GroupDto(Long id, String groupName, Calendar creationDate, Boolean chatEnable, List<User> users) {
		this.id = id;
		this.iAmAdmin = false;
		this.groupName = groupName;
		this.creationDate = creationDate;
		this.chatEnable = chatEnable;
		this.users = users;
	}

	public GroupDto(Long id, Boolean iAmAdmin, String groupName, Calendar creationDate,
		Boolean chatEnable, List<User> users) {
		this.id = id;
		this.iAmAdmin = iAmAdmin;
		this.groupName = groupName;
		this.creationDate = creationDate;
		this.chatEnable = chatEnable;
		this.users = users;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getiAmAdmin() {
		return iAmAdmin;
	}

	public void setiAmAdmin(Boolean iAmAdmin) {
		this.iAmAdmin = iAmAdmin;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public Boolean getChatEnable() {
		return chatEnable;
	}

	public void setChatEnable(Boolean chatEnable) {
		this.chatEnable = chatEnable;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
