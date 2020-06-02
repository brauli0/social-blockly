package es.udc.paproject.backend.model.services;

import java.util.List;

import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.Comment;

public interface CommentService {
	Comment createComment(Long userId, Long programId, String commentText)
			throws InstanceNotFoundException;

	Comment replyComment(Long commentOrigId, Long userId, Long programId, String commentText)
			throws InstanceNotFoundException;

	void deleteComment(Long commentId) throws InstanceNotFoundException;

	List<Comment> getCommentsByProgram(Long programId) throws InstanceNotFoundException;

	List<Comment> getCommentReplies(Long commentId) throws InstanceNotFoundException;

	Comment getComment(Long commentId) throws InstanceNotFoundException;
}
