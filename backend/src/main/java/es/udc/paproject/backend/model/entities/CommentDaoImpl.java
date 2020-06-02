package es.udc.paproject.backend.model.entities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class CommentDaoImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Comment> getCommentsByProgram(Long programId) {
		String queryString = "SELECT c FROM Comment c WHERE c.program.id = :programId";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("programId", programId);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Comment> getCommentReplies(Long commentId) {
		String queryString = "SELECT c FROM Comment c WHERE c.commentOrigId = :commentId";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("commentId", commentId);

		return query.getResultList();
	}
}
