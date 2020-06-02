package es.udc.paproject.backend.rest.dtos;

import java.util.Calendar;

public class ExerciseParamsDto2 {
	Long exerciseId;
	String statementText;
	Calendar expirationDate;

	public ExerciseParamsDto2() {}

	public ExerciseParamsDto2(Long exerciseId, String statementText, Calendar expirationDate) {
		this.exerciseId = exerciseId;
		this.statementText = statementText;
		this.expirationDate = expirationDate;
	}

	public Long getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(Long exerciseId) {
		this.exerciseId = exerciseId;
	}

	public String getStatementText() {
		return statementText;
	}

	public void setStatementText(String statementText) {
		this.statementText = statementText;
	}

	public Calendar getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
	}
}
