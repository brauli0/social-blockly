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
public class Comment {
	private Long id;
	private Long commentOrigId;
	private User user;
	private Program program;
    private String commentText;
    private Calendar commentDate;

	public Comment() {}

	public Comment(User user, Program program, String commentText) {
		this.user = user;
		this.program = program;
		this.commentText = commentText;
	}

	public Comment(Long commentOrigId, User user, Program program, String commentText) {
		this.commentOrigId = commentOrigId;
		this.user = user;
		this.program = program;
		this.commentText = commentText;
	}

	public Comment(Long id, User user, Program program, String commentText, Calendar commentDate) {
		this.id = id;
		this.user = user;
		this.program = program;
		this.commentText = commentText;
		this.commentDate = commentDate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCommentOrigId() {
		return commentOrigId;
	}

	public void setCommentOrigId(Long commentOrigId) {
		this.commentOrigId = commentOrigId;
	}

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="programId")
	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public Calendar getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Calendar commentDate) {
		this.commentDate = commentDate;
	}
}
