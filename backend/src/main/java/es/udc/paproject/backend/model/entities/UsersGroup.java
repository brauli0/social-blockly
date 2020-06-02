package es.udc.paproject.backend.model.entities;

import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class UsersGroup {
	private Long id;
    private String groupName;
    private Calendar creationDate;
    private Boolean chatEnable;
    private List<UsersGroupRel> usersRel;

	public UsersGroup() {}

	public UsersGroup(String groupName) {
		this.groupName = groupName;
		this.chatEnable = true;
	}

	public UsersGroup(Long id, String groupName, Calendar creationDate) {
		this.id = id;
		this.groupName = groupName;
		this.creationDate = creationDate;
		this.chatEnable = true;
	}

	public UsersGroup(Long id, String groupName, Calendar creationDate, List<UsersGroupRel> usersRel) {
		this.id = id;
		this.groupName = groupName;
		this.creationDate = creationDate;
		this.usersRel = usersRel;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(mappedBy = "usersGroup", cascade = CascadeType.REMOVE)
	public List<UsersGroupRel> getUsersRel() {
		return usersRel;
	}

	public void setUsersRel(List<UsersGroupRel> usersRel) {
		this.usersRel = usersRel;
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
}
