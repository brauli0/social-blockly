package es.udc.paproject.backend.model.entities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class ExerciseDaoImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Exercise> getExercisesByGroup(Long groupId) {

		String queryString = "SELECT e FROM Exercise e WHERE e.group.id = :groupId";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("groupId", groupId);

		return query.getResultList();
	}
}
