package es.udc.paproject.backend.model.entities;

import java.io.Serializable;

public class UsersGroupRelId implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long user;
	private Long usersGroup;

	public UsersGroupRelId() {}

	public UsersGroupRelId(Long user, Long usersGroup) {
		this.user = user;
		this.usersGroup = usersGroup;
	}

	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public Long getUsersGroup() {
		return usersGroup;
	}

	public void setUsersGroup(Long usersGroup) {
		this.usersGroup = usersGroup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((usersGroup == null) ? 0 : usersGroup.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsersGroupRelId other = (UsersGroupRelId) obj;
		if (usersGroup == null) {
			if (other.usersGroup != null)
				return false;
		} else if (!usersGroup.equals(other.usersGroup))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
}
