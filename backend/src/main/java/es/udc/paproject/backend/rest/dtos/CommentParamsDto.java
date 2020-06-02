package es.udc.paproject.backend.rest.dtos;

public class CommentParamsDto {
	Long userId;
	Long programId;
	String commentText;

	public CommentParamsDto() {}

	public CommentParamsDto(Long userId, Long programId, String commentText) {
		this.userId = userId;
		this.programId = programId;
		this.commentText = commentText;
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

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
}
