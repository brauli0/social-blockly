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
public class Exercise {
	private Long id;
	private String statementText;
	private UsersGroup group;
	private User user;
	private Calendar creationDate;
	private Calendar expirationDate;
	private String blocks;

	public Exercise() {}

	public Exercise(String statementText, UsersGroup group, User user,
			Calendar expirationDate, String blocks) {
		this.statementText = statementText;
		this.group = group;
		this.user = user;
		this.expirationDate = expirationDate;
		this.blocks = blocks;
	}

	public Exercise(Long id, String statementText, UsersGroup group, User user, Calendar creationDate,
			Calendar expirationDate, String blocks) {
		this.id = id;
		this.statementText = statementText;
		this.group = group;
		this.user = user;
		this.creationDate = creationDate;
		this.expirationDate = expirationDate;
		this.blocks = blocks;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatementText() {
		return statementText;
	}

	public void setStatementText(String statementText) {
		this.statementText = statementText;
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

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public Calendar getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getBlocks() {
		return blocks;
	}

	public void setBlocks(String blocks) {
		this.blocks = blocks;
	}
}
