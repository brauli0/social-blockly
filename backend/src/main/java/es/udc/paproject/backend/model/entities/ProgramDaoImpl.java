package es.udc.paproject.backend.model.entities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class ProgramDaoImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Program> findByUserId(Long userId) {

		String queryString = "SELECT p FROM Program p WHERE p.user.id = :userId";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("userId", userId);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Program> findPublicByUserId(Long userId) {

		String queryString = "SELECT p FROM Program p WHERE p.user.id = :userId and p.privateProgram = false";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("userId", userId);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Program> findSharedPrograms(Long userId) {

		// Don't caring about privateProgram attribute
		String queryString = "SELECT p FROM Program p JOIN Shared s ON p.id = s.program.id "
				+ "WHERE s.user.id = :userId";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("userId", userId);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<User> findSharedUsers(Long programId) {

		String queryString = "SELECT u FROM User u JOIN Shared s ON u.id = s.user.id "
				+ "WHERE s.program.id = :programId";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("programId", programId);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Program> findProgramsSharedWithMeByUser(Long myUserId, Long otherUserId) {

		String queryString = "SELECT p FROM Program p JOIN Shared s ON p.id = s.program.id "
				+ "WHERE s.user.id = :myUserId AND p.user.id = :otherUserId";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("myUserId", myUserId);
		query.setParameter("otherUserId", otherUserId);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Program> findByKeyword(String keyword) {
		String queryString = "SELECT p FROM Program p "
				+ "WHERE p.privateProgram = false "
				+ "AND p.programName LIKE :keyword "
				+ "OR p.programDesc LIKE :keyword";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("keyword", "%"+keyword+"%");

		return query.setMaxResults(10).getResultList();
	}

	public void deleteSharedUsers(Long programId) {
		String queryString = "DELETE FROM Shared s WHERE s.program.id = :programId";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("programId", programId);

		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Program> findByExercise(Long exerciseId) {

		String queryString = "SELECT p FROM Program p JOIN Exercise e ON p.exercise.id = e.id "
				+ "WHERE e.id = :exerciseId";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("exerciseId", exerciseId);

		return query.getResultList();
	}
}
