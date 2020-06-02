package es.udc.paproject.backend.rest.dtos;

public class RatingParamsDto {
	Long userId;
	Long programId;
	Float rating;

	public RatingParamsDto() {}

	public RatingParamsDto(Long userId, Long programId, Float rating) {
		this.userId = userId;
		this.programId = programId;
		this.rating = rating;
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

	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}
}
