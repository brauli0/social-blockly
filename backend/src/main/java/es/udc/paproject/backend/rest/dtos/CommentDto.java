package es.udc.paproject.backend.rest.dtos;

import java.util.Calendar;

public class CommentDto {
	private Long id;
	private Long commentOrigId;
	private Long userId;
	private String userName;
	private Long programId;
    private String commentText;
    private Calendar commentDate;

	public CommentDto() {}

	public CommentDto(Long id, Long commentOrigId, Long userId, String userName, Long programId,
			String commentText, Calendar commentDate) {
		this.id = id;
		this.commentOrigId = commentOrigId;
		this.userId = userId;
		this.userName = userName;
		this.programId = programId;
		this.commentText = commentText;
		this.commentDate = commentDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public Calendar getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Calendar commentDate) {
		this.commentDate = commentDate;
	}
}
