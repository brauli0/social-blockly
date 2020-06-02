package es.udc.paproject.backend.rest.dtos;

import java.util.Calendar;

public class ExerciseParamsDto {
	String statementText;
	Long groupId;
	Long userId;
	Calendar expirationDate;
	String blocks;

	public ExerciseParamsDto() {}

	public ExerciseParamsDto(String statementText, Long groupId, Long userId,
			Calendar expirationDate, String blocks) {
		this.statementText = statementText;
		this.groupId = groupId;
		this.userId = userId;
		this.expirationDate = expirationDate;
		this.blocks = blocks;
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
