package es.udc.paproject.backend.model.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(SharedId.class)
public class Shared {
	private User user;
	private Program program;

	public Shared() {}

	public Shared(User user, Program program) {
		this.user = user;
		this.program = program;
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
	@JoinColumn(name="programId", nullable=false)
	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}
}
