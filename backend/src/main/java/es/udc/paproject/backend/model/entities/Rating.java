package es.udc.paproject.backend.model.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(RatingId.class)
public class Rating {
	private User user;
	private Program program;
	private Float rating;

	public Rating() {}

	public Rating(User user, Program program, Float rating) {
		this.user = user;
		this.program = program;
		this.rating = rating;
	}

	@Id
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="userId", nullable=false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Id
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="programId", nullable=false)
	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}
}
