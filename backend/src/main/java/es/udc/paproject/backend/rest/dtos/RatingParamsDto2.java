package es.udc.paproject.backend.rest.dtos;

public class RatingParamsDto2 {
	Long userId;
	Long programId;

	public RatingParamsDto2() {}

	public RatingParamsDto2(Long userId, Long programId) {
		this.userId = userId;
		this.programId = programId;
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
