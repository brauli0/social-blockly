package es.udc.paproject.backend.rest.dtos;

public class ProgramParamsDto {
	private Long userId;
	private Long exerciseId;
	private String programName;
	private String programDesc;
	private String code;
	private Boolean privateProgram;

	public ProgramParamsDto() {}

	public ProgramParamsDto(Long userId, Long exerciseId, String programName, String programDesc,
			String code, Boolean privateProgram) {
		this.userId = userId;
		this.exerciseId = exerciseId;
		this.programName = programName;
		this.programDesc = programDesc;
		this.code = code;
		this.privateProgram = privateProgram;
	}

	public final Long getUserId() {
		return userId;
	}

	public final void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(Long exerciseId) {
		this.exerciseId = exerciseId;
	}

	public final String getProgramName() {
		return programName;
	}

	public final void setProgramName(String programName) {
		this.programName = programName;
	}

	public final String getProgramDesc() {
		return programDesc;
	}

	public final void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}

	public final String getCode() {
		return code;
	}

	public final void setCode(String code) {
		this.code = code;
	}

	public Boolean getPrivateProgram() {
		return privateProgram;
	}

	public void setPrivateProgram(Boolean privateProgram) {
		this.privateProgram = privateProgram;
	}
}
