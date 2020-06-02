package es.udc.paproject.backend.model.entities;

import java.io.Serializable;

public class RatingId implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long user;
	private Long program;

	public RatingId() {}

	public RatingId(Long user, Long program) {
		this.user = user;
		this.program = program;
	}

	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public Long getProgram() {
		return program;
	}

	public void setProgram(Long program) {
		this.program = program;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((program == null) ? 0 : program.hashCode());
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
		RatingId other = (RatingId) obj;
		if (program == null) {
			if (other.program != null)
				return false;
		} else if (!program.equals(other.program))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
}
