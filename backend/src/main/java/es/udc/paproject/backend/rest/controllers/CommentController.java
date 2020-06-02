package es.udc.paproject.backend.rest.controllers;

import static es.udc.paproject.backend.rest.dtos.CommentConversor.toCommentDto;
import static es.udc.paproject.backend.rest.dtos.CommentConversor.toCommentDtos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.Comment;
import es.udc.paproject.backend.model.entities.Program;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.services.CommentService;
import es.udc.paproject.backend.model.services.PermissionException;
import es.udc.paproject.backend.model.services.ProgramService;
import es.udc.paproject.backend.rest.dtos.CommentDto;
import es.udc.paproject.backend.rest.dtos.CommentParamsDto;
import es.udc.paproject.backend.rest.dtos.CommentParamsDto2;
import es.udc.paproject.backend.rest.dtos.IdDto;

@RestController
@RequestMapping("/comment")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private ProgramService programService;

	@PostMapping("/create")
	public CommentDto createComment(@RequestAttribute Long userId,
			@RequestBody CommentParamsDto commentParamsDto)
			throws InstanceNotFoundException, PermissionException {

		if (!commentParamsDto.getUserId().equals(userId)) {
			throw new PermissionException();
		}

		return toCommentDto(commentService.createComment(commentParamsDto.getUserId(),
				commentParamsDto.getProgramId(), commentParamsDto.getCommentText()));
	}

	@PostMapping("/reply")
	public CommentDto createReply(@RequestAttribute Long userId,
			@RequestBody CommentParamsDto2 commentParamsDto2)
			throws InstanceNotFoundException, PermissionException {

		if (!commentParamsDto2.getUserId().equals(userId)) {
			throw new PermissionException();
		}

		return toCommentDto(commentService.replyComment(commentParamsDto2.getCommentOrigId(),
				commentParamsDto2.getUserId(), commentParamsDto2.getProgramId(),
				commentParamsDto2.getCommentText()));
	}

	@PostMapping("/delete")
	public void deleteComment(@RequestAttribute Long userId, @RequestBody IdDto idDto)
			throws InstanceNotFoundException, PermissionException {

		Comment comment = commentService.getComment(idDto.getId());
		if (comment.getUser().getId() != userId) {
			throw new PermissionException();
		}

		commentService.deleteComment(idDto.getId());
	}

	@GetMapping("/program/{programId}")
	public List<CommentDto> getCommentsByProgram(@RequestAttribute Long userId,
			@PathVariable Long programId)
			throws InstanceNotFoundException, PermissionException {

		Program program = programService.getProgram(programId);
		if (program.getPrivateProgram()) {
			if (program.getUser().getId() != userId) {
				List<User> users = programService.getSharedUsersByProgram(programId);

				boolean shared = false;
				if (users.size() == 0) {
					throw new PermissionException();
				}

				for (User user : users) {
					if (user.getId() == userId) {
						shared = true;
					}
				}

				if (!shared) {
					throw new PermissionException();
				}
			}
		}

		return toCommentDtos(commentService.getCommentsByProgram(programId));
	}

	@GetMapping("/comment/{commentId}")
	public List<CommentDto> getCommentReplies(@RequestAttribute Long userId,
			@PathVariable Long commentId)
			throws InstanceNotFoundException, PermissionException {

		Comment comment = commentService.getComment(commentId);
		Program program = programService.getProgram(comment.getProgram().getId());
		if (program.getPrivateProgram()) {
			if (program.getUser().getId() != userId) {
				List<User> users = programService.getSharedUsersByProgram(program.getId());

				boolean shared = false;
				if (users.size() == 0) {
					throw new PermissionException();
				}

				for (User user : users) {
					if (user.getId() == userId) {
						shared = true;
					}
				}

				if (!shared) {
					throw new PermissionException();
				}
			}
		}

		return toCommentDtos(commentService.getCommentReplies(commentId));
	}
}
