package es.udc.paproject.backend.rest.dtos;

import java.util.Calendar;

public class ProgramDto {
	private Long id;
	private Long userId;
	private Long exerciseId;
    private String userName;
    private String programName;
    private String programDesc;
    private Calendar creationDate;
    private Calendar updateDate;
    private String code;
    private Boolean privateProgram;

    public ProgramDto(Long id, Long userId, Long exerciseId, String userName, String programName, String programDesc,
    		Calendar creationDate, Calendar updateDate, String code, Boolean privateProgram) {
		this.id = id;
		this.userId = userId;
		this.exerciseId = exerciseId;
    	this.userName = userName;
		this.programName = programName.trim();
		this.programDesc = programDesc.trim();
		this.creationDate = creationDate;
		this.updateDate = updateDate;
		this.code = code;
		this.privateProgram = privateProgram;
	}

	public final Long getId() {
		return id;
	}

	public final void setId(Long id) {
		this.id = id;
	}

	public final Long getUserId() {
		return userId;
	}

	public final void setUserId(Long userID) {
		this.userId = userID;
	}

	public Long getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(Long exerciseId) {
		this.exerciseId = exerciseId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public final Calendar getCreationDate() {
		return creationDate;
	}

	public final void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public Calendar getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Calendar updateDate) {
		this.updateDate = updateDate;
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
