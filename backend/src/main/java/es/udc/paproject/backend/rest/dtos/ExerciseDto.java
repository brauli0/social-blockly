package es.udc.paproject.backend.rest.dtos;

import java.util.Calendar;

public class ExerciseDto {
	private Long id;
	private String statementText;
	private Long groupId;
	private Long userId;
	private Calendar creationDate;
	private Calendar expirationDate;
	private String blocks;

	public ExerciseDto() {}

	public ExerciseDto(Long id, String statementText, Long groupId, Long userId, Calendar creationDate,
			Calendar expirationDate, String blocks) {
		this.id = id;
		this.statementText = statementText;
		this.groupId = groupId;
		this.userId = userId;
		this.creationDate = creationDate;
		this.expirationDate = expirationDate;
		this.blocks = blocks;
	}

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

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
