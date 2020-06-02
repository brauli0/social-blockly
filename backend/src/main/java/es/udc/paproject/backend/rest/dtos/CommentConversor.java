package es.udc.paproject.backend.rest.dtos;

import java.util.ArrayList;
import java.util.List;

import es.udc.paproject.backend.model.entities.Comment;

public class CommentConversor {

	private CommentConversor() {}

	public final static CommentDto toCommentDto(Comment comment) {
		return new CommentDto(comment.getId(), comment.getCommentOrigId(),
				comment.getUser().getId(), comment.getUser().getUserName(),
				comment.getProgram().getId(), comment.getCommentText(), comment.getCommentDate());
	}

	public final static List<CommentDto> toCommentDtos(List<Comment> comments) {
		List<CommentDto> commentDtos = new ArrayList<>();

		for (Comment comment : comments) {
			commentDtos.add(new CommentDto(comment.getId(), comment.getCommentOrigId(),
					comment.getUser().getId(), comment.getUser().getUserName(),
					comment.getProgram().getId(), comment.getCommentText(), comment.getCommentDate()));
		}

		return commentDtos;
	}
}
