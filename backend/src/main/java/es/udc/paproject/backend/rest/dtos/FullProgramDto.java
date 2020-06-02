package es.udc.paproject.backend.rest.dtos;

import java.util.Calendar;
import java.util.List;

public class FullProgramDto {
	private Long id;
    private Long userId;
    private String programName;
    private String programDesc;
    private Calendar creationDate;
    private Calendar updateDate;
    private String code;
    private Boolean privateProgram;
    private String visibility;
    private List<UserDto> sharedUsers;

    public FullProgramDto(Long id, Long userId, String programName, String programDesc,
    		Calendar creationDate, Calendar updateDate, String code, Boolean privateProgram, String visibility, List<UserDto> sharedUsers) {
		this.id = id;
    	this.userId = userId;
		this.programName = programName.trim();
		this.programDesc = programDesc.trim();
		this.creationDate = creationDate;
		this.updateDate = updateDate;
		this.code = code;
		this.privateProgram = privateProgram;
		this.visibility = visibility;
		this.sharedUsers = sharedUsers;
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

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public List<UserDto> getSharedUsers() {
		return sharedUsers;
	}

	public void setSharedUsers(List<UserDto> sharedUsers) {
		this.sharedUsers = sharedUsers;
	}
}