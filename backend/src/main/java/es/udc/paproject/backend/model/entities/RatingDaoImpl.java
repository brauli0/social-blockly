package es.udc.paproject.backend.model.entities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class RatingDaoImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Rating> getProgramRatings(Long programId) {
		String queryString = "SELECT r FROM Rating r WHERE r.program.id = :programId";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("programId", programId);

		return query.getResultList();
	}
}
