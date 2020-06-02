package es.udc.paproject.backend.rest.dtos;

public class ProgramParamsDto3 {
	Long ownerId;
	Long userId;
	Long programId;

	public ProgramParamsDto3() {}

	public ProgramParamsDto3(Long ownerId, Long userId, Long programId) {
		this.ownerId = ownerId;
		this.userId = userId;
		this.programId = programId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}
}
