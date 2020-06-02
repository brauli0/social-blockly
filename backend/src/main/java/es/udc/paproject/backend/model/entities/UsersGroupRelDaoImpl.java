package es.udc.paproject.backend.model.entities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class UsersGroupRelDaoImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<User> getAllUsersByGroupId(Long groupId) {

		String queryString = "SELECT u FROM UsersGroupRel r JOIN User u ON r.groupId = u.id where r.groupId = :groupId;";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("groupId", groupId);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<UsersGroup> getAllGroupsByUserId(Long userId) {

		String queryString = "SELECT g FROM UsersGroupRel r JOIN UsersGroup g ON g.id = r.userId JOIN ON r.userId = g.id where userId = :userId;";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("userId", userId);

		return query.getResultList();
	}
}
