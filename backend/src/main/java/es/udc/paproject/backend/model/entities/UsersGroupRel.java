package es.udc.paproject.backend.model.entities;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(UsersGroupRelId.class)
public class UsersGroupRel {
	private User user;
	private UsersGroup usersGroup;
	private Boolean isAdmin;
	private Calendar accessDate;

	public UsersGroupRel() {}

	public UsersGroupRel(User user, UsersGroup usersGroup) {
		this.user = user;
		this.usersGroup = usersGroup;
	}

	public UsersGroupRel(User user, UsersGroup usersGroup, Boolean isAdmin) {
		this.user = user;
		this.usersGroup = usersGroup;
		this.isAdmin = isAdmin;
	}

	public UsersGroupRel(User user, UsersGroup usersGroup, Boolean isAdmin, Calendar accessDate) {
		this.user = user;
		this.usersGroup = usersGroup;
		this.isAdmin = isAdmin;
		this.accessDate = accessDate;
	}

	@Id
	@ManyToOne
	@JoinColumn(name="userId", nullable=false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Id
	@ManyToOne
	@JoinColumn(name="groupId", nullable=false)
	public UsersGroup getUsersGroup() {
		return usersGroup;
	}

	public void setUsersGroup(UsersGroup usersGroup) {
		this.usersGroup = usersGroup;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Calendar getAccessDate() {
		return accessDate;
	}

	public void setAccessDate(Calendar accessDate) {
		this.accessDate = accessDate;
	}
}
