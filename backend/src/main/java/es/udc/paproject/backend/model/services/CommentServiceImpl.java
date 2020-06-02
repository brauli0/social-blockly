package es.udc.paproject.backend.model.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.Comment;
import es.udc.paproject.backend.model.entities.CommentDao;
import es.udc.paproject.backend.model.entities.CommentDaoImpl;
import es.udc.paproject.backend.model.entities.Program;
import es.udc.paproject.backend.model.entities.ProgramDao;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private ProgramDao programDao;

	@Autowired
	private CommentDao commentDao;

	@Autowired
	private CommentDaoImpl commentDaoImpl;

	@Override
	public Comment createComment(Long userId, Long programId, String commentText)
			throws InstanceNotFoundException {

		Optional<User> opUser = userDao.findById(userId);
		if (!opUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}
		User user = opUser.get();

		Optional<Program> opProgram = programDao.findById(programId);
		if (!opProgram.isPresent()) {
			throw new InstanceNotFoundException("project.entities.program", programId);
		}
		Program program = opProgram.get();

		Comment comment = new Comment(user, program, commentText);

		return commentDao.save(comment);
	}

	@Override
	public Comment replyComment(Long commentOrigId, Long userId, Long programId, String commentText)
			throws InstanceNotFoundException {
		Optional<User> opUser = userDao.findById(userId);
		if (!opUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}
		User user = opUser.get();

		Optional<Program> opProgram = programDao.findById(programId);
		if (!opProgram.isPresent()) {
			throw new InstanceNotFoundException("project.entities.program", programId);
		}
		Program program = opProgram.get();

		Optional<Comment> opComment = commentDao.findById(commentOrigId);
		if (!opComment.isPresent()) {
			throw new InstanceNotFoundException("project.entities.comment", commentOrigId);
		}

		Comment comment = new Comment(commentOrigId, user, program, commentText);

		return commentDao.save(comment);
	}

	@Override
	public void deleteComment(Long commentId) throws InstanceNotFoundException {
		Optional<Comment> comment = commentDao.findById(commentId);
		if (comment.isPresent())
			commentDao.delete(comment.get());
		else
			throw new InstanceNotFoundException("project.entities.comment", commentId);
	}

	@Override
	public List<Comment> getCommentsByProgram(Long programId) throws InstanceNotFoundException {
		Optional<Program> opProgram = programDao.findById(programId);
		if (!opProgram.isPresent()) {
			throw new InstanceNotFoundException("project.entities.program", programId);
		}

		return commentDaoImpl.getCommentsByProgram(programId);
	}

	@Override
	public List<Comment> getCommentReplies(Long commentId) throws InstanceNotFoundException {
		Optional<Comment> opComment= commentDao.findById(commentId);
		if (!opComment.isPresent()) {
			throw new InstanceNotFoundException("project.entities.comment", commentId);
		}

		return commentDaoImpl.getCommentReplies(commentId);
	}

	@Override
	public Comment getComment(Long commentId) throws InstanceNotFoundException {
		Optional<Comment> opComment = commentDao.findById(commentId);
		if (!opComment.isPresent()) {
			throw new InstanceNotFoundException("project.entities.program", commentId);
		}

		return opComment.get();
	}
}
