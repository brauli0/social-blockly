package es.udc.paproject.backend.rest.dtos;

public class CommentParamsDto2 {
	Long commentOrigId;
	Long userId;
	Long programId;
	String commentText;

	public CommentParamsDto2() {}

	public CommentParamsDto2(Long commentOrigId, Long userId, Long programId, String commentText) {
		this.commentOrigId = commentOrigId;
		this.userId = userId;
		this.programId = programId;
		this.commentText = commentText;
	}

	public Long getCommentOrigId() {
		return commentOrigId;
	}

	public void setCommentOrigId(Long commentOrigId) {
		this.commentOrigId = commentOrigId;
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
